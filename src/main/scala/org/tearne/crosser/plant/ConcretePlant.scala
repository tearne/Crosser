package org.tearne.crosser.plant

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.util.AlleleCount

import sampler.data.Distribution

sealed trait ConcretePlant extends Crossable with Distribution[ConcretePlant]{
	val chromosomes: IndexedSeq[Chromosome]
	def alleleCount(donor: RootPlant): AlleleCount = chromosomes.foldLeft(AlleleCount(0,0)){(acc, item) => acc + item.alleleCount(donor)}
}

case class RootPlant(val name: String, val species: Species) extends ConcretePlant{
	val chromosomes = species.buildChromosomesFrom(this)
	
	def sample: ConcretePlant = this
	
	override def toString() = name
	def canEqual(other: Any) = other.isInstanceOf[RootPlant]
	override def equals(other: Any) = {
		other match {
			case that: RootPlant => 
				(that canEqual this) &&
				name == that.name && 	
				species == that.species
			case _ => false
		}
	}
}

class PlantException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
	def this(message: String) = this(message, null)
}

case class Plant(val name: String, val chromosomes: IndexedSeq[Chromosome], val species: Species) extends ConcretePlant {
	if(species.cMLengths != chromosomes.map(_.size))
		throw new PlantException("Chromosomes for plant %s do not match species (%s)".format(name, species))
	
	def sample: ConcretePlant = this
}

class PlantFactory{
	def apply(name: String, chromosomes: IndexedSeq[Chromosome], species: Species) = 
		new Plant(name, chromosomes, species)
}