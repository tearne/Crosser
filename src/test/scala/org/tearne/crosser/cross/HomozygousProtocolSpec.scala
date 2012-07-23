package org.tearne.crosser.cross

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.cross.LocusPresence._
import org.tearne.crosser.plant.ConcretePlant
import org.specs2.specification.Scope

@RunWith(classOf[JUnitRunner])
class HomozygousProtocolSpec extends Specification with Mockito{
	val locusOnC1 = Locus(null, 1, 1, null)
	val locusOnC2 = Locus(null, 2, 2, null)
	
	"HomozygousProtocol" should{
		"be a protocol" in {
			HomozygousProtocol(Set[Locus]()) must beAnInstanceOf[Protocol]
		}
		"fail if one of the traits is homozygously missing" in new Setup{
			c1.satisfies(locusOnC1) returns Homozygously
			c2.satisfies(locusOnC1) returns No
			
			c1.satisfies(locusOnC2) returns No
			c2.satisfies(locusOnC2) returns Heterozygously
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[ConcretePlant]
			plant.chromosomes returns chromosomes
			
			instance.isSatisfiedBy(plant) mustEqual false
		}
		"pass if all traits homozygously present" in new Setup{
			c1.satisfies(locusOnC1) returns Homozygously
			c2.satisfies(locusOnC1) returns No
			
			c1.satisfies(locusOnC2) returns No
			c2.satisfies(locusOnC2) returns Homozygously
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[ConcretePlant]
			plant.chromosomes returns chromosomes
			
			instance.isSatisfiedBy(plant) mustEqual true
		}
		"throws exception if obtains a short-circuit answer" in new Setup{
			//Homozygous questions shouldn't get short circuit answers
			c1.satisfies(locusOnC1) returns AtLeastHeterozygously //Error
			c2.satisfies(locusOnC1) returns No
			
			c1.satisfies(locusOnC2) returns No
			c2.satisfies(locusOnC2) returns Homozygously
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[ConcretePlant]
			plant.chromosomes returns chromosomes
			
			instance.isSatisfiedBy(plant) must throwA[ProtocolException]
		}
		"have value based hashcode and equals" in {
			val instance1a = HomozygousProtocol(Set(locusOnC1, locusOnC2))
			val instance1b = HomozygousProtocol(Set(locusOnC1, locusOnC2))
			val instance2 = HomozygousProtocol(Set(locusOnC1))
			
			(instance1a mustEqual instance1b) and
			(instance1a mustNotEqual instance2) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a.hashCode mustNotEqual instance2.hashCode)
		}
	}
	
	trait Setup extends Scope {
		val instance = HomozygousProtocol(Set(locusOnC1, locusOnC2))
		val c1 = mock[Chromosome]
		val c2 = mock[Chromosome]
	}
}