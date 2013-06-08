package org.tearne.crosser.distribution.components

import org.tearne.crosser.plant.Chromosome
import sampler.data.EmpiricalSeq
import sampler.data.Empirical._
import scala.collection.GenTraversableOnce
import sampler.math.Random

case class ChromosomeDistribution(val samples: IndexedSeq[Chromosome])(implicit r: Random) extends EmpiricalSeq[Chromosome](samples)(r){
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