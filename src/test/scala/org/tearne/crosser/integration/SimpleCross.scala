package org.tearne.crosser.integration

import org.specs2.mutable.Specification
import java.nio.file.Paths
import org.tearne.crosser.config.CrosserConfig
import org.tearne.crosser.distribution.CrossSamplerService
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.specification.Scope
import org.tearne.crosser.distribution.PlantDistBankComponent
import org.tearne.crosser.util.Random
import org.tearne.crosser.util.Probability
import org.tearne.crosser.plant.Gameter
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.distribution.PlantDistCrosser
import org.tearne.crosser.distribution.PlantDistFactory
import org.tearne.crosser.plant.ChromosomeFactory
import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.util.AlleleCount

@RunWith(classOf[JUnitRunner])
class SimpleCross extends Specification{
	"this example" in new Environment{
		val path = Paths.get("src/test/resource/SimpleCross.config")
		val myConfig = new CrosserConfig(path)
		
		val result = crossSampler.getDistributionFor(myConfig.crosses.last._2)
		
		val ac = (for(i <- 1 to 100) yield result.sample(rnd).alleleCount(myConfig.plants("Donor1"))).foldLeft(AlleleCount(0,0)){_+_}
		println(ac.proportion)
		
		
		success
	}
	
	trait Environment extends Scope with 
			CrossSamplerService with
			PlantDistBankComponent {
		//Stuff to build a CrossSampler
		val rnd = new Random()
		val crossSampler = new CrossSampler(rnd)
		
		//Stuff needed to build a PlantDistBank
		val plantFactory = new PlantFactory()
		val chromosomeFactory = new ChromosomeFactory()
		val recombinationProb = Probability(0.01)
		val gameter = new Gameter(rnd, recombinationProb)
		val chromosomeCrosser = new ChromosomeCrosser(chromosomeFactory, gameter)
		val crosser = new Crosser(plantFactory, chromosomeCrosser)
		val plantDistFactory = new PlantDistFactory()
		val plantDistCrosser = new PlantDistCrosser(crosser, plantDistFactory, rnd, 100, 0.05)
		val plantDistBank = new PlantDistBank(plantDistCrosser)
	}
}