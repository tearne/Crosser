package org.tearne.crosser

import sampler.math.StatisticsComponent
import org.tearne.crosser.scheme.ConfigScheme
import java.nio.file.Paths
import java.nio.file.Files
import java.io.FileNotFoundException
import sampler.data.ParallelSampleBuilder
import org.tearne.crosser.util.AlleleCount
import sampler.io.CSVTableWriter
import sampler.data.Types._
import sampler.r.ScriptRunner
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant
import scala.collection.immutable.ListMap
import sampler.data.Empirical._
import sampler.data.Empirical
import sampler.math.Probability
import sampler.math.Random
import org.tearne.crosser.cross.Crossable
import java.io.FileWriter
import java.nio.charset.Charset
import org.tearne.crosser.scheme.Scheme

object ExampleAppTwo{
	
	def main(args: Array[String]) {
		val path = Paths.get(args(0)).toAbsolutePath
		if(!Files.exists(path)) throw new FileNotFoundException(path.toString())
		
		new Application(new ConfigScheme(path))
	}
	
	class Application(scheme: Scheme) extends CrosserServiceFactory with StatisticsComponent{
		val tolerance = scheme.tolerance
		val recombinationProb = scheme.recombinationProb
		val chunkSize = scheme.chunkSize

		def buildContributionDist(plant: Crossable, donor: Crossable) = (plant, donor) match {
			case (cross: Cross, donor: RootPlant) => {
				println(s"Cross $cross, Donor $donor")
				val crossDistribution = crossSamplerService.getDistributionFor(cross)
				new ParallelSampleBuilder(chunkSize)(crossDistribution)(seq => {
					println("loop size = "+seq.size)
					maxDistance(seq.take(seq.size - chunkSize).toEmpiricalSeq, seq.toEmpiricalSeq) < tolerance ||
					seq.size == 1e8
				})
				.map(_.alleleCount(donor).proportion).seq
			}
			case _ => throw new UnsupportedOperationException()
		}
		
		val requiredOutputs = for{
			cross <- scheme.crosses.values
			donor <- scheme.plants.values
		} yield (cross, donor)

		val table = requiredOutputs.map{case (cross, donor) => 
			(cross, donor) -> mean(buildContributionDist(cross, donor).toEmpiricalSeq)
		}
		
		val crossColumn = table.map(_._1._1.name)
		val donorColumn = table.map(_._1._2.name)
		val meanColumn = table.map(_._2)

		val columns = Seq(
			new Column(crossColumn.toSeq, "Cross"),
			new Column(donorColumn.toSeq, "Donor"),
			new Column(meanColumn.toSeq, "Mean")
		)
		new CSVTableWriter(Paths.get("confidence.csv"), true).apply(columns: _*)
		
		val lastCross = scheme.crosses.last._2
		val distributions = scheme.plants.values.map(donor => 
			donor -> buildContributionDist(lastCross, donor)
		).toMap
		
		val cols = distributions.foldLeft(List[(String, Double)]()){case (acc, (plant,dist)) => 
			acc ++ (Stream.continually(plant.name) zip dist)
		}.unzip
		
		new CSVTableWriter(Paths.get("lastCrossDist.csv"), true).apply(
			new Column(cols._1, "FromDonor"),
			new Column(cols._2, "Sample")
		)

		val rcmd =
"""
require(ggplot2)
require(reshape)
pdf("plot.pdf", width=11.6, height=4.1) #1/2 A6 landscape paper
data = read.csv("confidence.csv")
data$Cross = ordered(data$Cross, levels=c("F1_1", "F1_2", "F1_3", "BC1F1", "BC2F1", "BC2F1S1", "BC2F1S2", "BC2F1S3", "BC2F1S4"))
ggplot(data, aes(x=Cross, colour=Donor, fill=Donor, y=Mean)) +
			geom_bar(stat="identity") +
			scale_y_continuous(name="Mean Proportions")
			
dist = read.csv("lastCrossDist.csv")
ggplot(dist, aes(x=Sample, colour=FromDonor)) + 
			geom_density() + 
			labs(colour="Donor", title="Density of donor contributions in BC2F1S4") +
			scale_x_continuous(name="Proportion") +
			scale_y_continuous(name="Dentisy")
dev.off()
"""

		ScriptRunner(rcmd, Paths.get("plot.R").toAbsolutePath())
	}
}