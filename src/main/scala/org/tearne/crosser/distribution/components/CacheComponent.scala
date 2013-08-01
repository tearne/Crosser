package org.tearne.crosser.distribution.components

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Crossable
import sampler.data.Samplable
import org.slf4j.LoggerFactory
import org.tearne.crosser.distribution.PlantDistribution

/**
 * Caches plant distributions
 */
trait CacheComponent{
	//Cake pattern allows immutable mutual dependency 
	// between CrossSampler and PlantDistBank
	this: CrossSamplableComponent with DistributionCrosserComponent =>
		
	val log = LoggerFactory.getLogger(getClass().getName())	
		
	val cache: Cache
	
	class Cache {
		//TODO TestMe
		val distributionTable = collection.mutable.Map[Cross, PlantDistribution]()
		def get(cross: Cross): PlantDistribution = {
			if(!distributionTable.contains(cross)) log.trace("Cross {} not cached.  Will be built.", cross.name)
			else log.trace("Cross {} is already cached", cross.name)
			
			distributionTable.getOrElseUpdate(cross,
				distributionCrosser.build(
					crossSamplable.get(cross.left),
					crossSamplable.get(cross.right),
					cross
				)
			)
		}
	}
}

class PlantDistBankException(msg: String, cause: Throwable) extends RuntimeException(msg, cause){
	def this(msg: String) = this(msg, null)
}