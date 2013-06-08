package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross
import sampler.math.Random
import sampler.data.Samplable
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.distribution.components.PlantDistFactory
import org.tearne.crosser.distribution.components.PlantDistCrosserComponent
import org.tearne.crosser.distribution.components.PlantDistMetricComponent
import org.tearne.crosser.distribution.components.PlantDistBankComponent
import sampler.math.StatisticsComponent

class CrossSamplerService(
		rnd: Random, 
		crosser: Crosser, 
		distFactory: PlantDistFactory, 
		chunkSize: Int, 
		tolerance: Double
	) extends CrossSamplerComponent
		with PlantDistBankComponent
		with PlantDistCrosserComponent
		with PlantDistMetricComponent{
	
	val statistics = StatisticsComponent
	val crossSampler = new CrossSampler(rnd)
	val plantDistCrosser = new PlantDistCrosser(crosser, distFactory, chunkSize, tolerance)
	
	def getDistributionFor(crossable: Crossable): Samplable[ConcretePlant] = 
		crossSampler.getDistributionFor(crossable)
}

trait CrossSamplerComponent{
	//Cake pattern allows immutable mutual dependency 
	// between CrossSampler and PlantDistBank
	this: PlantDistBankComponent =>
	
	val crossSampler: CrossSampler 

	class CrossSampler(rnd: Random) {
		def getDistributionFor(crossable: Crossable): Samplable[ConcretePlant] = 
			plantDistBank.get(crossable)
		
		def sample(crossable: Crossable): ConcretePlant = {
			crossable match{
				case c: ConcretePlant => c
				case cross: Cross => plantDistBank.get(cross).sample
			}
		}
	}
}