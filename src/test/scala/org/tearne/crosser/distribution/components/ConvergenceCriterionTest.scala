package org.tearne.crosser.distribution.components

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.junit.Before
import org.tearne.crosser.distribution.PlantEmpirical

class ConvergenceCriterionTest extends AssertionsForJUnit with MockitoSugar{
	@Test def stopIfSufficientSamplesAndToleranceAcceptable {
		val oldDist = mock[PlantEmpirical]; when(oldDist.numSuccess).thenReturn(199)
		val newDist = mock[PlantEmpirical]; when(newDist.numSuccess).thenReturn(200)

		val metric = mock[Metric]
		when(metric.apply(oldDist,newDist)).thenReturn(100)
		val tolerance = 100
		val fewestPlants = 200
		val instance = new ConvergenceCriterion(metric, tolerance, fewestPlants)
		
		assert(instance.hasConverged(oldDist, newDist) === true)
	}
	
	@Test def dontStopIfTooFewSamples {
		val oldDist = mock[PlantEmpirical]; when(oldDist.numSuccess).thenReturn(99)
		val newDist = mock[PlantEmpirical]; when(newDist.numSuccess).thenReturn(199)

		val metric = mock[Metric]
		when(metric.apply(oldDist,newDist)).thenReturn(99)
		val tolerance = 100
		val fewestPlants = 200
		val instance = new ConvergenceCriterion(metric, tolerance, fewestPlants)
		
		assert(instance.hasConverged(oldDist, newDist) === false)
	}
	
	@Test def dontStopIfDistanceTooLarge {
		val oldDist = mock[PlantEmpirical]; when(oldDist.numSuccess).thenReturn(99)
		val newDist = mock[PlantEmpirical]; when(newDist.numSuccess).thenReturn(199)

		val metric = mock[Metric]
		when(metric.apply(oldDist,newDist)).thenReturn(101)
		val tolerance = 100
		val fewestPlants = 200
		val instance = new ConvergenceCriterion(metric, tolerance, fewestPlants) 
		
		
		assert(instance.hasConverged(oldDist, newDist) === false)
	}
}