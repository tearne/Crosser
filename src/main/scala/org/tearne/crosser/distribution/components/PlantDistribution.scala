package org.tearne.crosser.distribution.components

import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.Species
import sampler.math.Random
import sampler.data.Samplable
import sampler.data.EmpiricalTable
import sampler.data.Empirical._

class PlantDistribution(val chromoDists: Seq[ChromosomeDistribution], val name: String, species: Species, val failures: Int) extends Samplable[ConcretePlant]{
	if(chromoDists.size != species.cMLengths.size) throw new PlantDistributionException(
		"Number of chromosome distributions (%d) incompatible with specified species (%d)".format(chromoDists.size, species.cMLengths.size)
	)
	
	//def numObservations(dist: EmpiricalTable[Chromosome]) = dist.counts.foldLeft(0){case (acc, (chrom, count)) => acc + count}
	
	//TODO better way to do this
	val total = chromoDists(0).size//numObservations(chromoDists(0))
	chromoDists.foreach(dist => if(dist.size != total) throw new PlantDistributionException("Chromosome distributions do not all contain the same number of observations"))

	lazy val successProbability: Double = if(total == 0) 0 else failures.asInstanceOf[Double]/total
	def ++(plants: Seq[ConcretePlant], failures: Int): PlantDistribution = {
			val chromosomesByIndex = plants.map(_.chromosomes).transpose
			val newDistributions = (chromosomesByIndex zip chromoDists).map{case (chroms, dist) =>
				dist ++ chroms
			} 
			new PlantDistribution(newDistributions.toIndexedSeq, name, species, this.failures+failures)
	}
	
	def sample(implicit rnd: Random): ConcretePlant = {
		val chromosomes = chromoDists.map(_.sample(rnd))
		new Plant(name, chromosomes.toIndexedSeq, species)
	}
}
object PlantDistribution{
	def apply(cross: Cross): PlantDistribution = 
		new PlantDistribution(cross.species.cMLengths.map(l => ChromosomeDistribution.empty), cross.name, cross.species, 0)
}

class PlantDistributionException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
	def this(message: String) { this(message, null) }
}
