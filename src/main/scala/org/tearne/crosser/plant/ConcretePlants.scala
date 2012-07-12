package org.tearne.crosser.plant

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.exception.PlantException

sealed trait ConcretePlant extends Crossable{
	val chromosomes: IndexedSeq[Chromosome]
}

case class RootPlant(val name: String, val species: Species) extends ConcretePlant{
	val chromosomes = species.buildChromosomesFrom(this)

	override def toString() = name
	def canEqual(other: Any) = other.isInstanceOf[Plant]
	override def equals(other: Any) = {
		other match {
			case that: Plant => 
				(that canEqual this) &&
				super.equals(that) &&
				name == that.name && 	
				species == that.species &&	
				chromosomes == that.chromosomes
		}
	}
}

case class Plant(val name: String, val chromosomes: IndexedSeq[Chromosome], val species: Species) extends ConcretePlant {
	if(species.cMLengths != chromosomes.map(_.size))
		throw new PlantException("Chromosomes for plant %s do not match species (%s)".format(name, species))
}