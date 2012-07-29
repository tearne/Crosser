package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.util.Random
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross

trait CrossSamplerService{
	//Cake pattern allows immutable mutual dependency 
	// between CrossSampler and PlantDistBank
	this: PlantDistBankComponent =>
	
	val crossSampler: CrossSampler 

	class CrossSampler(rnd: Random) {
		def getDistributionFor(crossable: Crossable): Samplable = 
			plantDistBank.get(crossable)
		
		def sample(crossable: Crossable): ConcretePlant = {
			crossable match{
				case c: ConcretePlant => c
				case cross: Cross => plantDistBank.get(cross).sample(rnd)
			}
		}
	}
}
