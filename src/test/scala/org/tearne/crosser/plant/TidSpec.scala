package org.tearne.crosser.plant

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.tearne.crosser.cross.Locus
import org.specs2.specification.Scope

@RunWith(classOf[JUnitRunner])
class TidSpec extends Specification with Mockito{
	
	
	val linkGroupIrrelevant = 999
	val nameIrrelevant = "whatever"
	
	"Tid" should {
		"store alleles" in new Setup {
			val testAlleles = IndexedSeq(p1, p1, p1, p2, p3)
			val instance = Tid(testAlleles)
			
			instance.alleles must_== testAlleles
		}
		"have companion to build tid for root plants" in new Setup{
			val instance = Tid(5, p1)
			
			instance.alleles must beEqualTo(1 to 5 map(i => p1))
		}
		"have value based hashcode and equals" in new Setup {
			val instance1a = Tid(IndexedSeq(p1, p2, p3))
			val instance1b = Tid(IndexedSeq(p1, p2, p3))
			val instance2 = Tid(IndexedSeq(p1, p1, p3))
			
			(instance1a mustEqual instance1b) and 
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a mustNotEqual instance2) and
			(instance1a.hashCode mustNotEqual instance2.hashCode)
		}
		"satisfies locus" in new Setup {
			val instance = Tid(IndexedSeq(p1, p2, p3))
			val locus = Locus(p2, linkGroupIrrelevant, 2, nameIrrelevant)
			
			instance.satisfies(locus) mustEqual true
		}
		"doesn't satisfy locus" in new Setup {
			val instance = Tid(IndexedSeq(p1, p1, p3))
			val locus = Locus(p2, linkGroupIrrelevant, 2, nameIrrelevant)
			
			instance.satisfies(locus) mustEqual false
		}
	}
	
	trait Setup extends Scope {
		val p1 = mock[RootPlant]
		val p2 = mock[RootPlant]
		val p3 = mock[RootPlant]
	}
}