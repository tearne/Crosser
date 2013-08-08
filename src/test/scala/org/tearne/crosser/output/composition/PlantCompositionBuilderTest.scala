package org.tearne.crosser.output.composition

import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.mock.MockitoSugar
import org.junit.Test
import org.mockito.Mockito._
import org.tearne.crosser.distribution.PlantDistribution
import org.tearne.crosser.distribution.ChromosomeDistribution
import org.tearne.crosser.plant.Chromosome

class PlantContributionBuilderTest 
		extends AssertionsForJUnit 
		with MockitoSugar 
		with PlantCompositionBuilderComponent
		with ChromosomeCompositionBuilderComponent
		with TidCompositionBuilderComponent {
	
	val tidCompositionBuilder = null
	val chromosomeCompositionBuilder = mock[ChromosomeCompositionBuilder]
	val plantCompositionBuilder = new PlantCompositionBuilder
	val instance = plantCompositionBuilder
	

	@Test def buildFromPlantDistribution  = {
		val pDist = mock[PlantDistribution]
		val c1Dist = mock[ChromosomeDistribution]
		val c2Dist = mock[ChromosomeDistribution]
		val c3Dist = mock[ChromosomeDistribution]
		val cDists = IndexedSeq(c1Dist, c2Dist, c3Dist)
		when(pDist.chromoDists).thenReturn(cDists)
		
		val chromosome1Composition = mock[ChromosomeComposition]
		val chromosome2Composition = mock[ChromosomeComposition]
		val chromosome3Composition = mock[ChromosomeComposition]
		
		when(chromosomeCompositionBuilder.apply(c1Dist)).thenReturn(chromosome1Composition)
		when(chromosomeCompositionBuilder.apply(c2Dist)).thenReturn(chromosome2Composition)
		when(chromosomeCompositionBuilder.apply(c3Dist)).thenReturn(chromosome3Composition)
		
		assert(
			instance(pDist) === PlantComposition(IndexedSeq(chromosome1Composition, chromosome2Composition, chromosome3Composition))
		)
	}
}