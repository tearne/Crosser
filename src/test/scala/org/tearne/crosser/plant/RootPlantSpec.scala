package org.tearne.crosser.plant

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.cross.Crossable
import org.specs2.mock.Mockito
import org.tearne.crosser.distribution.ChromosomeBank
import org.tearne.crosser.util.Random

@RunWith(classOf[JUnitRunner])
class RootPlantSpec extends Specification with Mockito{
	val species1 = Species("species1", 1,2,3)
	val species2 = Species("species2", 1,2,3)
	val instance = RootPlant("Donor", species1)
	
	"RootPlant" should {
		"be a concrete plant" in {
			instance must beAnInstanceOf[ConcretePlant]
		}
		"be crossable" in {
			instance must beAnInstanceOf[Crossable]
		}
		"return itself when sampled" in {
			val random = mock[Random]
			instance.sample(null, random) mustEqual instance
		}
		"have chromosomes entirely containing references to self" in {
			instance.chromosomes must beEqualTo(IndexedSeq[Chromosome](
					Chromosome(Tid(1,instance), Tid(1,instance)),
					Chromosome(Tid(2,instance), Tid(2,instance)),
					Chromosome(Tid(3,instance), Tid(3,instance))
			))
		}
		"have value based hashcode and equals" in {
			val instance1a = RootPlant("Donor1", species1)
			val instance1b = RootPlant("Donor1", species1)
			val instance2 = RootPlant("Donor2", species1)
			val instance3 = RootPlant("Donor3", species2)
			val instance4 = Plant("Offspring", species1.buildChromosomesFrom(instance3), species1)
			
			(instance1a mustEqual instance1b) and
			(instance1a mustNotEqual instance2) and
			(instance1a mustNotEqual instance3) and
			(instance1a mustNotEqual instance4) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a.hashCode mustNotEqual instance2.hashCode) and
			(instance1a.hashCode mustNotEqual instance3.hashCode) and
			(instance1a.hashCode mustNotEqual instance4.hashCode)
		}
	}
}