package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.cross.Crosser
import scala.annotation.tailrec
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.Species
import sampler.math.Random
import sampler.data.Samplable
trait PlantDistCrosserComponent{
	this: PlantDistMetricComponent =>
		
	val plantDistCrosser: PlantDistCrosser
	
	class PlantDistCrosser(crosser: Crosser, distFactory: PlantDistFactory, rnd: Random, chunkSize: Int, tolerance: Double) {
		def build(leftParentDist: Samplable[ConcretePlant], rightParentDist: Samplable[ConcretePlant], cross: Cross): PlantDistribution = {
			def buildOffspring() = 
				crosser(leftParentDist.sample(rnd), rightParentDist.sample(rnd), cross)
			
			@tailrec
			def addSamples(dist: PlantDistribution): PlantDistribution = {
				val oldDist = dist
				val offspringWithFailures = (1 to chunkSize).map(i => buildOffspring).groupBy(o => cross.protocol.isSatisfiedBy(o))
				
				val numNewFailures =  offspringWithFailures.get(false).map(_.size).getOrElse(0)
				val newDist = oldDist ++(offspringWithFailures.getOrElse(true, Nil), numNewFailures)
				val diff = plantDistMetric(oldDist, newDist)
				if(diff < tolerance) newDist
				else addSamples(newDist)
			}
			
			addSamples(distFactory.build(cross))
		}
	}
}



class PlantDistFactory{
	def build(cross: Cross): PlantDistribution = 
		PlantDistribution(cross)
}