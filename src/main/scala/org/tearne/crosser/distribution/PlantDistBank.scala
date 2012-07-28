package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.util.Random
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.cross.Crossable

trait PlantDistBankComponent{
	//Cake pattern allows immutable mutual dependency 
	// between CrossSampler and PlantDistBank
	this: CrossSamplerService =>
		
	val plantDistBank: PlantDistBank
	
	class PlantDistBank(plantDistBuilder: PlantDistBuilder) {
		def get(cross: Crossable): PlantDistribution = {
			//uses plant dist builder
			throw new UnsupportedOperationException("todo")
		}
	}
}

class PlantDistBankException(msg: String, cause: Throwable) extends RuntimeException(msg, cause){
	def this(msg: String) = this(msg, null)
}