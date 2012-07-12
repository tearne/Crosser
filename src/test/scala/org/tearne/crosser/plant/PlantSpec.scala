package org.tearne.crosser.plant

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.tearne.crosser.cross.Crossable
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.exception.PlantException

@RunWith(classOf[JUnitRunner])
class PlantSpec extends Specification with Mockito{
	val species = Species("foo", 1,2,3)
	val instance = Plant("bar", species.buildChromosomesFrom(mock[RootPlant]), species)
	val otherSpecies = Species("foo2", 1,2,3,4)
	
	"Plant" should {
		"be a concrete plant" in {
			instance must beAnInstanceOf[ConcretePlant]
		}
		"be crossable" in {
			instance must beAnInstanceOf[Crossable]
		}
		"throw exception if chromosome lengths incompatible with species" in {
			Plant("bar", otherSpecies.buildChromosomesFrom(mock[RootPlant]), species) must
				throwA[PlantException]
		}
		"have value based hashcode and equals" in {
			val chromosomes1 = species.buildChromosomesFrom(mock[RootPlant])
			val chromosomes2 = otherSpecies.buildChromosomesFrom(mock[RootPlant])
			
			val instance1a = Plant("bar", chromosomes1, species)
			val instance1b = Plant("bar", chromosomes1, species)
			val instance2 = Plant("bar2", chromosomes1, species)
			val instance3 = Plant("bar", chromosomes2, otherSpecies)
			
			(instance1a mustEqual instance1b) and
			(instance1a mustNotEqual instance2) and
			(instance1a mustNotEqual instance3) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a.hashCode mustNotEqual instance2.hashCode) and
			(instance1a.hashCode mustNotEqual instance3.hashCode)
		}
	}
}