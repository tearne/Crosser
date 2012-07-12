package org.tearne.crosser.plant

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito

@RunWith(classOf[JUnitRunner])
class SpeciesSpec extends Specification with Mockito{
	"Species" should {
		"build a sequence of chromosomes" in {
			val p1 = mock[RootPlant]
			val instance = Species("bert", Vector(1,2,3))
			val chromosomes = instance.buildChromosomesFrom(p1)
			chromosomes mustEqual IndexedSeq(
					Chromosome(Tid(1,p1), Tid(1,p1)),
					Chromosome(Tid(2,p1), Tid(2,p1)),
					Chromosome(Tid(3,p1), Tid(3,p1))
			)
		}
		"have value based hashcode and equals" in {
			val instance1a = Species("bert", 1,2,3)
			val instance1b = Species("bert", 1,2,3)
			val instance2 = Species("bert", 1,2,2)
			val instance3 = Species("bert!", 1,2,3)
			
			(instance1a mustEqual instance1b) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a mustNotEqual instance2) and
			(instance1a.hashCode mustNotEqual instance2.hashCode) and
			(instance1a mustNotEqual instance3) and
			(instance1a.hashCode mustNotEqual instance3.hashCode)
		}
		"should be buildable from varargs or vector" in {
			val instance1 = Species("bert", Vector(1,2,3))
			val instance2 = Species("bert", 1,2,3)
			instance1 mustEqual instance2
		}
	}
}