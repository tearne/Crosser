package org.tearne.crosser.output.composition

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.plant.Tid

class TidCompositionTest extends AssertionsForJUnit with MockitoSugar{
	@Test def proportionsPerIndex = {
		val p1 = mock[RootPlant]
		val p2 = mock[RootPlant]
		val p3 = mock[RootPlant]
		
		val instance = TidComposition(IndexedSeq[Map[RootPlant, Int]](
			Map[RootPlant, Int](p1 -> 3),
			Map[RootPlant, Int](p1 -> 1, p2 -> 2),
			Map[RootPlant, Int](p1 -> 2, p3 -> 1)
		))
		
		assert(instance.proportions(p1) === IndexedSeq(1.0, 1.0 / 3, 2.0 / 3))
		assert(instance.proportions(p2) === IndexedSeq(0.0, 2.0 / 3, 0.0))
		assert(instance.proportions(p3) === IndexedSeq(0.0, 0.0, 1.0 / 3))
	} 

}