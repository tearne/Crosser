package org.tearne.crosser.distribution

import org.junit.runner.RunWith
import org.junit.runner.RunWith
import org.tearne.crosser.plant.Species
import org.tearne.crosser.cross.Cross
import org.scalatest.mock.MockitoSugar
import org.scalatest.FreeSpec
import org.mockito.Mockito._

class PlantEmpiricalFactoryTest extends FreeSpec with MockitoSugar{
	val threeFailures = 3
	val name = "myCross"
	
	"PlantEmpiricalFactory should" - {
		"create new plant distributions" in {
			val instance = new PlantEmpiricalFactory()
			val species = Species("name", IndexedSeq(1,1,1))
			val cross = mock[Cross]
			when(cross.name) thenReturn name
			when(cross.species) thenReturn species
			
			//TODO improve equality checks with Discrete
			val result = instance.build(cross)
			assertResult(Seq(
				ChromosomeDistribution.empty,
				ChromosomeDistribution.empty,
				ChromosomeDistribution.empty
			))(result.chromoDistSeq)
			assertResult(0)(result.numFailures)
			assertResult(name)(result.name)
		}
	}
}