package org.tearne.crosser.cross

import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.util.Probability
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.ConcretePlant

class Crosser(plantFactory: PlantFactory, chromosomeCrosser: ChromosomeCrosser) {
	def cross(left: ConcretePlant, right: ConcretePlant, name: String): Plant = null
}

class CrosserException(msg: String, cause: Throwable) extends RuntimeException(msg, cause){
	def this(msg: String) = this(msg, null)
}