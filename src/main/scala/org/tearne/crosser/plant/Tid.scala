package org.tearne.crosser.plant

import org.tearne.crosser.cross.Locus

case class Tid(val alleles: IndexedSeq[RootPlant]){
	val size = alleles.size
	
	def satisfies(locus: Locus): Boolean = {
		alleles(locus.cMIndex) == locus.rootPlant
	}
}
object Tid{
	def apply(length: Int, plant: RootPlant): Tid = {
		Tid((1 to length).map(i => plant))
	}
}