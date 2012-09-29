package org.tearne.crosser.integration

import org.specs2.mutable.Specification
import java.nio.file.Paths
import org.tearne.crosser.config.CrosserConfig
import org.tearne.crosser.distribution.CrossSamplerService
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.specification.Scope
import org.tearne.crosser.distribution.PlantDistBankComponent
import org.tearne.crosser.plant.Gameter
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.distribution.PlantDistFactory
import org.tearne.crosser.plant.ChromosomeFactory
import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.util.AlleleCount
import sampler.math.Random
import sampler.math.Probability

@RunWith(classOf[JUnitRunner])
class SimpleCross extends Specification{
	"this example" in{
		//Stuff needed to build a CrossSamplerService
		val rnd = new Random
		val plantFactory = new PlantFactory()
		val chromosomeFactory = new ChromosomeFactory()
		val recombinationProb = Probability(0.01)
		val gameter = new Gameter(rnd, recombinationProb)
		val chromosomeCrosser = new ChromosomeCrosser(chromosomeFactory, gameter)
		val crosser = new Crosser(plantFactory, chromosomeCrosser)
		val plantDistFactory = new PlantDistFactory()
		
		val crossSamplerService = new CrossSamplerService(rnd, crosser, plantDistFactory, 100, 0.05)
		
		val path = Paths.get("src/test/resource/SimpleCross.config")
		val myConfig = new CrosserConfig(path)
		
		val result = crossSamplerService.getDistributionFor(myConfig.crosses.last._2)
		
		val ac = (for(i <- 1 to 100) yield result.sample(rnd).alleleCount(myConfig.plants("Donor1"))).foldLeft(AlleleCount(0,0)){_+_}
		ac.proportion must beCloseTo(0.75, 0.01)
	}
}