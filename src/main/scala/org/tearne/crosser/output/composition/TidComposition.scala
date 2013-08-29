package org.tearne.crosser.output.composition

import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.plant.Tid

case class TidComposition(val counts: IndexedSeq[Map[RootPlant, Int]]) {
	lazy val proportions = counts.map{ cmMap => 
		val total  = cmMap.values.sum.toDouble
		cmMap.mapValues{count => count / total}
	}
}

trait TidCompositionBuilderComponent{
	val tidCompositionBuilder: TidCompositionBuilder
	
	class TidCompositionBuilder {
		def apply(tids: Seq[Tid]) = new TidComposition({
			val size = tids(0).size
			val init = IndexedSeq.fill(size)(Map[RootPlant, Int]())
			
			tids.foldLeft(init){case (acc, tid) => 
				assert(tid.alleles.size == size, "Tid length inconsistency")
				acc.zip(tid.alleles).map{case (map, donor) => 
					if(map.contains(donor)) map + (donor -> (map(donor) + 1))
					else map + (donor -> 1)
				}
			}
		})
	}
	
}

