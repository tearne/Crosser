package org.tearne.crosser.plant

import org.tearne.crosser.cross.Locus
import org.tearne.crosser.util.AlleleCount
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar

class TidTest extends FreeSpec with MockitoSugar{
	
	val linkGroupIrrelevant = 999
	val nameIrrelevant = "whatever"
	
	"Tid should" - {
		"give a count of donor alleles present" in new Setup {
			val testAlleles = IndexedSeq(p1, p1, p1, p2, p3)
			val instance = Tid(testAlleles)
			
			assertResult(AlleleCount(3,5))(instance.alleleCount(p1))
		}
		"store alleles" in new Setup {
			val testAlleles = IndexedSeq(p1, p1, p1, p2, p3)
			val instance = Tid(testAlleles)
			
			assertResult(testAlleles)(instance.alleles)
		}
		"have easy access to alleles size" in new Setup{
			val testAlleles = IndexedSeq(p1, p1, p1, p2, p3)
			assertResult(5)(Tid(testAlleles).size)
		}
		"have companion to build tid for root plants" in new Setup{
			val instance = Tid(5, p1)
			
			assertResult(1 to 5 map(i => p1))(instance.alleles)
		}
		"have value based hashcode and equals" in new Setup {
			val instance1a = Tid(IndexedSeq(p1, p2, p3))
			val instance1b = Tid(IndexedSeq(p1, p2, p3))
			val instance2 = Tid(IndexedSeq(p1, p1, p3))
			
			assertResult(instance1b)(instance1a) 
			assertResult(instance1a.hashCode)(instance1b.hashCode)
			assert(instance1a != instance2)
			assert(instance1a.hashCode != instance2.hashCode)
		}
		"satisfies locus" in new Setup {
			val instance = Tid(IndexedSeq(p1, p2, p3))
			val locus = Locus(p2, linkGroupIrrelevant, 2, nameIrrelevant)
			
			assert(instance.satisfies(locus))
		}
		"doesn't satisfy locus" in new Setup {
			val instance = Tid(IndexedSeq(p1, p1, p3))
			val locus = Locus(p2, linkGroupIrrelevant, 2, nameIrrelevant)
			
			assert(!instance.satisfies(locus))
		}
	}
	
	trait Setup {
		val p1 = mock[RootPlant]
		val p2 = mock[RootPlant]
		val p3 = mock[RootPlant]
	}
}