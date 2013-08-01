package org.tearne.crosser.output

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant
import sampler.data.Samplable
import org.tearne.crosser.plant.ConcretePlant
import sampler.data.ParallelSampleBuilder
import sampler.math.StatisticsComponent
import sampler.data.Empirical._

class DistributionBuilder(stats: StatisticsComponent, tolerance: Double, chunkSize: Int) {
	def apply[T](samplable: Samplable[T]): Seq[T] = {
		new ParallelSampleBuilder(chunkSize)(samplable)(seq => {
			stats.maxDistance(seq.take(seq.size - chunkSize).toEmpiricalSeq, seq.toEmpiricalSeq) < tolerance ||
				seq.size == 1e8
		})
		.seq
	}
}