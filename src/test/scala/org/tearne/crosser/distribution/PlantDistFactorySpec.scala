package org.tearne.crosser.distribution

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.util.Discrete
import org.tearne.crosser.plant.Species

class PlantDistFactorySpec extends Specification with Mockito{
	val threeFailures = 3
	
	"PlantDistFactory" should {
		"create new plant distributions" in {
			val c1_1 = mock[Chromosome]; val c1_2 = mock[Chromosome]; val c1_3 = mock[Chromosome]
			val p1 = mock[Plant]; p1.chromosomes returns IndexedSeq(c1_1, c1_2, c1_3)
			val c2_1 = mock[Chromosome]; val c2_2 = mock[Chromosome]; val c2_3 = mock[Chromosome]
			val p2 = mock[Plant]; p2.chromosomes returns IndexedSeq(c2_1, c2_2, c2_3)
			val c3_1 = mock[Chromosome]; val c3_2 = mock[Chromosome]; val c3_3 = mock[Chromosome]
			val p3 = mock[Plant]; p3.chromosomes returns IndexedSeq(c3_1, c3_2, c3_3)
			
			val instance = new PlantDistFactory()
			
			//TODO improve equality checks with Discrete
			val result = instance.build(Seq(p1,p2,p3), threeFailures)
			(result.chromoDists mustEqual Seq(
				Discrete(c1_1, c2_1, c3_1),
				Discrete(c1_2, c2_2, c3_2),
				Discrete(c1_3, c2_3, c3_3)
			)) and
			(result.failures mustEqual threeFailures)
		}
		"append to existing distributions" in {
			
			val p1 = mock[Plant]
			val p2 = mock[Plant]
			val p3 = mock[Plant]
			
			
			val existingDist = mock[PlantDistribution]
			val newDist = mock[PlantDistribution]
			existingDist.++(Seq(p1,p2,p3), threeFailures) returns newDist
			
			val instance = new PlantDistFactory()

			instance.augment(existingDist, Seq(p1,p2,p3), threeFailures) mustEqual newDist
		}
	}

}