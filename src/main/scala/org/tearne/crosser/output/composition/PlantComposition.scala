package org.tearne.crosser.output.composition

import org.tearne.crosser.distribution.PlantEmpirical

case class PlantComposition(chromosomeCompositions: IndexedSeq[ChromosomeComposition])

trait PlantCompositionBuilderComponent {
	this: ChromosomeCompositionBuilderComponent =>
	
	val plantCompositionBuilder: PlantCompositionBuilder
		
	class PlantCompositionBuilder {
		def apply(plantDist: PlantEmpirical): PlantComposition = {
			PlantComposition(
				plantDist.chromoDistSeq.map{cDist => chromosomeCompositionBuilder(cDist)}
			)
		}
	}
}