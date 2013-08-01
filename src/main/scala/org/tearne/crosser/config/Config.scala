package org.tearne.crosser.config

import org.tearne.crosser.plant.RootPlant
import scala.collection.immutable.ListMap
import org.tearne.crosser.output.Output
import org.tearne.crosser.cross.Cross

trait Config {
	val chunkSize: Int
	val tolerance: Double
	
	val name: String
	val plants: Map[String, RootPlant]
	val crosses: ListMap[String, Cross]
	
	val outputs: List[Output]
}