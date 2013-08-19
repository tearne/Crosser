package org.tearne.crosser.output

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant
import sampler.data.Samplable
import org.tearne.crosser.plant.ConcretePlant
import sampler.data.ParallelSampleBuilder
import sampler.math.StatisticsComponent
import sampler.data.Empirical._

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
	}
}

trait StatisticDistributionServiceImpl 
		extends StatisticDistributionService
		with StatisticsComponent {
	val statDistBuilder = new StatisticDistributionService
}