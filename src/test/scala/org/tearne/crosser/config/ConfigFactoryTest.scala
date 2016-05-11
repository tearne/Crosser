package org.tearne.crosser.config

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import java.nio.file.Paths
import org.scalatest.FreeSpec

class ConfigFactoryTest extends FreeSpec {

	"ConfigFactory should" - {
		val expected = "MyCross"
		
		"parse a 'human' config without error" in {
			val path = Paths.get("src/test/resource/testHuman.config")
			val instance = ConfigFactory.fromPath(path)
			assertResult(expected)(instance.name)
		}
		"parse a 'web' config without error" in {
			val path = Paths.get("src/test/resource/testWeb.config")
			val instance = ConfigFactory.fromPath(path)
			assertResult(expected)(instance.name)
		}
	}	
}