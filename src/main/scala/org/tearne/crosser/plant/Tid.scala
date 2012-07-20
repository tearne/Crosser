package org.tearne.crosser.plant

import org.tearne.crosser.cross.Locus

case class Tid(val alleles: IndexedSeq[RootPlant]){
	def satisfies(locus: Locus): Boolean = {
		false
	}
}
object Tid{
	def apply(length: Int, plant: RootPlant): Tid = {
		Tid((1 to length).map(i => plant))
	}
}