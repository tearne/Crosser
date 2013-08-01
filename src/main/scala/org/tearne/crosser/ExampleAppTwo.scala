package org.tearne.crosser

import sampler.math.StatisticsComponent
import java.nio.file.{Paths,Files}
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
import sampler.math.Probability
import sampler.math.Random
import org.tearne.crosser.cross.Crossable
import java.io.FileWriter
import java.nio.charset.Charset
import org.tearne.crosser.output.DistributionBuilder
import org.tearne.crosser.output.Writer
import org.tearne.crosser.config.HumanConfig
import org.tearne.crosser.config.Config

object ExampleAppTwo{
	
	def main(args: Array[String]) {
		val path = Paths.get("examples").resolve("Tanzania2013.config").toAbsolutePath
		if(!Files.exists(path)) throw new FileNotFoundException(path.toString())
		
		new Application(new HumanConfig(path))
	}
	
	class Application(config: Config){
		implicit val random: Random = Random
		val tolerance = config.tolerance
		val chunkSize = config.chunkSize

		val service = CrosserServiceFactory(tolerance, chunkSize, 0.01)
		
		val wd = Paths.get("")
		//TODO same tolerance as building plant distributions
		val writer = new Writer
		val distBuilder = new DistributionBuilder(StatisticsComponent, tolerance, chunkSize)
		val outputsRequested = config.outputs
		println(" req = "+outputsRequested.map(_.name))
//		val outputs = 
		outputsRequested.map(out => 
			writer(wd.resolve(out.name), out.buildData(service, distBuilder): _*)
		)
//		outputs.foreach(a => writer(wd.resolve(), a: _*))
		
		System.exit(0)
		
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