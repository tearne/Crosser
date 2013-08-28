package org.tearne.crosser.output.composition

import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.mock.MockitoSugar
import org.junit.Test
import org.tearne.crosser.plant.Tid
import org.mockito.Mockito._
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.distribution.ChromosomeDistribution
import org.tearne.crosser.distribution.ChromosomeEmpirical

class ChromosomeCompositionBuilderTest 
		extends AssertionsForJUnit 
		with MockitoSugar 
		with TidCompositionBuilderComponent 
		with ChromosomeCompositionBuilderComponent{
	
	val tidCompositionBuilder: TidCompositionBuilder = mock[TidCompositionBuilder]
	val chromosomeCompositionBuilder = new ChromosomeCompositionBuilder
	val instance = chromosomeCompositionBuilder
	
	@Test def accumulateCentimorganContributions {
		val size = 30
		def mockTid = {val tid = mock[Tid]; when(tid.size).thenReturn(size); tid}
		
		val left1 = mockTid
		val left2 = mockTid
		val left3 = mockTid
		val right1 = mockTid
		val right2 = mockTid
		val right3 = mockTid
		
		val leftTidComposition = mock[TidComposition]
		val rightTidComposition = mock[TidComposition]
		
		when(tidCompositionBuilder.apply(Seq(left1, left2, left3))).thenReturn(leftTidComposition)
		when(tidCompositionBuilder.apply(Seq(right1, right2, right3))).thenReturn(rightTidComposition)

		val chromSample1 = Chromosome(left1, right1)
		val chromSample2 = Chromosome(left2, right2)
		val chromSample3 = Chromosome(left3, right3)
		
		val chromDist = ChromosomeEmpirical(IndexedSeq(chromSample1, chromSample2, chromSample3))
		
		val result = instance.apply(chromDist)

		assert(result.left === leftTidComposition)
		assert(result.right === rightTidComposition)
	}
}