package org.tearne.crosser.distribution.components

import sampler.math.Random
import org.tearne.crosser.distribution.components._
import org.tearne.crosser.distribution.PlantEmpirical
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.ConcretePlant
import sampler.data.Samplable
import org.tearne.crosser.plant.Plant
import sampler.data.Distribution
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

class CrossDistributionsTest extends FreeSpec with MockitoSugar{
	val name = "crossName"
	
	"CrossDistributions" - {
		"return cross samplables based on distributions in the cache" in new Instance {
			//val random = mock[Random]
			val cross = mock[Cross]
			val plantSamplable = mock[Distribution[Plant]]
			val plantEmpirical = mock[PlantEmpirical]
			
			when(cache.get(cross)) thenReturn plantEmpirical 
			when(plantEmpirical.toDistribution(random)) thenReturn plantSamplable 
			
			assertResult(plantSamplable)(instance.get(cross))
		}
		
		"return identity samplable for concrete plants" in new Instance {
			val concretePlant = mock[ConcretePlant]
			assertResult(false){
				instance.get(concretePlant)
					.until(_.size == 1000)
					.sample
					.exists(_ != concretePlant)
			}
		}
	}
	
	//TODO this looks a bit mad
	trait Instance 
			extends CrossDistributionsComponent 
			with CacheComponent 
			with DistributionCrosserComponent {
		val random = mock[Random]
		
		val statistics = null
		val metric = null
		val crossDistributions = new CrossDistributions(random)
		
		val distributionCrosser = null
		val cache = mock[Cache]
		
		val instance = crossDistributions
	}
}