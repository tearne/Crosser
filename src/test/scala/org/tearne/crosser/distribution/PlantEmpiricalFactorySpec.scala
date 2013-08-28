package org.tearne.crosser.distribution

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.plant.Species
import org.tearne.crosser.cross.Cross

@RunWith(classOf[JUnitRunner])
class PlantEmpiricalFactorySpec extends Specification with Mockito{
	val threeFailures = 3
	val name = "myCross"
	
	"PlantEmpiricalFactory" should {
		"create new plant distributions" in {
			val instance = new PlantEmpiricalFactory()
			val species = Species("name", IndexedSeq(1,1,1))
			val cross = mock[Cross]
			cross.name returns name
			cross.species returns species
			
			//TODO improve equality checks with Discrete
			val result = instance.build(cross)
			(result.chromoDistSeq mustEqual Seq(
				ChromosomeDistribution.empty,
				ChromosomeDistribution.empty,
				ChromosomeDistribution.empty
			)) and
			(result.numFailures mustEqual 0) and
			(result.name mustEqual name)
		}
	}
}