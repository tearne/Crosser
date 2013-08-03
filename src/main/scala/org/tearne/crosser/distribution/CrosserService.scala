package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.distribution.components._
import sampler.math.StatisticsComponent
import sampler.math.Random
import org.tearne.crosser.cross.Crossable
import sampler.data.Samplable
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross

class CrosserService(
		rnd: Random, 
		crosser: Crosser, 
		distFactory: PlantDistributionFactory, 
		chunkSize: Int, 
		tolerance: Double
	) extends CrossSamplableComponent
		with CacheComponent
		with DistributionCrosserComponent
		with MetricComponent{
	
	val statistics = StatisticsComponent
	
	val cache = new Cache
	val crossSamplable = new CrossSamplable(rnd)
	val distributionCrosser = new DistributionCrosser(crosser, distFactory, chunkSize, tolerance)
	val metric = new Metric
	
	//TODO should test this?
	def getSamplable(crossable: Crossable): Samplable[ConcretePlant] = crossSamplable.get(crossable)
	def getSuccessProbability(cross: Cross): Double = cache.get(cross).successProbability
}