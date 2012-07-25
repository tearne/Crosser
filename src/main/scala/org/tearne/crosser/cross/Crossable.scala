package org.tearne.crosser.cross

import org.tearne.crosser.distribution.ChromosomeBank
import org.tearne.crosser.util.Random
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Species

trait Crossable{
	val species: Species
	def sample(cBank: ChromosomeBank, rnd: Random): ConcretePlant
}

case class Cross(left: Crossable, right: Crossable, protocol: Protocol, name: String) extends Crossable{
	def sample(cBank: ChromosomeBank, rnd: Random): ConcretePlant = cBank.sample(this, rnd)
	val species = {
		if(left.species != right.species) throw new CrossableException("Left and right parents are of different species")
		else left.species
	}
}

class CrossableException(msg: String, cause: Throwable) extends RuntimeException(msg, cause){
	def this(msg: String) = this(msg, null)
}