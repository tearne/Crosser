package org.tearne.crosser.distribution.components

import org.tearne.crosser.distribution.PlantEmpirical
import org.slf4j.LoggerFactory

class ConvergenceCriterion(metric: Metric, tolerance: Double, fewestPlants: Int) {
	val log = LoggerFactory.getLogger(this.getClass())
	
	def hasConverged(oldDist: PlantEmpirical, newDist: PlantEmpirical): Boolean = {
		if(newDist.numSuccess < fewestPlants){
			log.debug("Too few successful offspring: required {}, had {}", fewestPlants, newDist.numSuccess)
			false
		} else{ 
			val dist = metric.apply(oldDist, newDist)
			if(dist > tolerance){
				log.debug("Tolerance not yet achieved: required {}, had {}", tolerance, dist)
				false
			}
			else {
				log.debug("Convergence achieved with {} successful offspring out of {}", newDist.numSuccess, newDist.numSamples)
				true
			}
		}
	}
}