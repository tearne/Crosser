package org.tearne.crosser.cross

import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.plant.Species
import org.mockito.Mockito._
import org.mockito.Mockito
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar

class LocusTest extends FreeSpec with MockitoSugar{
	val plant = RootPlant("Plant", mock[Species])
	
	"Locus should" - {
		"Convert linkage group to index" in {
			assertResult(4)(Locus(plant, 5, 10, "locus").linkGroupIndex)
		}
		"Convert cM position to index" in {
			assertResult(9)(Locus(plant, 5, 10, "locus").cMIndex)
		}
		"Have value based hashcode and equals" in {
			val instance1a = Locus(plant, 5, 10, "locus")
			val instance1b = Locus(plant, 5, 10, "locus")
			val instance2 = Locus(mock[RootPlant], 5, 10, "locus")
			val instance3 = Locus(plant, 6, 10, "locus")
			val instance4 = Locus(plant, 6, 11, "locus")
			val instance5 = Locus(plant, 5, 10, "fred")
		
			assertResult(instance1b)(instance1a)
			assert(instance1a != instance2)
			assert(instance1a != instance3)
			assert(instance1a != instance4)
			assert(instance1a != instance5)
			assertResult(instance1b.hashCode)(instance1a.hashCode)
			assert(instance1a.hashCode != instance2.hashCode)
			assert(instance1a.hashCode != instance3.hashCode)
			assert(instance1a.hashCode != instance4.hashCode)
			assert(instance1a.hashCode != instance5.hashCode)
		}
	}
}