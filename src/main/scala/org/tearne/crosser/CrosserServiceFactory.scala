package org.tearne.crosser

import sampler.math.Random
import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.plant.ChromosomeFactory
import sampler.math.Probability
import org.tearne.crosser.plant.Gameter
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.distribution.components.PlantDistFactory
import org.tearne.crosser.distribution.CrossSamplerService

trait CrosserServiceFactory{
	val recombinationProb: Double
	val chunkSize: Int
	val tolerance: Double
	
	implicit val rnd = Random
	
	lazy val crossSamplerService = {
		val plantFactory = new PlantFactory()
		val chromosomeFactory = new ChromosomeFactory()
		val gameter = new Gameter(rnd, Probability(recombinationProb))
		val chromosomeCrosser = new ChromosomeCrosser(chromosomeFactory, gameter)
		val crosser = new Crosser(plantFactory, chromosomeCrosser)
		val plantDistFactory = new PlantDistFactory()
		
		new CrossSamplerService(rnd, crosser, plantDistFactory, chunkSize, tolerance)
	}
}