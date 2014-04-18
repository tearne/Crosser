package org.tearne.crosser.cross

import org.junit.runner.RunWith
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Species
import org.tearne.crosser.plant.Plant
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

class CrossTest extends FreeSpec with MockitoSugar{
	"Cross should" - {
		"be crossable" in new Setup{
			assert(
					Cross(leftCrossable, leftCrossable, protocol, "bert").isInstanceOf[Crossable]
			)
		}
		"know it's species from it's parents" in  new Setup{
			val species = mock[Species]
			when(leftCrossable.species).thenReturn(species)
			when(rightCrossable.species).thenReturn(species)
			
			val instance = Cross(leftCrossable, rightCrossable, protocol, "bert")
			
			assertResult(species)(instance.species)
			
		}
		"throw exception if parent species don't agree" in new Setup{
			when(leftCrossable.species).thenReturn(mock[Species])
			when(rightCrossable.species).thenReturn(mock[Species])
			
			intercept[CrossableException]{
				Cross(leftCrossable, rightCrossable, protocol, "bert")
			}
		}
		"have value based hashcode and equals" in new Setup{
			val instance1a = Cross(leftCrossable, rightCrossable, protocol, "bert")
			val instance1b = Cross(leftCrossable, rightCrossable, protocol, "bert")
			val instance2 = Cross(mock[Crossable], rightCrossable, protocol, "bert")
			val instance3 = Cross(leftCrossable, mock[Crossable], protocol, "bert")
			val instance4 = Cross(leftCrossable, rightCrossable, mock[Protocol], "bert")
			val instance5 = Cross(leftCrossable, rightCrossable, protocol, "bert1")
			
			
			assertResult(instance1b)(instance1a)
			assertResult(instance2)(instance1a)
			assertResult(instance3)(instance1a)
			assertResult(instance4)(instance1a)
			assertResult(instance5)(instance1a)
			assertResult(instance1b.hashCode)(instance1a.hashCode)
			assert(instance1a.hashCode != instance2.hashCode)
			assert(instance1a.hashCode != instance3.hashCode)
			assert(instance1a.hashCode != instance4.hashCode)
			assert(instance1a.hashCode != instance5.hashCode)
		}
	}

	trait Setup {
		val leftCrossable = mock[Crossable]
		val rightCrossable = mock[Crossable]
		val protocol = mock[Protocol]		
	}
}