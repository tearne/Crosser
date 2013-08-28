package org.tearne.crosser.distribution
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.Species
import sampler.data.Samplable
import sampler.data.Empirical._
import sampler.data.Empirical
import sampler.math.Random

class PlantEmpirical(val chromoDistSeq: IndexedSeq[ChromosomeEmpirical], val name: String, species: Species, val numFailures: Int) {
	if(chromoDistSeq.size != species.cMLengths.size) throw new PlantEmpiricalException(
		"Number of chromosome distributions (%d) incompatible with specified species (%d)".format(chromoDistSeq.size, species.cMLengths.size)
	)
	
	//TODO better way to do this
	val numSuccess = chromoDistSeq(0).size 
	val numSamples = numSuccess + numFailures
	
	if(chromoDistSeq.exists(_.size != numSuccess)) throw new PlantEmpiricalException("Chromosome distributions do not all contain the same number of observations")

	lazy val successProbability: Double = if(numSamples == 0) 0 else {
		1.0 - numFailures.asInstanceOf[Double]/numSamples
	}
	def ++(successes: Seq[ConcretePlant], failures: Int): PlantEmpirical = {
		val newDistributions = if(successes.size == 0) chromoDistSeq
		else {
			val chromosomesByIndex = successes.map(_.chromosomes).transpose
			val newDists = (chromosomesByIndex zip chromoDistSeq).map{case (chroms, dist) =>
				dist ++ chroms
			} 
			newDists
		}
			
		new PlantEmpirical(newDistributions.toIndexedSeq, name, species, this.numFailures+failures)
	}
	
	def toSamplable(implicit r: Random): Samplable[Plant] = new Samplable[Plant]{
		val chromoSamplalbes = chromoDistSeq.map{_.toSamplable}
		def sample: Plant = {
			val chromosomes = chromoSamplalbes.map(_.sample)
			new Plant(name, chromosomes.toIndexedSeq, species)
		}
	}
}
object PlantEmpirical{
	def apply(cross: Cross): PlantEmpirical = 
		new PlantEmpirical(cross.species.cMLengths.map(l => ChromosomeDistribution.empty), cross.name, cross.species, 0)
}

class PlantEmpiricalFactory{
	def build(cross: Cross): PlantEmpirical = 
		PlantEmpirical(cross)
}

class PlantEmpiricalException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
	def this(message: String) { this(message, null) }
}
