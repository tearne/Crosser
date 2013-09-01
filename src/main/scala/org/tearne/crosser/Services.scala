package org.tearne.crosser

import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.plant.ChromosomeFactory
import org.tearne.crosser.plant.Gameter
import org.tearne.crosser.plant.ChromosomeCrosser
import sampler.math.Probability
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.distribution.PlantEmpiricalFactory
import sampler.math.Random
import org.tearne.crosser.config.Config
import org.tearne.crosser.output.Writer
import sampler.math.StatisticsComponent
import java.nio.file.Paths
import org.tearne.crosser.output._
import org.tearne.crosser.distribution._
import org.tearne.crosser.output.composition._
import org.slf4j.LoggerFactory

trait Services {
	val crossingService: CrossingService
	val statisticsDistributionService: StatisticDistributionService
	val compositionService: CompositionService
}

trait RootComponent {
	val random: Random
	val chunkSize: Int
	val tolerance: Double
}

trait ServicesImpl extends Services { 
	self: RootComponent =>
		
	val crossingService = new {
		val random = self.random
		val chunkSize = self.chunkSize
		val tolerance = self.tolerance
	} with CrossingServiceImpl
	
	val statisticsDistributionService = new {
		val chunkSize = self.chunkSize
		val tolerance = self.tolerance
	} with StatisticDistributionServiceImpl
	
	val compositionService = new CompositionServiceImpl{}
}
