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

trait Services extends StatisticsServiceComponent with StatisticsComponent{
	val crossingService: CrossingService
	val compositionService: CompositionService
}

trait SystemConfig {
	val random: Random
	val chunkSize: Int
	val tolerance: Double
	val fewestPlants: Int
}

trait ServicesImpl 
	extends Services 
	with StatisticsServiceComponent
	with StatisticsComponent { 
	
	self: SystemConfig =>
		
	val crossingService = new {
		val random = self.random
		val chunkSize = self.chunkSize
		val tolerance = self.tolerance
		val fewestPlants = self.fewestPlants
	} with CrossingServiceImpl
	
	val statService = new StatisticsService(
		self.chunkSize,
		self.tolerance
	)
	
	val compositionService = new CompositionServiceImpl{}
}

