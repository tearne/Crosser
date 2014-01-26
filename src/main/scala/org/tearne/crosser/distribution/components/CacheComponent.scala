package org.tearne.crosser.distribution.components

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Crossable
import sampler.data.Samplable
import org.slf4j.LoggerFactory
import org.tearne.crosser.distribution.PlantEmpirical

/**
 * Caches plant distributions
 */
trait CacheComponent{
	//Cake pattern allows immutable mutual dependency 
	// between CrossSampler and PlantDistBank
	this: CrossDistributionsComponent with DistributionCrosserComponent =>
		
	val log = LoggerFactory.getLogger(getClass().getName())	
		
	val cache: Cache
	
	class Cache {
		val log = LoggerFactory.getLogger(this.getClass())
		val distributionTable = collection.mutable.Map[Cross, PlantEmpirical]()
		
		def get(cross: Cross): PlantEmpirical = { 
			if(!distributionTable.contains(cross))
				log.trace("Cross {} not cached.  Will be built.", cross.name)
			else 
				log.trace("Cross {} is already cached", cross.name)
			
			def build(cross: Cross): PlantEmpirical = 
				distributionCrosser.build(
					crossDistributions.get(cross.left),
					crossDistributions.get(cross.right),
					cross
				)
				
			distributionTable.getOrElseUpdate(cross, build(cross))
		}
	}
}

class PlantDistBankException(msg: String, cause: Throwable) extends RuntimeException(msg, cause){
	def this(msg: String) = this(msg, null)
}