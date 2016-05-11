package org.tearne.crosser.plant

import org.tearne.crosser.cross.Locus
import org.tearne.crosser.cross.LocusPresence._
import org.tearne.crosser.util.AlleleCount
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

class ChromosomeTest extends FreeSpec with MockitoSugar {
	val p1 = mock[RootPlant]
	val p2 = mock[RootPlant]
	val leftTid = Tid(IndexedSeq(p1, p1, p1, p2))
	val rightTid = Tid(IndexedSeq(p1, p1, p2, p2))
	val instance = Chromosome(leftTid, rightTid)
	
	def makeMockTid(length: Int) = {val t = mock[Tid]; when(t.size) thenReturn length; t}
	
	"Chromosome should" - {
		"give a count of donor alleles present" in {
			val rootPlant = mock[RootPlant]
			val leftTid = mock[Tid]
			val rightTid = mock[Tid]
			
			val alleleCount1 = mock[AlleleCount]
			val alleleCount2 = mock[AlleleCount]
			val alleleCount3 = mock[AlleleCount]
			when(alleleCount1 + alleleCount2) thenReturn alleleCount3
			
			when(leftTid.alleleCount(rootPlant)) thenReturn alleleCount1
			when(rightTid.alleleCount(rootPlant)) thenReturn alleleCount2
			
			assertResult(alleleCount3){
				Chromosome(leftTid, rightTid).alleleCount(rootPlant)
			}
		}
		"throw exception if left and right tid differ in length" in {
			val leftTid = Tid(5, mock[RootPlant])
			val rightTid = Tid(4, mock[RootPlant])
			
			intercept[ChromosomeException]{
				Chromosome(leftTid, rightTid)
			}
		}
		"have value based hashcode and equals" in {
			val instance2 = Chromosome(leftTid, rightTid)
			val instance3 = Chromosome(rightTid, leftTid)
			
			assertResult(instance2)(instance)
			assertResult(instance.hashCode)(instance2.hashCode)
			assert(instance != instance3)
			assert(instance.hashCode != instance3.hashCode)
		}
		"report it's size" in {
			assertResult(4)(instance.size)
		}
		"confirm loci heterozygous" in {
			val leftTid = makeMockTid(5)
			val rightTid = makeMockTid(5)
			
			val locus = mock[Locus]
			when(leftTid.satisfies(locus)) thenReturn true
			when(rightTid.satisfies(locus)) thenReturn false
			
			val instance = Chromosome(rightTid, leftTid)
			assertResult(Heterozygously)(instance.satisfies(locus))
		}
		"confirm loci at least heterozugous even if might be hom when using short circuit" in {
			val leftTid = makeMockTid(5)
			val rightTid = makeMockTid(5)
			
			val locus = mock[Locus]
			
			when(leftTid.satisfies(locus)) thenReturn true
			
			val instance = Chromosome(leftTid,rightTid)
			
			verify(rightTid.satisfies(locus), never)
			assertResult(AtLeastHeterozygously)(instance.satisfies(locus, true))
		}
		"confirm loci homozygous" in {
			val leftTid = makeMockTid(5)
			val rightTid = makeMockTid(5)
			
			val locus = mock[Locus]
			
			when(leftTid.satisfies(locus)) thenReturn true
			when(rightTid.satisfies(locus)) thenReturn true
			
			val instance = Chromosome(rightTid, leftTid)
			assertResult(Homozygously)(instance.satisfies(locus))
		}
		"confirm it doesn't satisfy loci" in{
			val leftTid = makeMockTid(5)
			val rightTid = makeMockTid(5)
			
			val locus = mock[Locus]
			
			when(leftTid.satisfies(locus)) thenReturn false
			when(rightTid.satisfies(locus)) thenReturn false
			
			val instance = Chromosome(rightTid, leftTid)
			assertResult(No)(instance.satisfies(locus))
		}
		"confirm it doesn't satisfy loci with short circuit" in{
			val leftTid = makeMockTid(5)
			val rightTid = makeMockTid(5)
			
			val locus = mock[Locus]
			
			when(leftTid.satisfies(locus)) thenReturn false
			when(rightTid.satisfies(locus)) thenReturn false
			
			val instance = Chromosome(rightTid, leftTid)
			assertResult(No)(instance.satisfies(locus, true))
		}
	}
}