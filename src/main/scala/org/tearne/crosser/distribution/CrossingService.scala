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
import org.tearne.crosser.plant.ChromosomeCrosser
import sampler.data.Distribution
import sampler.math.Statistics
import org.tearne.crosser.distribution.components.CrossDistributionsComponent

trait RandomComponent{
	val random: Random
}

trait CrossingService
		extends CrossDistributionsComponent
		with CacheComponent
		with DistributionCrosserComponent
		with RandomComponent {
	
	def getDistribution(crossable: Crossable): Distribution[ConcretePlant] = crossDistributions.get(crossable)
	def getSuccessProbability(cross: Cross): Double = cache.get(cross).successProbability
	def getPlantDistribution(cross: Cross): PlantEmpirical = cache.get(cross)
}
		
trait CrossingServiceImpl 
		extends CrossingService {
	val chunkSize: Int
	val tolerance: Double
	val fewestPlants: Int
	
	val crossDistributions = new CrossDistributions(random)

	val plantFactory = new PlantFactory()
	val chromosomeFactory = new ChromosomeFactory()
	val gameter = new Gameter(random, 0.01)
	val chromosomeCrosser = new ChromosomeCrosser(chromosomeFactory, gameter)
	val crosser = new Crosser(plantFactory, chromosomeCrosser)
	
	val cache = new Cache()
	
	val plantDistFactory = new PlantEmpiricalFactory()
	val convCriterion = new ConvergenceCriterion(new Metric(Statistics), tolerance, fewestPlants)
	val distributionCrosser = new DistributionCrosser(crosser, plantDistFactory, chunkSize, convCriterion)
}