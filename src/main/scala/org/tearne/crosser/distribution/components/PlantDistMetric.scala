package org.tearne.crosser.distribution.components

import sampler.math.StatisticsComponent
import org.slf4j.LoggerFactory

trait PlantDistMetricComponent {
	val statistics: StatisticsComponent
		
	val plantDistMetric = new PlantDistMetric()
	
	class PlantDistMetric{
		val log = LoggerFactory.getLogger(getClass.getName)
		def apply(pd1: PlantDistribution, pd2: PlantDistribution): Double = {
			log.trace("{}", pd1.size)
			log.trace("{}", pd2.size)
			if(pd1.size != 0 || pd2.size != 0)
				(pd1.chromoDists zip pd2.chromoDists).map{case (d1, d2) => statistics.maxDistance(d1,d2)}.sum
			else
				Double.MaxValue
		}
	}
}