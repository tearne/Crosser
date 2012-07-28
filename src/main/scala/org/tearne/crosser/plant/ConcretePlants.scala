package org.tearne.crosser.plant

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.util.Random

sealed trait ConcretePlant extends Crossable{
	val chromosomes: IndexedSeq[Chromosome]
}

case class RootPlant(val name: String, val species: Species) extends ConcretePlant{
	val chromosomes = species.buildChromosomesFrom(this)
	
	override def toString() = name
	def canEqual(other: Any) = other.isInstanceOf[RootPlant]
	override def equals(other: Any) = {
		other match {
			case that: RootPlant => 
				(that canEqual this) &&
				name == that.name && 	
				species == that.species
		}
	}
}

class PlantException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
	def this(message: String) = this(message, null)
}

case class Plant(val name: String, val chromosomes: IndexedSeq[Chromosome], val species: Species) extends ConcretePlant {
	if(species.cMLengths != chromosomes.map(_.size))
		throw new PlantException("Chromosomes for plant %s do not match species (%s)".format(name, species))
}

class PlantFactory{
	def build(name: String, chromosomes: IndexedSeq[Chromosome], species: Species) = 
		new Plant(name, chromosomes, species)
}