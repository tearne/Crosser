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
import org.tearne.crosser.util.Random

@RunWith(classOf[JUnitRunner])
class CrossSamplerSpec extends Specification with Mockito{
	val name = "crossName"
	
	"CrossSamplerSpec" should {
		"generate plant samples by obtaining it distribution from the bank" in new Instance {
			val cross = mock[Cross]
			val plantDistribution = mock[PlantDistribution]
			plantDistBank.get(cross) returns plantDistribution
			
			val sample1 = mock[Plant]
			val sample2 = mock[Plant]
			val sample3 = mock[Plant]
			
			plantDistribution.sample(rnd) returns (sample1, sample2, sample3)
			
			(instance.sample(cross) mustEqual sample1) and
			(instance.sample(cross) mustEqual sample2) and
			(instance.sample(cross) mustEqual sample3)
		}
		
		"produce a concrete plant when sampling a concrete plant" in new Instance{
			val concretePlant = mock[ConcretePlant]
			instance.sample(concretePlant) mustEqual concretePlant
		}
	}
	trait Instance extends Scope with 
			CrossSamplerService with
			PlantDistBankComponent{
		val rnd = mock[Random]
		val crossSampler = new CrossSampler(rnd)
		val plantDistBank = mock[PlantDistBank]
		val instance = crossSampler
	}
}