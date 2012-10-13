package org.tearne.crosser.distribution.components

import org.tearne.crosser.plant.Chromosome
import sampler.data.EmpiricalSeq
import sampler.data.Empirical._
import scala.collection.GenTraversableOnce

case class ChromosomeDistribution(val samples: IndexedSeq[Chromosome]) extends EmpiricalSeq[Chromosome](samples){
	val size = samples.size
	
	//TODO do this scala collections style. EmpiricalSeqLike?
	override def ++(more: GenTraversableOnce[Chromosome]) = new ChromosomeDistribution(samples ++ more)
}
object ChromosomeDistribution{
	def empty = new ChromosomeDistribution(IndexedSeq[Chromosome]())
}