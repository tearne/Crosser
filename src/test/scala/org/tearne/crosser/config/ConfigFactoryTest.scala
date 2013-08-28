package org.tearne.crosser.config

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import java.nio.file.Paths

class ConfigFactoryTest extends AssertionsForJUnit {

	val expected = "MyCross"
	
	@Test def parsingHumanConfig {
		val path = Paths.get("src/test/resource/testHuman.config")
		val instance = ConfigFactory.fromPath(path)
		assert(instance.name === expected)
	}
	
	@Test def parsingWebConfig {
		val path = Paths.get("src/test/resource/testWeb.config")
		val instance = ConfigFactory.fromPath(path)
		assert(instance.name === expected)
	}
}