package org.tearne
import java.nio.file.{Paths,Files}
import java.io.FileNotFoundException
import sampler.data.Types._
import sampler.data.Empirical._
import sampler.math.Random
import org.tearne.crosser.output.Writer
import org.slf4j.LoggerFactory
import org.tearne.crosser.config.HumanConfig
import org.tearne.crosser.RootComponent
import org.tearne.crosser.ServicesImpl
import org.tearne.crosser.config.ConfigFactory

object Crosser{
	val log = LoggerFactory.getLogger(getClass())
	
	def main(args: Array[String]) {
		log.info("""
            _______  ______  _____  _______ _______ _______  ______
            |       |_____/ |     | |______ |______ |______ |_____/
 org.tearne.|_____  |    \_ |_____| ______| ______| |______ |    \_                                                       			
""")
		val path = Paths.get(args(0)).toAbsolutePath
		if(!Files.exists(path)) throw new FileNotFoundException(path.toString())
		
		val conf = ConfigFactory.fromPath(path)

		trait RootComponentImpl extends RootComponent {
			val chunkSize = conf.chunkSize
			val tolerance = conf.tolerance
			val fewestPlants = conf.fewestPlants
			val random = Random
			log.trace("init Chunk size "+chunkSize)
		}
		
		val services = new RootComponentImpl with ServicesImpl
		
		val outDir = Paths.get("").resolve("output")
		if(!Files.exists(outDir)) Files.createDirectories(outDir)
		val writer = new Writer
		val outputsRequested = conf.outputs
		outputsRequested.map(out => 
			writer(outDir.resolve(out.fileName + ".csv"), out.buildData(services): _*)
		)
	}
}