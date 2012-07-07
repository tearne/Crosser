package org.tearne.crosser.cross

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HeterozygousProtocolSpec extends Specification {
	"HeterozygousProtocol" should{
		"be a protocol" in todo
		"fail if one of the traits is heterozygously missing" in todo
		"pass if all traits heterozygously present" in todo
		"pass if all traits het present and == num hom-ly present" in todo
		"pass if all traits het present and > correct num hom-ly present" in todo
		"have value based hashcode and equals" in todo
	}
}