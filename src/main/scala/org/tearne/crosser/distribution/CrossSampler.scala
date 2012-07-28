package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.ConcretePlant

trait CrossSamplerService{
	//Cake pattern allows immutable mutual dependency 
	// between CrossSampler and PlantDistBank
	this: PlantDistBankComponent =>
	
	val crossSampler: CrossSampler 

	class CrossSampler {
		def sample(crossable: Crossable): ConcretePlant = {
			//Will use plantDistBank
			throw new UnsupportedOperationException("TODO")	
		}
	}
}
