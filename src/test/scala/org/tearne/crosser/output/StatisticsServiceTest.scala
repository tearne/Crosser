package org.tearne.crosser.output

import org.scalatest.FreeSpec
import sampler.math.StatisticsComponent
import sampler.data.Distribution
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import sampler.math.Statistics
import sampler.data.Empirical

class StatisticsServiceTest extends FreeSpec with MockitoSugar {
	
	"Statistics service should" - {
		"Draw samples until convergence" in {
			val chunkSize = 3
			val tolerance = 0.1
			
			val instanceComponent = new CrossStatisticsComponent	
					with StatisticsComponent {
				val statistics = mock[Statistics]
				val crossStatistics = new CrossStatistics(chunkSize, tolerance)
			}
			val instance = instanceComponent.crossStatistics
			
			val dist = new Distribution[Int]{
				val it = (1 to 50).iterator
				def sample = it.next
			}
			when(instanceComponent.statistics.maxDistance(anyObject.asInstanceOf[Empirical[Int]], anyObject.asInstanceOf[Empirical[Int]]))
				.thenReturn(0.3, 0.2, 0.1, 0.09)
			
			assert(instance.gatherSamples(dist) === (1 to 12).toSeq)
		}
		
		"Calculate binomial tail probabilities" in {
			val selectionProb = 0.1
			val confidence = 0.95
			val numRequired = 20
			
			//Irrelevant to these tests
			val tolerance: Double = 0
			val chunkSize: Int = 0
	
			val instanceComponent = new CrossStatisticsComponent	
					with StatisticsComponent {
				val statistics = null//Statistics
				val crossStatistics = new CrossStatistics(chunkSize, tolerance)
			}
			val instance = instanceComponent.crossStatistics
			
			assertResult(275){
				instance.numPlantsForConfidence(confidence, selectionProb, numRequired)
			}
		}
	}
}