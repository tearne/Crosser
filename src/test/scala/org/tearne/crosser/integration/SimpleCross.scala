package org.tearne.crosser.integration

import java.nio.file.Paths
import sampler.math.Random
import org.tearne.crosser.util.AlleleCount
import org.tearne.crosser.plant.ConcretePlant
import sampler.math.StatisticsComponent
import org.scalatest.FreeSpec
import org.tearne.crosser.SystemConfig
import org.tearne.crosser.config.ConfigFactory
import org.tearne.crosser.ServicesImpl
import sampler.data.Distribution
import sampler.data.ParallelSampler
import sampler.data.ConvergenceProtocol
import sampler.data.MaxMetric
import sampler.data.ConvergenceProtocol
import org.scalautils.Tolerance._

class SimpleCross extends FreeSpec{
		"Simple cross integration test" in {
			val config = ConfigFactory.fromPath(Paths.get("src/test/resource/integrationTest.config"))

			trait SystemConfigImpl extends SystemConfig {
				val chunkSize = config.chunkSize
				val tolerance = config.tolerance
				val fewestPlants = config.fewestPlants
				val random = Random
			}
			
			val services = new SystemConfigImpl with ServicesImpl
			
			val resultPlantDist: Distribution[ConcretePlant] = services.crossingService.getDistribution(config.crosses.last._2)
			val donorOfInterest = config.plants("Donor1")
			
			val proportionD1 = ParallelSampler(resultPlantDist)(new ConvergenceProtocol[ConcretePlant](config.chunkSize, config.tolerance, 100000000) with MaxMetric)
				.map((_: ConcretePlant).alleleCount(donorOfInterest))
				.foldLeft(AlleleCount(0,0)){_+_}.proportion
			
			assert(proportionD1 === (0.745 +- 0.01))
		}
}