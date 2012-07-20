package org.tearne.crosser.cross

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HomozygousProtocolSpec extends Specification {
	"HomozygousProtocol" should{
		"be a protocol" in {
			HomozygousProtocol(Set[Locus]()) must beAnInstanceOf[Protocol]
		}
		"fail if one of the traits is homozygously missing" in todo
		"pass if all traits homozygously present" in todo
		"have value based hashcode and equals" in todo
	}
}