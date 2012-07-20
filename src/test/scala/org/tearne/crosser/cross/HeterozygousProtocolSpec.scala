package org.tearne.crosser.cross

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.cross.LocusPresence._
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.ConcretePlant

@RunWith(classOf[JUnitRunner])
class HeterozygousProtocolSpec extends Specification with Mockito{
	val aLocus = Locus(null, 1, 0, null)
	val bLocus = Locus(null, 2, 0, null)

	"HeterozygousProtocol" should{
		"be a protocol" in {
			HeterozygousProtocol(Set[Locus]()) must beAnInstanceOf[Protocol]
		}
		"fail if any of the loci not present in required chromosomes" in {
			val instance = HeterozygousProtocol(Set(aLocus, bLocus))
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			c1.satisfies(aLocus, true) returns AtLeastHeterozygously
			c2.satisfies(aLocus, true) returns No
			
			c1.satisfies(bLocus, true) returns No
			c2.satisfies(bLocus, true) returns No
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[ConcretePlant]
			plant.chromosomes returns chromosomes
			
			instance.isSatisfiedBy(plant) mustEqual false
		}
		"pass if all loci present in required chromosomes" in {
			val instance = HeterozygousProtocol(Set(aLocus, bLocus))
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			//True indicated short circuit checking
			c1.satisfies(aLocus, true) returns AtLeastHeterozygously
			c2.satisfies(aLocus, true) returns No
			
			c1.satisfies(bLocus, true) returns No
			c2.satisfies(bLocus, true) returns AtLeastHeterozygously
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[Plant]
			plant.chromosomes returns chromosomes
			
			instance.isSatisfiedBy(plant) mustEqual true
		}
		"pass if all traits het present and exact num hom-ly present" in {
			val cLocus = Locus(null, 3, 0, null)
			val instance = HeterozygousProtocol(Set(aLocus, bLocus, cLocus), 2)
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			c1.satisfies(aLocus) returns Heterozygously
			c2.satisfies(aLocus) returns No
			
			c1.satisfies(bLocus) returns No
			c2.satisfies(bLocus) returns Homozygously
			
			c1.satisfies(cLocus) returns No
			c2.satisfies(cLocus) returns Homozygously
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[Plant]
			plant.chromosomes returns chromosomes
			
			instance.isSatisfiedBy(plant) mustEqual true
		}
		"pass if all traits het present and > correct num hom-ly present" in {
			val instance = HeterozygousProtocol(Set(aLocus, bLocus), 1)
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			c1.satisfies(aLocus) returns Homozygously
			c2.satisfies(aLocus) returns No
			
			c1.satisfies(bLocus) returns No
			c2.satisfies(bLocus) returns Homozygously
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[Plant]
			plant.chromosomes returns chromosomes
			
			instance.isSatisfiedBy(plant) mustEqual true
		}
		"it's ok if short-circuit question returns exact answer" in {
			val instance = HeterozygousProtocol(Set(aLocus, bLocus))
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			c1.satisfies(aLocus, true) returns AtLeastHeterozygously
			c2.satisfies(aLocus, true) returns No
			
			c1.satisfies(bLocus, true) returns No
			c2.satisfies(bLocus, true) returns Heterozygously
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[Plant]
			plant.chromosomes returns chromosomes
			
			instance.isSatisfiedBy(plant) mustEqual true
		}
		"throws exception if non short-circuit question returns short-circuit answer" in {
			val instance = HeterozygousProtocol(Set(aLocus, bLocus), 1)
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			c1.satisfies(aLocus) returns AtLeastHeterozygously //Error
			c2.satisfies(aLocus) returns No
			
			c1.satisfies(bLocus) returns No
			c2.satisfies(bLocus) returns Homozygously
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[Plant]
			plant.chromosomes returns chromosomes
			
			instance.isSatisfiedBy(plant) must throwA[ProtocolException]
		}
		"have value based hashcode and equals" in {
			val instance1a = HeterozygousProtocol(Set(aLocus, bLocus))
			val instance1b = HeterozygousProtocol(Set(aLocus, bLocus), None)
			val instance2 = HeterozygousProtocol(Set(aLocus, bLocus), 1)
			
			(instance1a mustEqual instance1b) and
			(instance1a mustNotEqual instance2) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a.hashCode mustNotEqual instance2.hashCode)
		}
	}
	
}