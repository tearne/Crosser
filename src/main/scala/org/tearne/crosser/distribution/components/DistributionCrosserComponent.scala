package org.tearne.crosser.distribution.components

import org.tearne.crosser.cross.Crosser
import scala.annotation.tailrec
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross
import sampler.math.Random
import sampler.data.Samplable
import org.slf4j.LoggerFactory
import org.tearne.crosser.distribution.PlantEmpiricalFactory
import org.tearne.crosser.distribution.PlantEmpirical

/**
 * Builds plant distributions from parent distributions
 */
trait DistributionCrosserComponent{
	this: MetricComponent =>
		
	val distributionCrosser: DistributionCrosser
	
	class DistributionCrosser(crosser: Crosser, distFactory: PlantEmpiricalFactory, chunkSize: Int, tolerance: Double) {
		val log = LoggerFactory.getLogger(getClass.getName)
		
		def build(leftParentDist: Samplable[ConcretePlant], rightParentDist: Samplable[ConcretePlant], cross: Cross): PlantEmpirical = {
			def buildOffspring() = 
				crosser(leftParentDist.sample, rightParentDist.sample, cross)
			
			@tailrec
			def addSamples(dist: PlantEmpirical): PlantEmpirical = {
				val oldDist = dist
				val offspringWithFailures = (1 to chunkSize).par.map(i => buildOffspring).seq.groupBy(o => cross.protocol.isSatisfiedBy(o))
				
				val numNewFailures =  offspringWithFailures.get(false).map(_.size).getOrElse(0)
				
				val passed = offspringWithFailures.getOrElse(true, Nil)
				
				val newDist = oldDist ++(passed, numNewFailures)
				
				val diff = metric(oldDist, newDist)
				
				log.trace("So far: {} passed, {} failed", newDist.numSuccess, newDist.numFailures)
				
				if(diff < tolerance) newDist
				else addSamples(newDist)
			}
			
			log.info("Building {}...", cross.name)
			val result = addSamples(distFactory.build(cross))
			result
		}
	}
}



