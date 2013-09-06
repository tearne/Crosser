package org.tearne.crosser.distribution.components

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import org.junit.runner.RunWith
import sampler.math.Random
import org.tearne.crosser.distribution.components._
import org.tearne.crosser.distribution.PlantEmpirical
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.ConcretePlant
import sampler.data.Samplable
import org.tearne.crosser.plant.Plant

@RunWith(classOf[JUnitRunner])
class CrossSamplableSpec extends Specification with Mockito{
	val name = "crossName"
	
	"CrossSamplerSpec" should {
		"return cross samplables based on distributions in the cache" in new Instance{
			//val random = mock[Random]
			val cross = mock[Cross]
			val plantSamplable = mock[Samplable[Plant]]
			val plantEmpirical = mock[PlantEmpirical]
			
			cache.get(cross) returns plantEmpirical
			plantEmpirical.toSamplable(random) returns plantSamplable
			
			instance.get(cross) mustEqual plantSamplable
		}
		
		"return identity samplable for concrete plants" in new Instance{
			val concretePlant = mock[ConcretePlant]
			instance.get(concretePlant).until(_.size == 1000).sample.exists(_ != concretePlant) must_== false
		}
	}
	
	//TODO this looks a bit mad
	trait Instance extends Scope 
			with CrossSamplableComponent 
			with CacheComponent 
			with DistributionCrosserComponent {
		val random = mock[Random]
		
		val statistics = null
		val metric = null
		val crossSamplable = new CrossSamplable(random)
		
		val distributionCrosser = null
		val cache = mock[Cache]
		
		val instance = crossSamplable
	}
}