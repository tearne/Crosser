package org.tearne.crosser.plant

import org.tearne.crosser.cross.Crossable
import sampler.data.Distribution
import org.scalatest.mock.MockitoSugar
import org.scalatest.FreeSpec

class RootPlantSpec extends FreeSpec with MockitoSugar{
	val species1 = Species("species1", 1,2,3)
	val species2 = Species("species2", 1,2,3)
	val instance = RootPlant("Donor", species1)
	
	"RootPlant should" - {
		"Be a concrete plant" in {
			assert(instance.isInstanceOf[ConcretePlant])
		}
		"Be crossable" in {
			assert(instance.isInstanceOf[Crossable])
		}
		"Be samplable" in{
			assert(instance.isInstanceOf[Distribution[ConcretePlant]])
		}
		"Return itself when sampled" in {
			assertResult(instance)(instance.sample)
		}
		"Have chromosomes entirely containing references to self" in {
			assertResult(IndexedSeq[Chromosome](
					Chromosome(Tid(1,instance), Tid(1,instance)),
					Chromosome(Tid(2,instance), Tid(2,instance)),
					Chromosome(Tid(3,instance), Tid(3,instance))
			))(instance.chromosomes)
		}
		"Have value based hashcode and equals" in {
			val instance1a = RootPlant("Donor1", species1)
			val instance1b = RootPlant("Donor1", species1)
			val instance2 = RootPlant("Donor2", species1)
			val instance3 = RootPlant("Donor3", species2)
			val instance4 = Plant("Offspring", species1.buildChromosomesFrom(instance3), species1)
			
			assertResult(instance1b)(instance1a)
			assert(instance1a != instance2)
			assert(instance1a != instance3)
			assert(instance1a != instance4)
			assertResult(instance1a.hashCode)(instance1b.hashCode)
			assert(instance1a.hashCode != instance2.hashCode)
			assert(instance1a.hashCode != instance3.hashCode)
			assert(instance1a.hashCode != instance4.hashCode)
		}
	}
}