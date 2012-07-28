package org.tearne.crosser.distribution

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.util.Random
import org.specs2.specification.Scope
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.cross.Crosser

class PlantDistBuilderSpec extends Specification with Mockito{
	val name = "myCross"
	
	"PlantDistBuilder" should {
		"build distribution using samples from CrossSampler" in new MockCrossSamplerService{
			val leftParentDist = mock[PlantDistribution]
			val rightParentDist = mock[PlantDistribution]
			
			val rnd = mock[Random]
			val chunkSize = 2
			val tolerance = 0.2
			
			val sampleLeft1 = mock[Plant]
			val sampleLeft2 = mock[Plant]
			val sampleLeft3 = mock[Plant]
			val sampleLeft4 = mock[Plant]
			val sampleLeft5 = mock[Plant]
			val sampleLeft6 = mock[Plant]
			leftParentDist.sample(rnd) returns (
				sampleLeft1,
				sampleLeft2, 
				sampleLeft3, 
				sampleLeft4, 
				sampleLeft5, 
				sampleLeft6
			)
			
			val sampleRight1 = mock[Plant]
			val sampleRight2 = mock[Plant]
			val sampleRight3 = mock[Plant]
			val sampleRight4 = mock[Plant]
			val sampleRight5 = mock[Plant]
			val sampleRight6 = mock[Plant]
			rightParentDist.sample(rnd) returns (
				sampleRight1, 
				sampleRight2, 
				sampleRight3, 
				sampleRight4, 
				sampleRight5, 
				sampleRight6
			)
			
			val crosser = mock[Crosser]
			val sampleCross1 = mock[Plant]
			val sampleCross2 = mock[Plant]
			val sampleCross3 = mock[Plant]
			val sampleCross4 = mock[Plant]
			val sampleCross5 = mock[Plant]
			val sampleCross6 = mock[Plant]
			crosser.cross(sampleLeft1, sampleRight1, name) returns sampleCross1
			crosser.cross(sampleLeft2, sampleRight2, name) returns sampleCross2
			crosser.cross(sampleLeft3, sampleRight3, name) returns sampleCross3
			crosser.cross(sampleLeft4, sampleRight4, name) returns sampleCross4
			crosser.cross(sampleLeft5, sampleRight5, name) returns sampleCross5
			crosser.cross(sampleLeft6, sampleRight6, name) returns sampleCross6
			
			val distFactory = mock[PlantDistFactory]
			val dist1 = mock[PlantDistribution]
			val dist2 = mock[PlantDistribution]
			val dist3 = mock[PlantDistribution]
			distFactory.build(Seq(sampleCross1, sampleCross2), 1) returns dist1
			distFactory.augment(dist1, Seq(sampleCross3, sampleCross4), 2) returns dist2
			distFactory.augment(dist2, Seq(sampleCross5, sampleCross6), 3) returns dist3
			
			dist1.distanceTo(dist2) returns 0.9
			dist2.distanceTo(dist3) returns 0.1
			dist3.distanceTo(any) throws new RuntimeException("This shouldn't happen")
			
			val instance = new PlantDistBuilder(crosser, distFactory, rnd, chunkSize, tolerance)
			
			val result = instance.build(leftParentDist, rightParentDist)
			
			(result mustEqual dist3) and (result.failures mustEqual 6)
		}
	}
	trait MockCrossSamplerService extends Scope with
			CrossSamplerService with
			PlantDistBankComponent{
		val crossSampler = mock[CrossSampler]
		val plantDistBank = mock[PlantDistBank]
	}
}