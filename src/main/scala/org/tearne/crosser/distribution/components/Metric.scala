package org.tearne.crosser.distribution.components

import sampler.math.Statistics
import org.slf4j.LoggerFactory
import org.tearne.crosser.distribution.PlantEmpirical

/**
 * Measures distances between plant distributions
 */
class Metric(statistics: Statistics){
	val log = LoggerFactory.getLogger(getClass.getName)
	def apply(pd1: PlantEmpirical, pd2: PlantEmpirical): Double = {
		log.trace("{} in {}", pd1.numSuccess, pd1.numSamples)
		log.trace("{} in {}", pd2.numSuccess, pd2.numSamples) 
		//TODO should do more than just num success?
		if(pd1.numSuccess != 0 || pd2.numSuccess != 0)
			(pd1.chromoDistSeq zip pd2.chromoDistSeq).map{case (d1, d2) => statistics.maxDistance(d1,d2)}.sum
		else
			Double.MaxValue
	}
}
