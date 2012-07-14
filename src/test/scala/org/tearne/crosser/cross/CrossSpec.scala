package org.tearne.crosser.cross

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._
import org.specs2.mock.Mockito
import org.tearne.crosser.distribution.ChromosomeBank
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.util.Random

@RunWith(classOf[JUnitRunner])
class CrossSpec extends Specification with Mockito{
	val leftCrossable = mock[Crossable]
	val rightCrossable = mock[Crossable]
	val protocol = mock[Protocol]
	
	"Cross" should {
		"be crossable" in {
			Cross(leftCrossable, leftCrossable, protocol, "bert") must
				beAnInstanceOf[Crossable]
		}
		"use chromosome bank for sampling" in {
			val instance = Cross(leftCrossable, rightCrossable, protocol, "bert")
			val sampledPlant = mock[ConcretePlant]
			val chromosomeBank = mock[ChromosomeBank]
			val random = mock[Random]
			chromosomeBank.sample(instance, random) returns sampledPlant
			
			val result:ConcretePlant  = instance.sample(chromosomeBank, random)
			result mustEqual sampledPlant
		}
		"have value based hashcode and equals" in {
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

}