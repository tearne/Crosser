package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.distribution.components._
import sampler.math.StatisticsComponent
import sampler.math.Random
import org.tearne.crosser.cross.Crossable
import sampler.data.Samplable
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.plant.ChromosomeFactory
import org.tearne.crosser.plant.Gameter
import sampler.math.Probability
import org.tearne.crosser.plant.ChromosomeCrosser

trait RandomComponent{
	val random: Random
}

trait CrossingService
		extends CrossSamplableComponent
		with CacheComponent
		with DistributionCrosserComponent
		with MetricComponent
		with StatisticsComponent
		with RandomComponent {
	
	def getSamplable(crossable: Crossable): Samplable[ConcretePlant] = crossSamplable.get(crossable)
	def getSuccessProbability(cross: Cross): Double = cache.get(cross).successProbability
	def getPlantDistribution(cross: Cross): PlantEmpirical = cache.get(cross)
}
		
trait CrossingServiceImpl 
		extends CrossingService 
		with MetricComponentImpl{
	val chunkSize: Int
	val tolerance: Double
	
	val crossSamplable = new CrossSamplable(random)

	val plantFactory = new PlantFactory()
	val chromosomeFactory = new ChromosomeFactory()
	val gameter = new Gameter(random, Probability(0.01))
	val chromosomeCrosser = new ChromosomeCrosser(chromosomeFactory, gameter)
	val crosser = new Crosser(plantFactory, chromosomeCrosser)
	
	val cache = new Cache()
	
	val plantDistFactory = new PlantEmpiricalFactory()
	val distributionCrosser = new DistributionCrosser(crosser, plantDistFactory, chunkSize, tolerance)
}