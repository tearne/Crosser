package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.util.Random
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.ConcretePlant

trait CrossSamplerService{
	//Cake pattern allows immutable mutual dependency 
	// between CrossSampler and PlantDistBank
	this: PlantDistBankComponent =>
	
	val crossSampler: CrossSampler 

	class CrossSampler(rnd: Random) {
		def sample(crossable: Crossable): ConcretePlant = {
			crossable match{
				case c: ConcretePlant => c
				case _ => plantDistBank.get(crossable).sample(rnd)
			}
		}
	}
}
