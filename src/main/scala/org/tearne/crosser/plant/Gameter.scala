package org.tearne.crosser.plant

import sampler.math.Random


class Gameter(rnd: Random, recombinationProb: Double) {
	val half = 0.5
	
	def apply(chromosome: Chromosome): Tid = {
		//TODO would this be faster as a private method in the class
		def recombines() = rnd.nextBoolean(recombinationProb)
		
		val leftAlleles = chromosome.left.alleles
		val rightAlleles = chromosome.right.alleles
		
		val booleans = (1 to leftAlleles.size).map(i => recombines).scanLeft(rnd.nextBoolean(half)){case (acc, recombine) => if(recombine) !acc else acc}.tail
		
		val triples = IndexedSeq(booleans, leftAlleles, rightAlleles).transpose
		
		//TODO Would this be faster if done in a mutable way?
		val alleles = triples.map{case IndexedSeq(b, left, right) => 
			if(b.asInstanceOf[Boolean]) left.asInstanceOf[RootPlant] 
			else right.asInstanceOf[RootPlant]
		}
		
		Tid(alleles)
	}
}