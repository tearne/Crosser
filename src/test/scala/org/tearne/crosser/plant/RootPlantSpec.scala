package org.tearne.crosser.plant

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.cross.Crossable

@RunWith(classOf[JUnitRunner])
class RootPlantSpec extends Specification{
	val instance = RootPlant("Donor", Species(1,2,3))
	
	"RootPlant" should {
		"be a concrete plant" in {
			instance must beAnInstanceOf[ConcretePlant]
		}
		"be crossable" in {
			instance must beAnInstanceOf[Crossable]
		}
		"have chromosomes entirely containing references to self" in {
			instance.chromosomes must beEqualTo(IndexedSeq[Chromosome](
					Chromosome(Tid(1,instance), Tid(1,instance)),
					Chromosome(Tid(2,instance), Tid(2,instance)),
					Chromosome(Tid(3,instance), Tid(3,instance))
			))
		}
		"have value based hashcode and equals" in {
			val instance1a = RootPlant("Donor", Species(1,2,3))
			val instance1b = RootPlant("Donor", Species(1,2,3))
			val instance2 = RootPlant("Donor2", Species(1,2,3))
			val instance3 = RootPlant("Donor3", Species(1,2,3))
			
			(instance1a mustEqual instance1b) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a mustNotEqual instance2) and
			(instance1a mustNotEqual instance3) and
			(instance1a.hashCode mustNotEqual instance2.hashCode) and
			(instance1a.hashCode mustNotEqual instance3.hashCode)
		}
	}
}