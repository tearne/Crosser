package org.tearne.crosser.config

import java.nio.file.Path
import scala.util.Try
import org.slf4j.LoggerFactory
import com.typesafe.config.{ConfigFactory => TypesafeConfigFactory}
import java.net.URL
import scala.io.Source
import com.typesafe.config.{Config => TypesafeConfig}

object ConfigFactory {
	val log = LoggerFactory.getLogger(this.getClass())
	
	def fromPath(path: Path): Config = {
		val tConf = TypesafeConfigFactory.parseFile(path.toFile())
		fromTypesafeConfig(tConf)
	}
	
	def fromURL(url: String): Config = {
		val stream = new URL(url).openStream()
		val lines = Source.fromInputStream(stream).getLines
		val tConf = TypesafeConfigFactory.parseString(lines.next)
		assert(!lines.hasNext, "Expected only one line at config url")
		fromTypesafeConfig(tConf)
	}
	
	def fromTypesafeConfig(tConf: TypesafeConfig) = {
		Try{
			log.info("Try parsing human format config")
			new HumanConfig(tConf)
		}.getOrElse{
			log.info("Try parsing web format config")
			new WebConfig(tConf)
		}
	}
}