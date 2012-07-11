package org.tearne.crosser.plant

import org.tearne.crosser.cross.Crossable

trait ConcretePlant extends Crossable{
	val chromosomes: IndexedSeq[Chromosome]
}

case class RootPlant(val name: String, val species: Species) extends ConcretePlant{
	val chromosomes = species.buildChromosomesFrom(this)
}