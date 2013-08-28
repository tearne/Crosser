package org.tearne.crosser.output.composition

import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.distribution.ChromosomeDistribution
import org.tearne.crosser.distribution.ChromosomeEmpirical

case class ChromosomeComposition(val left: TidComposition, val right: TidComposition)

trait ChromosomeCompositionBuilderComponent {
	this: TidCompositionBuilderComponent => 
	
	val chromosomeCompositionBuilder: ChromosomeCompositionBuilder
		
	class ChromosomeCompositionBuilder {
		def apply(cDist: ChromosomeEmpirical): ChromosomeComposition = {
			ChromosomeComposition(
				tidCompositionBuilder(cDist.samples.map(_.left)), 
				tidCompositionBuilder(cDist.samples.map(_.right))
			)
		}
	}
}