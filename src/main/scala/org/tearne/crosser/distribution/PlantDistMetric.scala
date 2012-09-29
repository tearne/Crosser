package org.tearne.crosser.distribution

import sampler.data.EmpiricalMetricComponent

//TODO test
trait PlantDistMetricComponent {
	this: EmpiricalMetricComponent =>
		
	val plantDistMetric = new PlantDistMetric()
	
	class PlantDistMetric{
		def apply(pd1: PlantDistribution, pd2: PlantDistribution) = 
			(pd1.chromoDists zip pd2.chromoDists).map{case (d1, d2) => metric.max(d1,d2)}.sum
	}
}