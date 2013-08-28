package org.tearne.crosser.config

import java.nio.file.Path
import scala.util.Try

object ConfigFactory {
	def fromPath(path: Path): Config = {
		Try{
			new HumanConfig(path)
		}.getOrElse{
			new WebConfig(path)
		}
	}
}