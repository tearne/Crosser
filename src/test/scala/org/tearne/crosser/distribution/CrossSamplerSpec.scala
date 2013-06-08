package org.tearne.crosser.distribution

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.cross.Crossable
import org.specs2.specification.Scope
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import sampler.math.Random
import org.tearne.crosser.distribution.components.PlantDistCrosserComponent
import org.tearne.crosser.distribution.components.PlantDistribution
import org.tearne.crosser.distribution.components.PlantDistMetricComponent
import org.tearne.crosser.distribution.components.PlantDistBankComponent
import sampler.math.StatisticsComponent

@RunWith(classOf[JUnitRunner])
class CrossSamplerSpec extends Specification with Mockito{
	val name = "crossName"
	
	"CrossSamplerSpec" should {
		"return plant distributions obtained from the bank" in new Instance{
			val cross = mock[Cross]
			val plantDistribution = mock[PlantDistribution]
			plantDistBank.get(cross) returns plantDistribution
			
			instance.getDistributionFor(cross) mustEqual plantDistribution
		}
		"generate plant samples by obtaining it's distribution from the bank" in new Instance {
			val cross = mock[Cross]
			val plantDistribution = mock[PlantDistribution]
			plantDistBank.get(cross) returns plantDistribution
			
			val sample1 = mock[Plant]
			val sample2 = mock[Plant]
			val sample3 = mock[Plant]
			
			plantDistribution.sample returns (sample1, sample2, sample3)
			
			(instance.sample(cross) mustEqual sample1) and
			(instance.sample(cross) mustEqual sample2) and
			(instance.sample(cross) mustEqual sample3)
		}

	}
	
	//TODO this looks a bit mad
	trait Instance extends Scope 
			with CrossSamplerComponent 
			with PlantDistBankComponent 
			with PlantDistCrosserComponent
			with PlantDistMetricComponent{
		val rnd = mock[Random]
		
		val statistics = mock[StatisticsComponent]
		val crossSampler = new CrossSampler(rnd)
		val plantDistCrosser = null
		override val plantDistBank = mock[PlantDistBank]
		
		val instance = crossSampler
	}
}