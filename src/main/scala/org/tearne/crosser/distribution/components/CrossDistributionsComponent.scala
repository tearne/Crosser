package org.tearne.crosser.distribution.components

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.ConcretePlant

import sampler.data.Distribution
import sampler.math.Random

/**
 * Return root plants directly or samples from banked cross distributions
 */
trait CrossDistributionsComponent{
	//Cake pattern allows immutable mutual dependency 
	// between CrossDistributions and CacheComponent
	this: CacheComponent =>
	
	val crossDistributions: CrossDistributions 

	class CrossDistributions(rnd: Random) {
		def get(crossable: Crossable): Distribution[ConcretePlant] = {
			crossable match {
				case cross: Cross => cache.get(cross).toDistribution(rnd)
				case concrete: ConcretePlant => Distribution.continually(concrete) 
			}
		}
	}
}