package org.tearne.crosser.distribution.components

import sampler.math.StatisticsComponent
import org.slf4j.LoggerFactory
import org.tearne.crosser.distribution.PlantDistribution

/**
 * Measures distances between plant distributions
 */
trait MetricComponent {
	val statistics: StatisticsComponent
		
	val metric: Metric
	
	class Metric{
		val log = LoggerFactory.getLogger(getClass.getName)
		def apply(pd1: PlantDistribution, pd2: PlantDistribution): Double = {
			log.trace("{} in {}", pd1.numSuccess, pd1.numSamples)
			log.trace("{} in {}", pd2.numSuccess, pd2.numSamples) 
			//TODO should do more than just num success?
			if(pd1.numSuccess != 0 || pd2.numSuccess != 0)
				(pd1.chromoDists zip pd2.chromoDists).map{case (d1, d2) => statistics.maxDistance(d1,d2)}.sum
			else
				Double.MaxValue
		}
	}
}

trait MetricComponentImpl extends MetricComponent{
	val statistics = StatisticsComponent
	val metric = new Metric
}