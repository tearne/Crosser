package org.tearne.crosser.distribution.components

import sampler.math.Random
import org.tearne.crosser.cross.Crossable
import sampler.data.Samplable
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.ConcretePlant

/**
 * Return root plants directly or samples from banked cross distributions
 */
trait CrossSamplableComponent{
	//Cake pattern allows immutable mutual dependency 
	// between CrossSampler and CacheComponent
	this: CacheComponent =>
	
	val crossSamplable: CrossSamplable 

	class CrossSamplable(rnd: Random) {
		def get(crossable: Crossable): Samplable[ConcretePlant] = {
			crossable match {
				case cross: Cross => cache.get(cross).toSamplable(rnd)
				case concrete: ConcretePlant => Samplable.continually(concrete) 
			}
		}
	}
}