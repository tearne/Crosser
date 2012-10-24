package org.tearne.crosser.distribution.components

import sampler.data.EmpiricalMetricComponent

trait PlantDistMetricComponent {
	this: EmpiricalMetricComponent =>
		
	val plantDistMetric = new PlantDistMetric()
	
	class PlantDistMetric{
		def apply(pd1: PlantDistribution, pd2: PlantDistribution): Double =
			if(pd1.size != 0 || pd2.size != 0)
				(pd1.chromoDists zip pd2.chromoDists).map{case (d1, d2) => metric.max(d1,d2)}.sum
			else
				Double.MaxValue
	}
}