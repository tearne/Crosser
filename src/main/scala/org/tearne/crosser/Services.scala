package org.tearne.crosser

import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.plant.ChromosomeFactory
import org.tearne.crosser.plant.Gameter
import org.tearne.crosser.plant.ChromosomeCrosser
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
import sampler.math.Statistics

trait Services 
		extends CrossStatisticsComponent 
		with CrossingComponent
		with CompositionComponent 
		with StatisticsComponent {
}

trait SystemConfig {
	val random: Random
	val chunkSize: Int
	val tolerance: Double
	val fewestPlants: Int
}

trait ServicesImpl 
	extends Services 
	with CrossStatisticsComponent
	with CrossingComponent
	with CompositionServiceComponentImpl
	with StatisticsComponent { 
	
	self: SystemConfig =>
		
	val crossStatistics = new CrossStatistics(
		self.chunkSize,
		self.tolerance
	)

	val crossingService = new {
		val random = self.random
		val chunkSize = self.chunkSize
		val tolerance = self.tolerance
		val fewestPlants = self.fewestPlants
	} with CrossingServiceImpl
	
	val statistics = Statistics
}

