package org.tearne.crosser.cross

object LocusPresence extends Enumeration {
	type LocusPresence = Value
	val AtLeastHeterozygously, Heterozygously, Homozygously, No = Value
}