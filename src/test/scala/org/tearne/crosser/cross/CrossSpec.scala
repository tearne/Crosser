package org.tearne.crosser.cross

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Species
import org.specs2.specification.Scope
import org.tearne.crosser.proto.ChromosomeBank
import org.tearne.crosser.plant.Plant

@RunWith(classOf[JUnitRunner])
class CrossSpec extends Specification with Mockito{
	"Cross" should {
		"be crossable" in new Setup{
			Cross(leftCrossable, leftCrossable, protocol, "bert") must
				beAnInstanceOf[Crossable]
		}
		"know it's species from it's parents" in  new Setup{
			val species = mock[Species]
			leftCrossable.species returns species
			rightCrossable.species returns species
			
			val instance = Cross(leftCrossable, rightCrossable, protocol, "bert")
			
			instance.species mustEqual species
			
		}
		"throw exception if parent species don't agree" in new Setup{
			leftCrossable.species returns mock[Species]
			rightCrossable.species returns mock[Species]
			
			Cross(leftCrossable, rightCrossable, protocol, "bert") must throwA[CrossableException]
		}
		"have value based hashcode and equals" in new Setup{
			val instance1a = Cross(leftCrossable, rightCrossable, protocol, "bert")
			val instance1b = Cross(leftCrossable, rightCrossable, protocol, "bert")
			val instance2 = Cross(mock[Crossable], rightCrossable, protocol, "bert")
			val instance3 = Cross(leftCrossable, mock[Crossable], protocol, "bert")
			val instance4 = Cross(leftCrossable, rightCrossable, mock[Protocol], "bert")
			val instance5 = Cross(leftCrossable, rightCrossable, protocol, "bert1")
			
			
			(instance1a mustEqual instance1b) and
			(instance1a mustNotEqual instance2) and
			(instance1a mustNotEqual instance3) and
			(instance1a mustNotEqual instance4) and
			(instance1a mustNotEqual instance5) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a.hashCode mustNotEqual instance2.hashCode) and
			(instance1a.hashCode mustNotEqual instance3.hashCode) and
			(instance1a.hashCode mustNotEqual instance4.hashCode) and
			(instance1a.hashCode mustNotEqual instance5.hashCode)
		}
	}

	trait Setup extends Scope{
		val leftCrossable = mock[Crossable]
		val rightCrossable = mock[Crossable]
		val protocol = mock[Protocol]		
	}
}