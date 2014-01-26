package org.tearne.crosser.output

import org.scalatest.FreeSpec

class StatisticsDistributionServiceTest extends FreeSpec {
	"Correctly balculate binomial tail probabilities" in {
		val selectionProb = 0.1
		val confidence = 0.95
		val numRequired = 20
		
		val instance = new StatisticDistributionServiceImpl{
			val tolerance: Double = 0
			val chunkSize: Int = 0
		}

		assertResult(275){
			instance.statDistBuilder.numPlantsForConfidence(confidence, selectionProb, numRequired)
		}
	}
}