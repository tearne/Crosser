package org.tearne.crosser.distribution.components

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.cross.Cross
import org.specs2.specification.Scope
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.cross.Crosser
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.cross.Protocol
import org.tearne.crosser.plant.Species
import sampler.math.Random
import sampler.data.Samplable
import org.tearne.crosser.plant.ConcretePlant
import sampler.data.EmpiricalMetricComponent
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.distribution.CrossSamplerComponent

@RunWith(classOf[JUnitRunner])
class PlantDistCrosserSpec extends Specification with Mockito{
	val name = "myCross"
	
	"PlantDistCrosser" should {
		"build distribution using samples from CrossSampler" in new MockCrossSamplerService{
			
			val result = instance.build(leftParentDist, rightParentDist, cross)
			
			result mustEqual dist3
		}
	}
	
	trait MockCrossSamplerService extends Scope
			with CrossSamplerComponent 
			with PlantDistBankComponent 
			with PlantDistCrosserComponent
			with PlantDistMetricComponent
			with EmpiricalMetricComponent{
		val crossSampler = mock[CrossSampler]
		override val plantDistBank = mock[PlantDistBank]
		override val plantDistMetric = mock[PlantDistMetric]
		
		val leftParentDist = mock[Samplable[ConcretePlant]]
		val rightParentDist = mock[Samplable[ConcretePlant]]
		val cross = mock[Cross]
		
		val species = mock[Species]
		val protocol = mock[Protocol]
		cross.name returns name
		cross.protocol returns protocol
		cross.species returns species
		
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
		crosser(sampleLeft1, sampleRight1, cross) returns sampleCross1
		crosser(sampleLeft2, sampleRight2, cross) returns sampleCross2
		crosser(sampleLeft3, sampleRight3, cross) returns sampleCross3
		crosser(sampleLeft4, sampleRight4, cross) returns sampleCross4
		crosser(sampleLeft5, sampleRight5, cross) returns sampleCross5
		crosser(sampleLeft6, sampleRight6, cross) returns sampleCross6
		
		protocol.isSatisfiedBy(sampleCross1) returns false
		protocol.isSatisfiedBy(sampleCross2) returns true
		protocol.isSatisfiedBy(sampleCross3) returns true
		protocol.isSatisfiedBy(sampleCross4) returns true
		protocol.isSatisfiedBy(sampleCross5) returns false
		protocol.isSatisfiedBy(sampleCross6) returns false
		
		val distFactory = mock[PlantDistFactory]
		val dist0 = mock[PlantDistribution]
		val dist1 = mock[PlantDistribution]
		val dist2 = mock[PlantDistribution]
		val dist3 = mock[PlantDistribution]
		distFactory.build(cross) returns dist0
		dist0 ++(Seq(sampleCross1, sampleCross2), 1) returns dist1
		dist1 ++(Seq(sampleCross3, sampleCross4), 0) returns dist2
		dist2 ++(Seq(sampleCross5, sampleCross6), 2) returns dist3
		
		plantDistMetric(dist0, dist1) returns 1.0
		plantDistMetric(dist1, dist2) returns 0.6
		plantDistMetric(dist2, dist3) returns 0.1
		plantDistMetric.apply(org.mockito.Matchers.eq(dist3), any[PlantDistribution]) throws new RuntimeException("This shouldn't happen")
		
		val plantDistCrosser = new PlantDistCrosser(crosser, distFactory, rnd, chunkSize, tolerance)
		
		val instance = plantDistCrosser
	}
	
		
}