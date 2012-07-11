package org.tearne.crosser.plant

case class Tid(val alleles: IndexedSeq[RootPlant])
object Tid{
	def apply(length: Int, plant: RootPlant): Tid = {
		Tid((1 to length).map(i => plant))
	}
}