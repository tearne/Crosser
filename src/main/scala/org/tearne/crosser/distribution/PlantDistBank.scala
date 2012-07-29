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
	val distributionTable = collection.mutable.Map[Cross, PlantDistribution]()
	
	class PlantDistBank(plantDistCrosser: PlantDistCrosser) {
		def get(crossable: Crossable): Samplable = {
			crossable match {
				case cross: Cross => 
					distributionTable.getOrElseUpdate(cross,
						plantDistCrosser.build(
							crossSampler.getDistributionFor(cross.left),
							crossSampler.getDistributionFor(cross.right),
							cross
						)
					)
				case concretePlant: ConcretePlant => concretePlant
			}
		}
	}
}

class PlantDistBankException(msg: String, cause: Throwable) extends RuntimeException(msg, cause){
	def this(msg: String) = this(msg, null)
}