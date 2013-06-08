package org.tearne.crosser.cross

import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.ConcretePlant

class Crosser(plantFactory: PlantFactory, chromosomeCrosser: ChromosomeCrosser) {
	def apply(left: ConcretePlant, right: ConcretePlant, cross: Cross): Plant = {
		//TODO what about the cross protocol?
		if(left.species != right.species) throw new CrosserException("Parents are of different species")
		val chromosomes = (left.chromosomes zip right.chromosomes).map{case (lch, rch) => chromosomeCrosser(lch, rch)}
		plantFactory(cross.name, chromosomes, cross.species)
	}
}

class CrosserException(msg: String, cause: Throwable) extends RuntimeException(msg, cause){
	def this(msg: String) = this(msg, null)
}