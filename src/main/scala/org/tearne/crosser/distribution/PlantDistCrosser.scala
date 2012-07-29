package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.util.Random
import org.tearne.crosser.cross.Crosser
import scala.annotation.tailrec
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.Species

class PlantDistCrosser(crosser: Crosser, distFactory: PlantDistFactory, rnd: Random, chunkSize: Int, tolerance: Double) {
	def build(leftParentDist: Samplable, rightParentDist: Samplable, cross: Cross): PlantDistribution = {
		def buildOffspring() = 
			crosser.cross(leftParentDist.sample(rnd), rightParentDist.sample(rnd), cross.name)
		
		@tailrec
		def addSamples(dist: PlantDistribution): PlantDistribution = {
			val oldDist = dist
			val offspringWithFailures = (1 to chunkSize).map(i => buildOffspring).groupBy(o => cross.protocol.isSatisfiedBy(o))
			
			val numNewFailures =  offspringWithFailures.get(false).map(_.size).getOrElse(0)
			val newDist = oldDist ++(offspringWithFailures.getOrElse(true, Nil), numNewFailures)
			val diff = oldDist.distanceTo(newDist)
			println(diff)
			if(diff < tolerance) newDist
			else addSamples(newDist)
		}
		
		addSamples(distFactory.build(cross))
	}
}

class PlantDistFactory{
	def build(cross: Cross): PlantDistribution = 
		PlantDistribution(cross)

	def augment(oldDist:PlantDistribution, plants: Seq[Plant], numFailures: Int): PlantDistribution = throw new UnsupportedOperationException("todo")
}