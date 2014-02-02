package org.tearne.crosser.output.composition

import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.plant.Tid
import org.scalatest.FreeSpec

class TidCompositionTest extends FreeSpec with MockitoSugar{
	"calculate proportions at each cM" in {
		val p1 = mock[RootPlant]
		val p2 = mock[RootPlant]
		val p3 = mock[RootPlant]
		
		val instance = TidComposition.apply(IndexedSeq[Map[RootPlant, Int]](
			Map[RootPlant, Int](p1 -> 3),
			Map[RootPlant, Int](p1 -> 1, p2 -> 2),
			Map[RootPlant, Int](p1 -> 2, p3 -> 1)
		))
		
		assertResult(Map(p1 -> 1.0)){instance.proportions(0)}
		assertResult(Map(p1 -> 1.0/3, p2 -> 2.0/3)){instance.proportions(1)}
		assertResult(Map(p1 -> 2.0/3, p3 -> 1.0/3)){instance.proportions(2)}
	} 
}