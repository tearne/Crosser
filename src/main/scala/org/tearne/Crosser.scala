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
import org.tearne.crosser.config.Config
import java.nio.file.Path

object Crosser{
	val log = LoggerFactory.getLogger(getClass())
	
	def printUsage(){
		println(
"""
Bad args.  Usage:
	-f, --file	Load config from disk
	-u, --url	Load config from URL
"""
		)
	}
	
	def main(args: Array[String]) {
		def fromFile(s: String) = {s == "-f" || s == "--file"}
		def fromUrl(s: String) = {s == "-u" || s == "--url"}
		args.toList match{
			case List(switch, location) if fromFile(switch) =>
				val path = Paths.get(location)
				val workingDir = path.getParent()
				if(!Files.exists(path)) throw new FileNotFoundException(path.toString())
				log.info(s"Config: ${path.getFileName}")
				log.info(s"Working dir: $workingDir")
				run(ConfigFactory.fromPath(path), path.getParent())
			case List(switch, location) if fromUrl(switch) =>
				val config = ConfigFactory.fromURL(location)
				val workingDir = Paths.get(config.name)
				if(!Files.exists(workingDir)) Files.createDirectory(workingDir)
				log.info(s"Config: $location")
				log.info(s"Working dir: ${workingDir.toAbsolutePath()}")
				run(config, workingDir)
			case _ => 
				printUsage
		}	
	}
	
	def run(conf: Config, workingDir: Path){
		log.info("""Welcome
            _______  ______  _____  _______ _______ _______  ______
            |       |_____/ |     | |______ |______ |______ |_____/
 org.tearne.|_____  |    \_ |_____| ______| ______| |______ |    \_                                                       			
""")
//		val path = Paths.get(args(0)).toAbsolutePath
//		val workingDir = path.getParent
//		if(!Files.exists(path)) throw new FileNotFoundException(path.toString())
		
//		val conf = ConfigFactory.fromPath(path)

		trait RootComponentImpl extends RootComponent {
			val chunkSize = conf.chunkSize
			val tolerance = conf.tolerance
			val fewestPlants = conf.fewestPlants
			val random = Random
			log.trace("init Chunk size "+chunkSize)
		}
		
		val services = new RootComponentImpl with ServicesImpl
		
		val outDir = workingDir
		if(!Files.exists(outDir)) Files.createDirectories(outDir)
		val writer = new Writer
		val outputsRequested = conf.outputs
		outputsRequested.map(out => 
			writer(outDir.resolve(out.fileName + ".csv"), out.buildData(services): _*)
		)
	}
}