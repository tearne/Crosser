package org.tearne.crosser.output

import org.scalatest.FreeSpec
import sampler.math.StatisticsComponent

class StatisticsServiceTest extends FreeSpec {
	"Correctly balculate binomial tail probabilities" in {
		val selectionProb = 0.1
		val confidence = 0.95
		val numRequired = 20
		
		//Irrelevant to these tests
		val tolerance: Double = 0
		val chunkSize: Int = 0

		val instanceComponent = new StatisticsServiceComponent	
				with StatisticsComponent {
			val statistics = null//Statistics
			val statisticsService = new StatisticsService(chunkSize, tolerance)
		}
		val instance = instanceComponent.statisticsService
		
		assertResult(275){
			instance.numPlantsForConfidence(confidence, selectionProb, numRequired)
		}
	}
}