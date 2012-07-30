package org.tearne.crosser.plant

import org.tearne.crosser.cross.Locus
import org.tearne.crosser.util.AlleleCount

case class Tid(val alleles: IndexedSeq[RootPlant]){
	val size = alleles.size
	
	def satisfies(locus: Locus): Boolean = {
		alleles(locus.cMIndex) == locus.rootPlant
	}
	
	def alleleCount(donor: RootPlant) = {
		AlleleCount(alleles.count(_ == donor), alleles.size)
	}
}
object Tid{
	def apply(length: Int, plant: RootPlant): Tid = {
		Tid((1 to length).map(i => plant))
	}
}

class TidBuilder{
	def apply(alleles: IndexedSeq[RootPlant]) = new Tid(alleles)
}