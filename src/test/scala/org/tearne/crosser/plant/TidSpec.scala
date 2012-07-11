package org.tearne.crosser.plant

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito

@RunWith(classOf[JUnitRunner])
class TidSpec extends Specification with Mockito{
	val p1 = mock[RootPlant]
	val p2 = mock[RootPlant]
	val p3 = mock[RootPlant]
	
	"Tid" should {
		"store alleles" in {
			val testAlleles = IndexedSeq(p1, p1, p1, p2, p3)
			val instance = Tid(testAlleles)
			
			instance.alleles must_== testAlleles
			
		}
		"have companion to build tid for root plants" in {
			val instance = Tid(5, p1)
			
			instance.alleles must beEqualTo(1 to 5 map(i => p1))
		}
		"have value based hashcode and equals" in {
			val instance1a = Tid(IndexedSeq(p1, p2, p3))
			val instance1b = Tid(IndexedSeq(p1, p2, p3))
			val instance2 = Tid(IndexedSeq(p1, p1, p3))
			
			(instance1a mustEqual instance1b) and 
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a mustNotEqual instance2) and
			(instance1a.hashCode mustNotEqual instance2.hashCode)
		}
	}
}