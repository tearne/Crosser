package org.tearne
import java.nio.file.{Paths,Files}
import java.io.FileNotFoundException
import sampler.math.Random
import org.tearne.crosser.output.Writer
import org.slf4j.LoggerFactory
import org.tearne.crosser.config.HumanConfig
import org.tearne.crosser.ServicesImpl
import org.tearne.crosser.config.ConfigFactory
import org.tearne.crosser.config.Config
import java.nio.file.Path
import joptsimple.OptionParser
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.classic.util.ContextInitializer
import org.tearne.crosser.SystemConfig
import sampler.math.StatisticsComponent
import sampler.math.Statistics

object Crosser{
	def main(args: Array[String]) { 
		try{
			System.setProperty("LOG_DIR", "logs")
			val log = LoggerFactory.getLogger(getClass())
			log.info("Application started")
			
			import collection.JavaConversions._
			
			val op = new OptionParser()
			val fileOpt = op.acceptsAll(List("f", "file"), "Config from File").withRequiredArg().ofType(classOf[String]).describedAs("file")
			val urlOpt = op.acceptsAll(List("u", "url"), "Config from URL").withRequiredArg().ofType(classOf[String]).describedAs("url")
			val dirOpt = op.acceptsAll(List("o", "output"), "Output directory").withRequiredArg().ofType(classOf[String]).describedAs("dir")
			
			val options = op.parse(args: _*)
	
			def optionsError(msg: String){
				op.printHelpOn(System.out)
				throw new UnsupportedOperationException(msg)
			}
			
			def changeLoggingDir(dir: Path){
				log.info("Switching logging to {}", dir.toAbsolutePath())
				val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]
				val logConfigFile = new ContextInitializer(loggerContext).findURLOfDefaultConfigurationFile(false)
		
				val jc = new JoranConfigurator()
				jc.setContext(loggerContext)
				loggerContext.reset()
				loggerContext.putProperty("LOG_DIR", dir.toString())
				jc.doConfigure(logConfigFile)
			}
			
			if(options.has("f") && options.has("u")) optionsError("Cannot accept both file and url options")
			
			val outPath: Path = 
				if(options.has("o")) {
					val dir = Paths.get("").resolve(dirOpt.value(options)) 
					if(!Files.exists(dir))
						Files.createDirectory(dir)
					else if(!Files.isDirectory(dir)) optionsError(s"$dir exists and is not a directory")
					changeLoggingDir(dir)
					dir
				}
				else Paths.get("").toAbsolutePath()
			
			val config: Config = 
				if(options.has("u")){
					val url = urlOpt.value(options)
					ConfigFactory.fromURL(url)
				} else if(options.has("f")){
					val file = Paths.get(fileOpt.value(options))
					if(!Files.exists(file)) optionsError(s"Config file '$file' does not exist")
					ConfigFactory.fromPath(file)
				} else{
					op.printHelpOn(System.out)
					throw new UnsupportedOperationException("Input file or url option required")
				}
			
			run(config, outPath)
		} catch {
			case e: Exception => {
				LoggerFactory.getLogger(getClass()).error("Fatal Exception", e)
				System.exit(1)
			}
		}
		System.exit(0)
	}
	
	def run(conf: Config, workingDir: Path){
		val log = LoggerFactory.getLogger(getClass())

		log.info("""Welcome
            _______  ______  _____  _______ _______ _______  ______
            |       |_____/ |     | |______ |______ |______ |_____/
 org.tearne.|_____  |    \_ |_____| ______| ______| |______ |    \_                                                       			
""")
		log.info("Output Dir: {}", workingDir)
		log.info("Config name: {}", conf.name)

		trait SystemConfigImpl extends SystemConfig {
			val chunkSize = conf.chunkSize
			val tolerance = conf.tolerance
			val fewestPlants = conf.fewestPlants
			val random = Random
			log.trace("init Chunk size "+chunkSize)
		}
		
		val services = new SystemConfigImpl with ServicesImpl
		
		val outDir = workingDir
		if(!Files.exists(outDir)) Files.createDirectories(outDir)
		val writer = new Writer()
		val outputsRequested = conf.outputs
		outputsRequested.map(out => 
			writer.write(outDir.resolve(out.fileName + ".csv"), out.buildCSVLines(services))
		)
	}
}