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
import joptsimple.OptionParser

object Crosser{
	val log = LoggerFactory.getLogger(getClass())

	def main(args: Array[String]) {
		val op = new OptionParser()
		import collection.JavaConversions._
		
		val fileOpt = op.acceptsAll(List("f", "file"), "Config from File").withRequiredArg().ofType(classOf[String]).describedAs("file")
		val urlOpt = op.acceptsAll(List("u", "url"), "Config from URL").withRequiredArg().ofType(classOf[String]).describedAs("url")
		val dirOpt = op.accepts("o", "Output directory").withRequiredArg().ofType(classOf[String]).describedAs("dir")
		
		val options = op.parse(args: _*)

		def optionsError(msg: String){
			op.printHelpOn(System.out)
			throw new UnsupportedOperationException(msg)
		}
		
		if(options.has("f") && options.has("u")) optionsError("Cannot accept both file and url options")
		
		val outPath: Path = 
			if(options.has("o")) {
				val dir = Paths.get("").resolve(dirOpt.value(options)) 
				if(!Files.exists(dir))
					Files.createDirectory(dir)
				else if(!Files.isDirectory(dir)) optionsError(s"$dir exists and is not a directory")
				dir
			}
			else Paths.get("").toAbsolutePath()
		
		val config: Config = 
			if(options.has("u")){
				val url = urlOpt.value(options)
				log.info("Config from URL {}", url)
				ConfigFactory.fromURL(url)
			} else if(options.has("f")){
				val file = Paths.get(fileOpt.value(options))
				if(!Files.exists(file)) optionsError(s"Config file '$file' does not exist")
				log.info("Config from file {}", file)
				ConfigFactory.fromPath(file)
			} else{
				op.printHelpOn(System.out)
				throw new UnsupportedOperationException("Input file or url option required")
			}
		
		log.info("Output Dir = {}", outPath)
		run(config, outPath)
	}
	
	def run(conf: Config, workingDir: Path){
		log.info("""Welcome
            _______  ______  _____  _______ _______ _______  ______
            |       |_____/ |     | |______ |______ |______ |_____/
 org.tearne.|_____  |    \_ |_____| ______| ______| |______ |    \_                                                       			
""")

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