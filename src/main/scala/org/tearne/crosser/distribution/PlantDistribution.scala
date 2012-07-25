package org.tearne.crosser.distribution

import org.tearne.crosser.util.Discrete
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.util.Random
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.Species

class PlantDistribution(val chromoDists: Seq[Discrete[Chromosome]], val name: String, species: Species, failures: Int) {
	if(chromoDists.size != species.cMLengths.size) throw new PlantDistributionException("Number of chromosome distributions incompatible with specified species")
	
	val total = chromoDists(0).size
	chromoDists.foreach(dist => if(dist.size != total) throw new PlantDistributionException("Chromosome distributions inconsistent with stated total"))

	lazy val successProbability: Double = if(total == 0) 0 else failures.asInstanceOf[Double]/total
	def ++(plants: Seq[ConcretePlant], failures: Int): PlantDistribution = {
			val chromosomesByIndex = plants.map(_.chromosomes).transpose
			val newDistributions = (chromosomesByIndex zip chromoDists).map{case (chroms, dist) =>
				dist ++ chroms
			} 
			new PlantDistribution(newDistributions.toIndexedSeq, name, species, this.failures+failures)
	}
	
	def distanceTo(that: PlantDistribution): Double = 
		(chromoDists zip that.chromoDists).map{case (d1, d2) => d1.distanceTo(d2)}.sum
	
	def sample(rnd: Random): ConcretePlant = {
		val chromosomes = chromoDists.map(_.sample(rnd))
		new Plant(name, chromosomes.toIndexedSeq, species)
	}
}
object PlantDistribution{
	def apply(cross: Cross, name: String): PlantDistribution = 
			new PlantDistribution(cross.species.cMLengths.map(l => Discrete[Chromosome]()), name, cross.species, 0)
}

class PlantDistributionException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
	def this(message: String) { this(message, null) }
}
