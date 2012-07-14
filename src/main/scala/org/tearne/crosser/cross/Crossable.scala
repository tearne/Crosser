package org.tearne.crosser.cross

import org.tearne.crosser.distribution.ChromosomeBank
import org.tearne.crosser.util.Random
import org.tearne.crosser.plant.ConcretePlant

trait Crossable{
	def sample(cBank: ChromosomeBank, rnd: Random): ConcretePlant
}

case class Cross(left: Crossable, right: Crossable, protocol: Protocol, name: String) extends Crossable{
	def sample(cBank: ChromosomeBank, rnd: Random): ConcretePlant = cBank.sample(this, rnd)
}