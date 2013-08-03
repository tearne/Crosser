package org.tearne.crosser.distribution
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.Species
import sampler.data.Samplable
import sampler.data.Empirical._

class PlantDistribution(val chromoDists: IndexedSeq[ChromosomeDistribution], val name: String, species: Species, val numFailures: Int) extends Samplable[ConcretePlant]{
	if(chromoDists.size != species.cMLengths.size) throw new PlantDistributionException(
		"Number of chromosome distributions (%d) incompatible with specified species (%d)".format(chromoDists.size, species.cMLengths.size)
	)
	
	//TODO better way to do this
	val numSuccess = chromoDists(0).size 
	val numSamples = numSuccess + numFailures
	
	chromoDists.foreach(dist => if(dist.size != numSuccess) throw new PlantDistributionException("Chromosome distributions do not all contain the same number of observations"))

	lazy val successProbability: Double = if(numSamples == 0) 0 else {
		1.0 - numFailures.asInstanceOf[Double]/numSamples
	}
	def ++(successes: Seq[ConcretePlant], failures: Int): PlantDistribution = {
		val newDistributions = if(successes.size == 0) chromoDists
		else {
			val chromosomesByIndex = successes.map(_.chromosomes).transpose
			val newDists = (chromosomesByIndex zip chromoDists).map{case (chroms, dist) =>
				dist ++ chroms
			} 
			newDists
		}
			
		new PlantDistribution(newDistributions.toIndexedSeq, name, species, this.numFailures+failures)
	}
	
	//TODO This doesn't work.  It just iterates along the diagonal
	// of the n-dimensional chromosome space
//	def iterator = new Iterator[Plant]{
//		val chromoDistIterators = chromoDists.map(_.samples.iterator)
//		
//		def next = Plant(name, chromoDistIterators.map(_.next), species)
//		def hasNext = chromoDistIterators.forall(_.hasNext)
//	}
	
	def sample: Plant = {
		val chromosomes = chromoDists.map(_.sample)
		new Plant(name, chromosomes.toIndexedSeq, species)
	}
}
object PlantDistribution{
	def apply(cross: Cross): PlantDistribution = 
		new PlantDistribution(cross.species.cMLengths.map(l => ChromosomeDistribution.empty), cross.name, cross.species, 0)
}

class PlantDistributionFactory{
	def build(cross: Cross): PlantDistribution = 
		PlantDistribution(cross)
}

class PlantDistributionException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
	def this(message: String) { this(message, null) }
}
