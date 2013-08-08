package org.tearne.crosser.output.composition

import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.plant.Tid
import org.junit.Before

class TidCompositionBuilderTest extends AssertionsForJUnit 
		with MockitoSugar
		with TidCompositionBuilderComponent {
	
	val tidCompositionBuilder = new TidCompositionBuilder
	val instance = tidCompositionBuilder
	
	@Test def buildFromSeqOfTids {
		val p1 = mock[RootPlant]
		val p2 = mock[RootPlant]
		val p3 = mock[RootPlant]
		
		val tid1 = Tid(IndexedSeq(p1, p1, p1))
		val tid2 = Tid(IndexedSeq(p1, p2, p1))
		val tid3 = Tid(IndexedSeq(p1, p2, p3))
		
		val result = instance(Seq(tid1, tid2, tid3))
		
		val expected = TidComposition(IndexedSeq[Map[RootPlant, Int]](
			Map[RootPlant, Int](p1 -> 3),
			Map[RootPlant, Int](p1 -> 1, p2 -> 2),
			Map[RootPlant, Int](p1 -> 2, p3 -> 1)
		))
		
		assert(result === expected)
	}
	
	@Test def exceptionIfTidsInconsistentSizes {
		val p1 = mock[RootPlant]
		val tid1 = Tid(IndexedSeq(p1, p1, p1))
		val tid2 = Tid(IndexedSeq(p1, p1))
		
		intercept[AssertionError]{ instance(Seq(tid1, tid2)) }
	}

}