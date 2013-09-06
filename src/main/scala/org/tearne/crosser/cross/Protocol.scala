package org.tearne.crosser.cross

import org.tearne.crosser.plant.ConcretePlant

sealed trait Protocol{
	val requiredLoci: Set[Locus]
	def isSatisfiedBy(plant: ConcretePlant): Boolean
}

case class HeterozygousProtocol(val requiredLoci: Set[Locus], val homRequirement: Option[Int] = None) extends Protocol {
	def isSatisfiedBy(plant: ConcretePlant): Boolean = {
		if(homRequirement.isDefined){
			val requirements = requiredLoci.toSeq.map(locus => {
				plant.chromosomes(locus.linkGroupIndex).satisfies(locus)
			})
			
			if(requirements.contains(LocusPresence.AtLeastHeterozygously)) throw new ProtocolException("Non short circuit question should not return short circuit answer")
			
			requirements.find(_ == LocusPresence.No).isEmpty && requirements.count(_ == LocusPresence.Homozygously) >= homRequirement.get
		}else{
			requiredLoci.map(locus => {
				val presence = plant.chromosomes(locus.linkGroupIndex).satisfies(locus, true)
				(presence == LocusPresence.AtLeastHeterozygously) || (presence == LocusPresence.Heterozygously)
			}).find(!_).isEmpty
		}
	}
}
object HeterozygousProtocol{
	def apply(requiredLoci: Set[Locus], homRequirement: Int) = new HeterozygousProtocol(requiredLoci, Some(homRequirement))
}

case class HomozygousProtocol(val requiredLoci: Set[Locus])  extends Protocol{
	def isSatisfiedBy(plant: ConcretePlant): Boolean = {
		val requirements = requiredLoci.toSeq.map(locus => {
			plant.chromosomes(locus.linkGroupIndex).satisfies(locus)
		})
		
		if(requirements.contains(LocusPresence.AtLeastHeterozygously)) throw new ProtocolException("Non short circuit question should not return short circuit answer")
		
		requirements.map(_ == LocusPresence.Homozygously).find(!_).isEmpty
	}
}

class ProtocolException(message: String, cause: Throwable) extends RuntimeException(message, cause){
	def this(message: String) { this(message, null) }
}
