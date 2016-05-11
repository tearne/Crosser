package org.tearne.crosser.plant

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.util.AlleleCount
import sampler.data.Samplable
import sampler.data.Distribution
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

class PlantTest extends FreeSpec with MockitoSugar{
	val species = Species("foo", 1,2,3)
	val instance = Plant("bar", species.buildChromosomesFrom(mock[RootPlant]), species)
	val otherSpecies = Species("foo2", 1,2,3,4)
	
	"Plant should" - {
		"give a count of donor alleles present" in {
			val chromosomes = IndexedSeq(mock[Chromosome],mock[Chromosome],mock[Chromosome])
			val donor = mock[RootPlant]
			
			when(chromosomes(0).size) thenReturn 1
			when(chromosomes(1).size) thenReturn 2
			when(chromosomes(2).size) thenReturn 3
			
			when(chromosomes(0).alleleCount(donor)) thenReturn AlleleCount(10,20)
			when(chromosomes(1).alleleCount(donor)) thenReturn AlleleCount(11,30)
			when(chromosomes(2).alleleCount(donor)) thenReturn AlleleCount(12,40)
			
			val instance = Plant("bar", chromosomes, species)
			assertResult(AlleleCount(33, 90))(instance.alleleCount(donor)) 
		}
		"be a concrete plant" in {
			assert(instance.isInstanceOf[ConcretePlant])
		}
		"be crossable" in {
			assert(instance.isInstanceOf[Crossable])
		}
		"be samplable" in{
			assert(instance.isInstanceOf[Distribution[ConcretePlant]])
		}
		"return itself when sampled" in {
			assertResult(instance)(instance.sample)
		}
		"throw exception if chromosome lengths incompatible with species" in {
			intercept[PlantException]{
				Plant("bar", otherSpecies.buildChromosomesFrom(mock[RootPlant]), species)
			}
		}
		"have value based hashcode and equals" in {
			val chromosomes1 = species.buildChromosomesFrom(mock[RootPlant])
			val chromosomes2 = otherSpecies.buildChromosomesFrom(mock[RootPlant])
			
			val instance1a = Plant("bar", chromosomes1, species)
			val instance1b = Plant("bar", chromosomes1, species)
			val instance2 = Plant("bar2", chromosomes1, species)
			val instance3 = Plant("bar", chromosomes2, otherSpecies)
			
			assertResult(instance1b)(instance1a)
			assert(instance1a != instance2)
			assert(instance1a != instance3)
			assertResult(instance1b.hashCode)(instance1a.hashCode)
			assert(instance1a.hashCode != instance2.hashCode)
			assert(instance1a.hashCode != instance3.hashCode)
		}
	}
}