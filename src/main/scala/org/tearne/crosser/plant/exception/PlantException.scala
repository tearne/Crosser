package org.tearne.crosser.plant.exception

class PlantException(message: String, cause: Throwable) extends RuntimeException(message, cause) {

	def this(message: String) = this(message, null)
}