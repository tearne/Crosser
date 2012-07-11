package org.tearne.crosser.plant

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.exception.ChromosomeException

@RunWith(classOf[JUnitRunner])
class ChromosomeSpec extends Specification with Mockito {
	val p1 = mock[RootPlant]
	val p2 = mock[RootPlant]
	val leftTid = Tid(IndexedSeq(p1, p1, p1, p2))
	val rightTid = Tid(IndexedSeq(p1, p1, p2, p2))
	val instance = Chromosome(leftTid, rightTid)
	
	"Chromosome" should {
		"throw exception if left and right tid differ in length" in {
			val leftTid = Tid(5, mock[RootPlant])
			val rightTid = Tid(4, mock[RootPlant])
			
			Chromosome(leftTid, rightTid) must throwA[ChromosomeException]
		}
		"give the proportion of a plant present" in {
			Chromosome(leftTid, rightTid).proportionOf(p1) must beEqualTo(5.0/8)
		}
		"have value based hashcode and equals" in{
			val instance2 = Chromosome(leftTid, rightTid)
			val instance3 = Chromosome(rightTid, leftTid)
			
			(instance mustEqual instance2) and
			(instance.hashCode mustEqual instance2.hashCode) and
			(instance mustNotEqual instance3) and
			(instance.hashCode mustNotEqual instance3.hashCode)
		}
		"report it's size" in {
			instance.size mustEqual 4
		}
	}
}