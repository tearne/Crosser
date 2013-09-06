package org.tearne.crosser.config

import java.nio.file.Path
import scala.util.Try
import org.slf4j.LoggerFactory

object ConfigFactory {
	val log = LoggerFactory.getLogger(this.getClass())
	
	def fromPath(path: Path): Config = {
		Try{
			log.info("Trying to human-format config")
			new HumanConfig(path)
		}.getOrElse{
			log.info("Trying to read web-format config")
			new WebConfig(path)
		}
	}
}