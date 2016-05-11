package org.tearne.crosser.util

import org.junit.runner.RunWith
import org.scalatest.FreeSpec

class AlleleCountTest extends FreeSpec{
	"AlleleCount should"  - {
		"contain both success and total counts" in {
			val instance = AlleleCount(3,4)
			assertResult(3)(instance.success)
			assertResult(4)(instance.total)
		}
		"be addable" in {
			val instance1 = AlleleCount(3,4)
			val instance2 = AlleleCount(7,96)
			val instanceSum = instance1 + instance2
			
			assertResult(10)(instanceSum.success)
			assertResult(100)(instanceSum.total)
		}
		"calculate proportion" in {
			val instance = AlleleCount(3,4)
			
			assertResult(3.0/4.0)(instance.proportion)
		}
		"have value based hashcode and equals" in {
			val instance1a = AlleleCount(3,4)
			val instance1b = AlleleCount(3,4)
			val instance2 = AlleleCount(3,5)
			val instance3 = AlleleCount(2,4)
			
			assertResult(instance1b)(instance1a) 
			assertResult(instance1b.hashCode)(instance1a.hashCode)
			assert(instance1a != instance2)
			assert(instance1a.hashCode != instance2.hashCode)
			assert(instance1a != instance3)
			assert(instance1a.hashCode != instance3.hashCode)
		}
	}
}