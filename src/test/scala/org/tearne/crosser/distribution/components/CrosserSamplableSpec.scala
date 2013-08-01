package org.tearne.crosser.distribution.components

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import org.junit.runner.RunWith
import sampler.math.Random
import org.tearne.crosser.distribution.components._
import org.tearne.crosser.distribution.PlantDistribution
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.ConcretePlant

@RunWith(classOf[JUnitRunner])
class CrossSamplableSpec extends Specification with Mockito{
	val name = "crossName"
	
	"CrossSamplerSpec" should {
		"return cross samplables from the cache" in new Instance{
			val cross = mock[Cross]
			val plantDistribution = mock[PlantDistribution]
			cache.get(cross) returns plantDistribution
			
			instance.get(cross) mustEqual plantDistribution
		}
		
		"return identity samplalbe for concrete plants" in new Instance{
			val concretePlant = mock[ConcretePlant]
			instance.get(concretePlant).until(_.size == 1000).sample.exists(_ != concretePlant) must_== false
		}
	}
	
	//TODO this looks a bit mad
	trait Instance extends Scope 
			with CrossSamplableComponent 
			with CacheComponent 
			with DistributionCrosserComponent
			with MetricComponent{
		val rnd = mock[Random]
		
		val statistics = null
		val metric = null
		val crossSamplable = mock[CrossSamplable]
		
		val distributionCrosser = null
		val cache = mock[Cache]
		
		val instance = crossSamplable
	}
}