package org.tearne.crosser.cross

import org.tearne.crosser.plant.ConcretePlant

sealed trait Protocol{
	val requiredLoci: Set[Locus]
	def isSatisfiedBy(plant: ConcretePlant): Boolean
}

case class HeterozygousProtocol(val requiredLoci: Set[Locus], val homRequirement: Option[Int] = None) extends Protocol {
	def isSatisfiedBy(plant: ConcretePlant): Boolean = false
}

object HeterozygousProtocol{
	def apply(requiredLoci: Set[Locus], homRequirement: Int) = new HeterozygousProtocol(requiredLoci, Some(homRequirement))
}
case class HomozygousProtocol(val requiredLoci: Set[Locus])  extends Protocol{
	def isSatisfiedBy(plant: ConcretePlant): Boolean = false
}

class ProtocolException(message: String, cause: Throwable) extends RuntimeException(message, cause){
	def this(message: String) { this(message, null) }
}
