package org.tearne.crosser.distribution.components

import org.tearne.crosser.cross.Crosser
import scala.annotation.tailrec
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross
import sampler.math.Random
import sampler.data.Samplable
import org.slf4j.LoggerFactory

trait PlantDistCrosserComponent{
	this: PlantDistMetricComponent =>
		
	val plantDistCrosser: PlantDistCrosser
	
	class PlantDistCrosser(crosser: Crosser, distFactory: PlantDistFactory, chunkSize: Int, tolerance: Double) {
		val log = LoggerFactory.getLogger(getClass.getName)
		
		def build(leftParentDist: Samplable[ConcretePlant], rightParentDist: Samplable[ConcretePlant], cross: Cross): PlantDistribution = {
			def buildOffspring() = 
				crosser(leftParentDist.sample, rightParentDist.sample, cross)
			
			@tailrec
			def addSamples(dist: PlantDistribution): PlantDistribution = {
				val oldDist = dist
				val offspringWithFailures = (1 to chunkSize).par.map(i => buildOffspring).seq.groupBy(o => cross.protocol.isSatisfiedBy(o))
				
				val numNewFailures =  offspringWithFailures.get(false).map(_.size).getOrElse(0)
				
				val passed = offspringWithFailures.getOrElse(true, Nil)
				
				val newDist = oldDist ++(passed, numNewFailures)
				
				val diff = plantDistMetric(oldDist, newDist)
				
				log.trace("Chunk complete on {}, diff = {}", cross.name, diff)
				log.trace("{} passed, {} failures satisfying protocol {}", passed.size.toString, numNewFailures.toString, cross.protocol.toString())
				
				if(diff < tolerance) newDist
				else addSamples(newDist)
			}
			
			log.info("Building {}...", cross.name)
			val result = addSamples(distFactory.build(cross))
			log.info("{} ...done", cross.name)
			result
		}
	}
}



class PlantDistFactory{
	def build(cross: Cross): PlantDistribution = 
		PlantDistribution(cross)
}