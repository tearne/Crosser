package org.tearne.crosser.plant

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.util.Random
import org.tearne.crosser.distribution.Samplable

sealed trait ConcretePlant extends Crossable with Samplable{
	val chromosomes: IndexedSeq[Chromosome]
}

case class RootPlant(val name: String, val species: Species) extends ConcretePlant{
	val chromosomes = species.buildChromosomesFrom(this)
	
	def sample(rnd: Random): ConcretePlant = this
	
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
	
	def sample(rnd: Random): ConcretePlant = this
}

class PlantFactory{
	def apply(name: String, chromosomes: IndexedSeq[Chromosome], species: Species) = 
		new Plant(name, chromosomes, species)
}