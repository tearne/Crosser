package org.tearne.crosser.util

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification

@RunWith(classOf[JUnitRunner])
class AlleleCountSpec extends Specification{
	"AlleleCount" should {
		"contain both success and total counts" in {
			val instance = AlleleCount(3,4)
			(instance.success mustEqual 3) and
			(instance.total mustEqual 4)
		}
		"be addable" in {
			val instance1 = AlleleCount(3,4)
			val instance2 = AlleleCount(7,96)
			val instanceSum = instance1 + instance2
			
			(instanceSum.success mustEqual 10) and
			(instanceSum.total mustEqual 100)
		}
		"calculate proportion" in {
			val instance = AlleleCount(3,4)
			
			instance.proportion mustEqual 3.0/4.0
		}
		"have value based hashcode and equals" in {
			val instance1a = AlleleCount(3,4)
			val instance1b = AlleleCount(3,4)
			val instance2 = AlleleCount(3,5)
			val instance3 = AlleleCount(2,4)
			
			(instance1a mustEqual instance1b) and 
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a mustNotEqual instance2) and
			(instance1a.hashCode mustNotEqual instance2.hashCode) and
			(instance1a mustNotEqual instance3) and
			(instance1a.hashCode mustNotEqual instance3.hashCode)
		}
	}
}