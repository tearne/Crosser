package org.tearne.crosser.requirement

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProtocolSpec extends Specification {
	"Protocol" should{
		"fail if one of the traits is heterozygously missing" in todo
		"fail if one of the traits is homozygously missing" in todo
		"pass if all traits present" in todo
		"have value based hashcode and equals" in todo
	}
}