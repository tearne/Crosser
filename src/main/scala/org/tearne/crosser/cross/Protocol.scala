package org.tearne.crosser.cross

sealed trait Protocol{
	val requiredTraits: Set[Locus]
}

case class HeterozygousProtocol(val requiredTraits: Set[Locus], val homRequirement: Option[Int] = None) extends Protocol
case class HomozygousProtocol(val requiredTraits: Set[Locus])  extends Protocol
