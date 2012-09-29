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

object CrosserServiceFactory {
	implicit val rnd = new Random
	
	private val plantFactory = new PlantFactory()
	private val chromosomeFactory = new ChromosomeFactory()
	private val recombinationProb = Probability(0.01)
	private val gameter = new Gameter(rnd, recombinationProb)
	private val chromosomeCrosser = new ChromosomeCrosser(chromosomeFactory, gameter)
	private val crosser = new Crosser(plantFactory, chromosomeCrosser)
	private val plantDistFactory = new PlantDistFactory()
	
	val crossSamplerService = new CrossSamplerService(rnd, crosser, plantDistFactory, 100, 0.05)
}