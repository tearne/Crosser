package org.tearne.crosser.cross

import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.cross.LocusPresence._
import org.tearne.crosser.plant.ConcretePlant
import org.scalatest.mock.MockitoSugar
import org.scalatest.FreeSpec
import org.mockito.Mockito._

class HomozygousProtocolTest extends FreeSpec with MockitoSugar{
	val locusOnC1 = Locus(null, 1, 1, null)
	val locusOnC2 = Locus(null, 2, 2, null)
	
	"HomozygousProtocol should" - {
		"Be a protocol" in {
			assert(HomozygousProtocol(Set[Locus]()).isInstanceOf[Protocol])
		}
		"Fail if one of the traits is homozygously missing" in new Setup{
			when(c1.satisfies(locusOnC1)).thenReturn(Homozygously)
			when(c2.satisfies(locusOnC1)).thenReturn(No)
			
			when(c1.satisfies(locusOnC2)).thenReturn(No)
			when(c2.satisfies(locusOnC2)).thenReturn(Heterozygously)
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[ConcretePlant]
			when(plant.chromosomes).thenReturn(chromosomes)
			
			assert(!instance.isSatisfiedBy(plant))
		}
		"pass if all traits homozygously present" in new Setup{
			when(c1.satisfies(locusOnC1)).thenReturn(Homozygously)
			when(c2.satisfies(locusOnC1)).thenReturn(No)
			
			when(c1.satisfies(locusOnC2)).thenReturn(No)
			when(c2.satisfies(locusOnC2)).thenReturn(Homozygously)
			
			val chromosomes = IndexedSeq[Chromosome](c1, c2)
			val plant = mock[ConcretePlant]
			when(plant.chromosomes).thenReturn(chromosomes)
			
			assert(instance.isSatisfiedBy(plant))
		}
		"throws exception if obtains a short-circuit answer" in new Setup{
			//Homozygous questions shouldn't get short circuit answers
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
			val instance1a = HomozygousProtocol(Set(locusOnC1, locusOnC2))
			val instance1b = HomozygousProtocol(Set(locusOnC1, locusOnC2))
			val instance2 = HomozygousProtocol(Set(locusOnC1))
			
			assertResult(instance1b)(instance1a)
			assert(instance1a != instance2)
			assertResult(instance1b.hashCode)(instance1a.hashCode)
			assert(instance1a.hashCode != instance2.hashCode)
		}
	}
	
	trait Setup {
		val instance = HomozygousProtocol(Set(locusOnC1, locusOnC2))
		val c1 = mock[Chromosome]
		val c2 = mock[Chromosome]
	}
}