package org.tearne.crosser.plant

import org.mockito.Mockito
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar

class SpeciesTest extends FreeSpec with MockitoSugar{
	"Species should" - {
		"Build a sequence of chromosomes" in {
			val p1 = mock[RootPlant]
			val instance = Species("bert", Vector(1,2,3))
			
			assertResult(IndexedSeq(
					Chromosome(Tid(1,p1), Tid(1,p1)),
					Chromosome(Tid(2,p1), Tid(2,p1)),
					Chromosome(Tid(3,p1), Tid(3,p1))
			))(instance.buildChromosomesFrom(p1))
		}
		"have value based hashcode and equals" in {
			val instance1a = Species("bert", 1,2,3)
			val instance1b = Species("bert", 1,2,3)
			val instance2 = Species("bert", 1,2,2)
			val instance3 = Species("bert!", 1,2,3)
			
			assertResult(instance1a)(instance1b)
			assertResult(instance1a.hashCode)(instance1b.hashCode)
			assert(instance1a != instance2)
			assert(instance1a.hashCode != instance2.hashCode)
			assert(instance1a != instance3)
			assert(instance1a.hashCode != instance3.hashCode)
		}
		"should be buildable from both varargs or vector" in {
			val instance1 = Species("bert", Vector(1,2,3))
			val instance2 = Species("bert", 1,2,3)
			assertResult(instance1)(instance2)
		}
	}
}