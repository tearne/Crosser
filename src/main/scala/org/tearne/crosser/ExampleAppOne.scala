package org.tearne.crosser

import sampler.math.StatisticsComponent
import org.tearne.crosser.scheme.Scheme
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

object ExampleAppOne{
	
	def main(args: Array[String]) {
		val path = Paths.get(args(0)).toAbsolutePath
		if(!Files.exists(path)) throw new FileNotFoundException(path.toString())
		
		new Application(new Scheme(path))
	}
	
	class Application(scheme: Scheme) extends CrosserServiceFactory with StatisticsComponent{
		val tolerance = scheme.tolerance
		val recombinationProb = scheme.recombinationProb
		val chunkSize = scheme.chunkSize
		
		val finalCross = scheme.crosses.last._2
		println(finalCross.name)
		println("Final cross is "+finalCross.protocol)
		
		println("done 1")
		
		def getDistribution(cross: Cross, donor: RootPlant): Seq[Double] = {
			val crossDistribution = crossSamplerService.getDistributionFor(cross)
			new ParallelSampleBuilder(chunkSize)(crossDistribution)(seq => 
				seq.size == 1e6
			)
			.map(_.alleleCount(donor).proportion).seq
		}
		
		println(scheme.output.map(_.name))
		
		val dists = ListMap(
			scheme
			.output
			.map{cross => (cross.name, getDistribution(cross, scheme.outputDonor))}: _*
		)
		
		val writer = new CSVTableWriter(Paths.get("out.csv"), true)
		val columns = dists.map{case (name, dist) =>
				new Column(dist, name)
			}.toSeq
		writer.apply(columns: _*)
	
		val posteriorPlotScript = 
"""
require(ggplot2)
require(reshape)
			
data = read.csv("out.csv")
			
pdf("out.pdf", width=8.27, height=5.83)
ggplot(melt(data), aes(x=value, colour=variable)) +
	geom_freqpoly() +
	scale_x_continuous(limits=c(0,1), name="Proportion of donor present") +
	scale_y_continuous(name="Density")
dev.off()
"""
	val p = Paths.get("out.r").toAbsolutePath()
	println(p.toString())
			
	ScriptRunner(posteriorPlotScript, p)
	
	}
		
		
}