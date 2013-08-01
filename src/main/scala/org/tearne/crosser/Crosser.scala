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
import org.tearne.crosser.config.Config
import org.tearne.crosser.output.DistributionBuilder
import org.tearne.crosser.output.Writer
import org.slf4j.LoggerFactory

object Crosser{
	val log = LoggerFactory.getLogger(getClass())
	
	def main(args: Array[String]) {
		log.info("""
 _______  ______  _____  _______ _______ _______  ______
 |       |_____/ |     | |______ |______ |______ |_____/
 |_____  |    \_ |_____| ______| ______| |______ |    \_                                                       			
""")
		val path = Paths.get(args(0)).toAbsolutePath
		if(!Files.exists(path)) throw new FileNotFoundException(path.toString())
		
		new Application(new Config(path))
	}
	
	class Application(config: Config){
		implicit val random: Random = Random
		val tolerance = config.tolerance
		val chunkSize = config.chunkSize

		val service = CrosserServiceFactory(tolerance, chunkSize, 0.01)
		
		val wd = Paths.get("")
		val writer = new Writer
		val distBuilder = new DistributionBuilder(StatisticsComponent, tolerance, chunkSize)
		val outputsRequested = config.outputs
		outputsRequested.map(out => 
			writer(wd.resolve(out.name + ".csv"), out.buildData(service, distBuilder): _*)
		)
	}
}