package org.tearne.crosser.scheme

import org.tearne.crosser.plant.RootPlant
import scala.collection.immutable.ListMap
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.cross.Crossable

trait Scheme {
	val chunkSize: Int
	val recombinationProb: Double
	val tolerance: Double
	
	val name: String
	
	val plants: Map[String, RootPlant]
	val crosses: ListMap[String, Cross]
	
	val outputTables: List[(Crossable, Crossable)]
}