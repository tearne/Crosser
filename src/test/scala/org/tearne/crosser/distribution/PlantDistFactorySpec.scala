package org.tearne.crosser.distribution

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.util.Discrete
import org.tearne.crosser.plant.Species
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.cross.Cross

@RunWith(classOf[JUnitRunner])
class PlantDistFactorySpec extends Specification with Mockito{
	val threeFailures = 3
	val name = "myCross"
	
	"PlantDistFactory" should {
		"create new plant distributions" in {
			val instance = new PlantDistFactory()
			val species = Species("name", IndexedSeq(1,1,1))
			val cross = mock[Cross]
			cross.name returns name
			cross.species returns species
			
			//TODO improve equality checks with Discrete
			val result = instance.build(cross)
			(result.chromoDists mustEqual Seq(
				Discrete[Chromosome](),
				Discrete[Chromosome](),
				Discrete[Chromosome]()
			)) and
			(result.failures mustEqual 0) and
			(result.name mustEqual name)
		}
	}
}