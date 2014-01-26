package org.tearne.crosser.distribution.components

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.tearne.crosser.distribution.CrossingService
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import sampler.data.Samplable
import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.distribution.PlantEmpirical
import sampler.data.Distribution

class CrossingServiceTest extends AssertionsForJUnit with MockitoSugar {
	@Test def getSamplableFromCrossable {
		val instance = getCrossingService
		val samplable = mock[Distribution[Plant]]
		val crossable = mock[Crossable]
		
		when(instance.crossDistributions.get(crossable)).thenReturn(samplable)
		assert(instance.getDistribution(crossable) === samplable)
	}
	
	@Test def getSuccessProbabilityFromCross {
		val instance = getCrossingService
		val cross = mock[Cross]
		val empirical = mock[PlantEmpirical]
		val prob = 0.3
		
		when(instance.cache.get(cross)).thenReturn(empirical)
		when(empirical.successProbability).thenReturn(prob)
		
		assert(instance.getSuccessProbability(cross) === prob)
	}
	
	@Test def getPlantDistributionFromCross {
		val instance = getCrossingService
		val cross = mock[Cross]
		val empirical = mock[PlantEmpirical]
		
		when(instance.cache.get(cross)).thenReturn(empirical)
		
		assert(instance.getPlantDistribution(cross) === empirical)
	}
	
	def getCrossingService = new CrossingService {
		val crossDistributions = mock[CrossDistributions]
		val statistics = null
		val cache = mock[Cache]
		val distributionCrosser = null
		val random = null
		val metric = null
	}
}