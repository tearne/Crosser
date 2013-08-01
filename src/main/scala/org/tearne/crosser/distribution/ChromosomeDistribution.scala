package org.tearne.crosser.distribution

import org.tearne.crosser.plant.Chromosome
import sampler.math.Random
import sampler.data.EmpiricalSeq
import scala.collection.GenTraversableOnce

case class ChromosomeDistribution(val samples: IndexedSeq[Chromosome]) extends EmpiricalSeq[Chromosome](samples){
	val size = samples.size
	
	//TODO do this scala collections style. EmpiricalSeqLike?
	override def ++(more: GenTraversableOnce[Chromosome]) = new ChromosomeDistribution(samples ++ more)
	
	//TODO dep injection
	val samplable = this.toSamplable(Random)
	def sample = samplable.sample
}
object ChromosomeDistribution{
	implicit val r = Random
	def empty = new ChromosomeDistribution(IndexedSeq[Chromosome]())
}