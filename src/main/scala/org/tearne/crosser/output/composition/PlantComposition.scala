package org.tearne.crosser.output.composition

import org.tearne.crosser.distribution.PlantDistribution

case class PlantComposition(chromosomeCompositions: IndexedSeq[ChromosomeComposition])

trait PlantCompositionBuilderComponent {
	this: ChromosomeCompositionBuilderComponent =>
	
	val plantCompositionBuilder: PlantCompositionBuilder
		
	class PlantCompositionBuilder {
		def apply(plantDist: PlantDistribution): PlantComposition = {
			PlantComposition(
				plantDist.chromoDists.map{cDist => chromosomeCompositionBuilder(cDist)}
			)
		}
	}
}