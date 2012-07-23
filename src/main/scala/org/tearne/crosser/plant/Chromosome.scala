package org.tearne.crosser.plant

import org.tearne.crosser.cross.Locus
import org.tearne.crosser.cross.LocusPresence

class ChromosomeException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
	def this(message: String) { this(message, null) }
}

case class Chromosome(left: Tid, right: Tid) {
	if(left.size != right.size) throw new ChromosomeException("Chromosomes not of same length: %d != %d".format(left.size, right.size))
	
	val size: Int = left.size
	def proportionOf(plant: RootPlant): Double = {
		def numberInTid(t: Tid): Int = {
			t.alleles.foldLeft(0){(acc,a) => acc + (if(a == plant) 1 else 0)}
		}
		println((numberInTid(left) + numberInTid(right)))
		(numberInTid(left) + numberInTid(right))/(2.0 * size)
	}
	
	def satisfies(locus: Locus, shortCircuit: Boolean = false): LocusPresence.Value = {
		val leftResult = left.satisfies(locus)
		val rightResult = right.satisfies(locus)
		
		if(leftResult){
			if(shortCircuit) LocusPresence.AtLeastHeterozygously
			else if(rightResult) LocusPresence.Homozygously
			else LocusPresence.Heterozygously
		}
		else if(rightResult) LocusPresence.Heterozygously
		else LocusPresence.No
	}
}