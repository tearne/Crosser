package org.tearne.crosser.util
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DiscreteSpec extends Specification{
	val once = 1
	val twice = 2
	val thrice = 3
	//	--d1--		--d2--		---d3---
	//					6		    3
	//				  5,6		  2,3
	//	4,5,6		4,5,6		1,2,3,4
	val d1 = new Discrete[Int](IndexedSeq(4, 5, 6))
	val d2 = new Discrete[Int](IndexedSeq(4, 5,5, 6,6,6))
	val d3 = new Discrete[Int](IndexedSeq(1, 2,2, 3,3,3, 4))
	
	"Discrete distributions" should {
		"Add together, summing counts" in { 			
			val dSum: Discrete[Int] = d2 + d3
			
			val resultCounts = dSum.countsMap
			
			(resultCounts.size === 6) and
			(resultCounts(1) === 1) and
			(resultCounts(2) === 2) and
			(resultCounts(3) === 3) and
			(resultCounts(4) === 2) and
			(resultCounts(5) === 2) and
			(resultCounts(6) === 3)
		}
		"Use infinity-norm for distance" in {
			def expectedDist(dA: Discrete[Int], dB: Discrete[Int], position: Int) =
				math.abs(dA(position)-dB(position))
		
			(d1.distanceTo(d2) ===  expectedDist(d1, d2, 6)) and
			(d1.distanceTo(d3) ===  expectedDist(d1, d3, 3)) and
			(d2.distanceTo(d3) ===  expectedDist(d2, d3, 6)) // index 6, not 3
		}
		"Normalise counts" in {
			d1(3) === 0 and
			d1(4) === 1.0/3 and
			d1(5) === 1.0/3 and
			d1(6) === 1.0/3 and
			d1(7) === 0 and
			//
			d2(3) === 0 and
			d2(4) === 1.0/6 and
			d2(5) === 2.0/6 and
			d2(6) === 3.0/6 and
			d2(7) === 0 and
			//
			d3(0) === 0 and
			d3(1) === 1.0/7 and
			d3(2) === 2.0/7 and
			d3(3) === 3.0/7 and
			d3(4) === 1.0/7 and
			d3(5) === 0
		}
		"Override equals and hashcode" in {
			val instance1a = new Discrete[Int](IndexedSeq(4, 5))
			val instance1b = new Discrete[Int](IndexedSeq(4, 5))
			val instance2 = new Discrete[Int](IndexedSeq(4, 5,5))
			
			(instance1a mustEqual instance1b) and
			(instance1a mustNotEqual instance2) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a.hashCode mustNotEqual instance2.hashCode)
		}
	}
}