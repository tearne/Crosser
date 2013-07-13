package org.tearne.crosser.distribution.components

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Crossable
import sampler.data.Samplable
import org.tearne.crosser.distribution.CrossSamplerComponent
import org.slf4j.LoggerFactory

trait PlantDistBankComponent{
	//Cake pattern allows immutable mutual dependency 
	// between CrossSampler and PlantDistBank
	this: CrossSamplerComponent with PlantDistCrosserComponent =>
		
	val log = LoggerFactory.getLogger(getClass().getName())	
		
	val plantDistBank: PlantDistBank = new PlantDistBank
	val distributionTable = collection.mutable.Map[Cross, PlantDistribution]()
	
	class PlantDistBank {
		def get(crossable: Crossable): Samplable[ConcretePlant] = {
			crossable match {
				case cross: Cross => 
					if(!distributionTable.contains(cross)) log.info("Cross {} not cached.  Building...", cross.name)
					else log.info("Cross {} is already cached", cross.name)
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