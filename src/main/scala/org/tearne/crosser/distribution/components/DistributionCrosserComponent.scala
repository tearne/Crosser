package org.tearne.crosser.distribution.components

import scala.annotation.tailrec

import org.slf4j.LoggerFactory
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.distribution.PlantEmpirical
import org.tearne.crosser.distribution.PlantEmpiricalFactory
import org.tearne.crosser.plant.ConcretePlant

import sampler.data.Distribution
import sampler.data.Samplable

/**
 * Builds plant distributions from parent distributions
 */
trait DistributionCrosserComponent{
	val distributionCrosser: DistributionCrosser
	
	class DistributionCrosser(crosser: Crosser, distFactory: PlantEmpiricalFactory, chunkSize: Int, criterion: ConvergenceCriterion) {
		val log = LoggerFactory.getLogger(getClass.getName)
		
		def build(leftParentDist: Distribution[ConcretePlant], rightParentDist: Distribution[ConcretePlant], cross: Cross): PlantEmpirical = {
			def buildOffspring() = 
				crosser(leftParentDist.sample, rightParentDist.sample, cross)
			
			@tailrec
			def addSamples(dist: PlantEmpirical): PlantEmpirical = {
				val oldDist = dist
				val offspringWithFailures = (1 to chunkSize).par.map(i => buildOffspring).seq.groupBy(o => cross.protocol.isSatisfiedBy(o))
				
				val numNewFailures =  offspringWithFailures.get(false).map(_.size).getOrElse(0)
				
				val passed = offspringWithFailures.getOrElse(true, Nil)
				val newDist = oldDist ++(passed, numNewFailures)
				
				log.trace("So far: {} passed, {} failed", newDist.numSuccess, newDist.numFailures)
				
				if(criterion.hasConverged(oldDist, newDist)) newDist
				else addSamples(newDist)
			}
			
			log.info("Building {}...", cross.name)
			val result = addSamples(distFactory.build(cross))
			result
		}
	}
}



