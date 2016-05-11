package org.tearne.crosser.output

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant
import sampler.data.Samplable
import org.tearne.crosser.plant.ConcretePlant
import sampler.math.StatisticsComponent
import org.apache.commons.math3.distribution.BinomialDistribution
import org.tearne.crosser.SystemConfig
import sampler.data.Distribution
import sampler.data.ParallelSampler
import sampler.Implicits._
import sampler.math.Statistics
import sampler.data.ConvergenceProtocol
import sampler.data.MaxMetric

trait CrossStatisticsComponent {
	this: StatisticsComponent => 
	
	val crossStatistics: CrossStatistics
	
	class CrossStatistics(chunkSize: Int, tolerance: Double) {
		def gatherSamples[T](distribution: Distribution[T]): Seq[T] = {
			
			ParallelSampler(distribution)(
				new ConvergenceProtocol[T](chunkSize, tolerance, 100000000) with MaxMetric
			).seq
		}
		
		def numPlantsForConfidence(confidenceReq: Double, selectionProb: Double, numPlantsReq: Int): Int = {
			var binom = new BinomialDistribution(numPlantsReq-1, selectionProb)
			while(binom.cumulativeProbability(numPlantsReq-1) > 1-confidenceReq){
				binom = new BinomialDistribution(binom.getNumberOfTrials()+1, selectionProb)
			}
			binom.getNumberOfTrials()
		  }
	}
}