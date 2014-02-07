package org.tearne.crosser.distribution.components

import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.cross.Protocol
import org.tearne.crosser.distribution.PlantEmpirical
import org.tearne.crosser.distribution.PlantEmpiricalFactory
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.Species

import sampler.data.Distribution
import sampler.math.Random

class DistributionCrosserSpec extends FreeSpec {
	val name = "myCross"
	
	"PlantDistCrosser should" - {
		"build distribution using samples obtained via CrossSamplable" in new MockCrossDistributionsService {
			val result = instance.build(leftParentDist, rightParentDist, cross)
			assertResult(dist3)(result)
		}
		"not crash if no good offsping on first chunk" in pending
		"keep sampling until fewest plants criteria met" in pending
	}
	
	trait MockCrossDistributionsService 
			extends CrossDistributionsComponent 
			with CacheComponent 
			with DistributionCrosserComponent 
			with MockitoSugar{
		
		val statistics = null//mock[StatisticsComponent]
		val crossDistributions = null//mock[CrossSamplable]
		val cache = null//mock[Cache]
		val convergenceCriterion = mock[ConvergenceCriterion]
		
		val leftParentDist = mock[Distribution[ConcretePlant]]
		val rightParentDist = mock[Distribution[ConcretePlant]]
		val cross = mock[Cross]
		
		val species = mock[Species]
		val protocol = mock[Protocol]
		when(cross.name).thenReturn(name)
		when(cross.protocol).thenReturn(protocol)
		when(cross.species).thenReturn(species)
		
		val rnd = mock[Random]
		val chunkSize = 2
		
		val sampleLeft1 = mock[Plant]
		val sampleLeft2 = mock[Plant]
		val sampleLeft3 = mock[Plant]
		val sampleLeft4 = mock[Plant]
		val sampleLeft5 = mock[Plant]
		val sampleLeft6 = mock[Plant]
		when(leftParentDist.sample).thenReturn(
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
		when(rightParentDist.sample).thenReturn(
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
		when(crosser(sampleLeft1, sampleRight1, cross)).thenReturn(sampleCross1)
		when(crosser(sampleLeft2, sampleRight2, cross)).thenReturn(sampleCross2)
		when(crosser(sampleLeft3, sampleRight3, cross)).thenReturn(sampleCross3)
		when(crosser(sampleLeft4, sampleRight4, cross)).thenReturn(sampleCross4)
		when(crosser(sampleLeft5, sampleRight5, cross)).thenReturn(sampleCross5)
		when(crosser(sampleLeft6, sampleRight6, cross)).thenReturn(sampleCross6)
		
		when(protocol.isSatisfiedBy(sampleCross1)).thenReturn(false)
		when(protocol.isSatisfiedBy(sampleCross2)).thenReturn(true)
		when(protocol.isSatisfiedBy(sampleCross3)).thenReturn(true)
		when(protocol.isSatisfiedBy(sampleCross4)).thenReturn(true)
		when(protocol.isSatisfiedBy(sampleCross5)).thenReturn(false)
		when(protocol.isSatisfiedBy(sampleCross6)).thenReturn(false)
		
		val distFactory = mock[PlantEmpiricalFactory]
		val dist0 = mock[PlantEmpirical]
		val dist1 = mock[PlantEmpirical]
		val dist2 = mock[PlantEmpirical]
		val dist3 = mock[PlantEmpirical]
		
		when(distFactory.build(cross)).thenReturn(dist0)
		when(dist0.++(Seq(sampleCross2), 1)).thenReturn(dist1)
		when(dist1.++(Seq(sampleCross3, sampleCross4), 0)).thenReturn(dist2)
		when(dist2.++(Nil, 2)).thenReturn(dist3)
		
		when(convergenceCriterion.hasConverged(dist0, dist1)).thenReturn(false)
		when(convergenceCriterion.hasConverged(dist1, dist2)).thenReturn(false)
		when(convergenceCriterion.hasConverged(dist2, dist3)).thenReturn(true)
		when(convergenceCriterion.hasConverged(org.mockito.Matchers.eq(dist3), any[PlantEmpirical])).thenThrow(new RuntimeException("This shouldn't happen"))
		
		val distributionCrosser = new DistributionCrosser(crosser, distFactory, chunkSize, convergenceCriterion)
		
		val instance = distributionCrosser
	}
}