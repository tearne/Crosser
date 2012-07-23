package org.tearne.crosser.cross

import org.tearne.crosser.plant.RootPlant

case class Locus(rootPlant: RootPlant, linkGroup: Int, cM: Int, name: String){
	val linkGroupIndex = linkGroup - 1
	val cMIndex = cM - 1
}