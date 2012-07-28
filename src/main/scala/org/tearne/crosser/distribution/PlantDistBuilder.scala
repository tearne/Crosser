package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.util.Random
import org.tearne.crosser.cross.Crosser

class PlantDistBuilder(crosser: Crosser, pDistFactory: PlantDistFactory, rnd: Random, chunkSize: Int, tolerance: Double) {
	def build(leftParentDist: PlantDistribution, rightParentDist: PlantDistribution): PlantDistribution = {
		throw new UnsupportedOperationException("todo")
	}
}

class PlantDistFactory{
	def build(plants: Seq[Plant], numFailures: Int): PlantDistribution = throw new UnsupportedOperationException("todo")
	def augment(oldDist:PlantDistribution, plants: Seq[Plant], numFailures: Int): PlantDistribution = throw new UnsupportedOperationException("todo")
}