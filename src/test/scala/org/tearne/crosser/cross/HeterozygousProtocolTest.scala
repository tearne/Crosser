package org.tearne.crosser.cross

import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.cross.LocusPresence._
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.ConcretePlant
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

class HeterozygousProtocolTest extends FreeSpec with MockitoSugar{
	val locusOnC1 = Locus(null, 1, 1, null)
	val locusOnC2 = Locus(null, 2, 2, null)

	"HeterozygousProtocol should" - {
		"Be a protocol" in {
			assert(HeterozygousProtocol(Set[Locus]()).isInstanceOf[Protocol])
		}
		"Fail if any of the loci not present in required chromosomes" in {
			val instance = HeterozygousProtocol(Set(locusOnC1, locusOnC2))
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			when(c1.satisfies(locusOnC1, true)).thenReturn(AtLeastHeterozygously)
			when(c2.satisfies(locusOnC1, true)).thenReturn(No)
			
			when(c1.satisfies(locusOnC2, true)).thenReturn(No)
			when(c2.satisfies(locusOnC2, true)).thenReturn(No)
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[ConcretePlant]
			when(plant.chromosomes).thenReturn(chromosomes)
			
			assert(!instance.isSatisfiedBy(plant))
		}
		"Pass if all loci present in required chromosomes" in {
			val instance = HeterozygousProtocol(Set(locusOnC1, locusOnC2))
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			//True indicated short circuit checking for a heterozygous protocol
			when(c1.satisfies(locusOnC1, true)).thenReturn(AtLeastHeterozygously)
			when(c2.satisfies(locusOnC1, true)).thenReturn(No)
			
			when(c1.satisfies(locusOnC2, true)).thenReturn(No)
			when(c2.satisfies(locusOnC2, true)).thenReturn(AtLeastHeterozygously)
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[Plant]
			when(plant.chromosomes).thenReturn(chromosomes)
			
			assert(instance.isSatisfiedBy(plant))
		}
		"pass if all traits het present and exact num hom-ly present" in {
			val locusOnC2_2 = Locus(null, 2, 3, null)
			val instance = HeterozygousProtocol(Set(locusOnC1, locusOnC2, locusOnC2_2), 2)
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			when(c1.satisfies(locusOnC1)).thenReturn(Heterozygously)
			when(c2.satisfies(locusOnC1)).thenReturn(No)
			
			when(c1.satisfies(locusOnC2)).thenReturn(No)
			when(c2.satisfies(locusOnC2)).thenReturn(Homozygously)
			
			when(c1.satisfies(locusOnC2_2)).thenReturn(No)
			when(c2.satisfies(locusOnC2_2)).thenReturn(Homozygously)
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[Plant]
			when(plant.chromosomes).thenReturn(chromosomes)
			
			assert(instance.isSatisfiedBy(plant))
		}
		"pass if all traits het present and > correct num hom-ly present" in {
			val instance = HeterozygousProtocol(Set(locusOnC1, locusOnC2), 1)
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			when(c1.satisfies(locusOnC1)).thenReturn(Homozygously)
			when(c2.satisfies(locusOnC1)).thenReturn(No)
			
			when(c1.satisfies(locusOnC2)).thenReturn(No)
			when(c2.satisfies(locusOnC2)).thenReturn(Homozygously)
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[Plant]
			when(plant.chromosomes).thenReturn(chromosomes)
			
			assert(instance.isSatisfiedBy(plant))
		}
		"it's ok if short-circuit question returns exact answer" in {
			val instance = HeterozygousProtocol(Set(locusOnC1, locusOnC1))
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			when(c1.satisfies(locusOnC1, true)).thenReturn(AtLeastHeterozygously)
			when(c2.satisfies(locusOnC1, true)).thenReturn(No)
			
			when(c1.satisfies(locusOnC2, true)).thenReturn(No)
			when(c2.satisfies(locusOnC2, true)).thenReturn(Heterozygously)
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[Plant]
			when(plant.chromosomes).thenReturn(chromosomes)
			
			assert(instance.isSatisfiedBy(plant))
		}
		"throws exception if non short-circuit question returns short-circuit answer" in {
			val instance = HeterozygousProtocol(Set(locusOnC1, locusOnC1), 1)
			
			val c1 = mock[Chromosome]
			val c2 = mock[Chromosome]
			
			when(c1.satisfies(locusOnC1)).thenReturn(AtLeastHeterozygously) //This is the error
			when(c2.satisfies(locusOnC1)).thenReturn(No)
			
			when(c1.satisfies(locusOnC2)).thenReturn(No)
			when(c2.satisfies(locusOnC2)).thenReturn(Homozygously)
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[ConcretePlant]
			when(plant.chromosomes).thenReturn(chromosomes)
			
			intercept[ProtocolException]{
				instance.isSatisfiedBy(plant)
			}
		}
		"have value based hashcode and equals" in {
			val instance1a = HeterozygousProtocol(Set(locusOnC1, locusOnC2))
			val instance1b = HeterozygousProtocol(Set(locusOnC1, locusOnC2), None)
			val instance2 = HeterozygousProtocol(Set(locusOnC1, locusOnC2), 1)
			
			assertResult(instance1b)(instance1a)
			assert(instance1a != instance2)
			assertResult(instance1b.hashCode)(instance1a.hashCode)
			assert(instance1a.hashCode != instance2.hashCode)
		}
	}
	
}