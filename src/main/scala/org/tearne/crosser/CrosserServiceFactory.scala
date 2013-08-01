package org.tearne.crosser

import sampler.math.Random
import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.plant.ChromosomeFactory
import sampler.math.Probability
import org.tearne.crosser.plant.Gameter
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.distribution.components._
import sampler.math.StatisticsComponent
import org.tearne.crosser.distribution.PlantDistributionFactory
import org.tearne.crosser.distribution.CrosserService

object CrosserServiceFactory {
	def apply(tolerance: Double, chunkSize: Int, recombinationProb: Double = 0.01)(implicit rnd: Random) = {
		val plantFactory = new PlantFactory()
		val chromosomeFactory = new ChromosomeFactory()
		val gameter = new Gameter(rnd, Probability(recombinationProb))
		val chromosomeCrosser = new ChromosomeCrosser(chromosomeFactory, gameter)
		val crosser = new Crosser(plantFactory, chromosomeCrosser)
		val plantDistFactory = new PlantDistributionFactory()
		
		new CrosserService(rnd, crosser, plantDistFactory, chunkSize, tolerance)
	}
	
}