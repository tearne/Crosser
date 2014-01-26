package org.tearne.crosser.output

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant
import sampler.data.Samplable
import org.tearne.crosser.plant.ConcretePlant
import sampler.data.ParallelSampleBuilder
import sampler.math.StatisticsComponent
import sampler.data.Empirical._
import org.apache.commons.math3.distribution.BinomialDistribution

trait StatisticDistributionService {
	this: StatisticsComponent => 
	
	val tolerance: Double
	val chunkSize: Int
	
	val statDistBuilder: StatisticDistributionService
	
	def build[T](samplable: Samplable[T]) = statDistBuilder(samplable)
	
	class StatisticDistributionService() {
		def apply[T](samplable: Samplable[T]): Seq[T] = {
			new ParallelSampleBuilder(chunkSize)(samplable)(seq => {
				maxDistance(seq.take(seq.size - chunkSize).toEmpiricalSeq, seq.toEmpiricalSeq) < tolerance ||
					seq.size == 1e8
			})
			.seq
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

trait StatisticDistributionServiceImpl 
		extends StatisticDistributionService
		with StatisticsComponent {
	val statDistBuilder = new StatisticDistributionService
}