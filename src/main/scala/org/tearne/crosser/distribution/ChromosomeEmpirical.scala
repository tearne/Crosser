package org.tearne.crosser.distribution

import org.tearne.crosser.plant.Chromosome
import sampler.math.Random
import sampler.data.EmpiricalSeq
import scala.collection.GenTraversableOnce

case class ChromosomeEmpirical(val samples: IndexedSeq[Chromosome]) extends EmpiricalSeq[Chromosome](samples){
	//TODO do this scala collections style. EmpiricalSeqLike?
	override def ++(more: GenTraversableOnce[Chromosome]) = new ChromosomeEmpirical(samples ++ more)
}
object ChromosomeDistribution{
	implicit val r = Random
	def empty = new ChromosomeEmpirical(IndexedSeq[Chromosome]())
}