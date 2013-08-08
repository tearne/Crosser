package org.tearne.crosser.output.composition

import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.distribution.ChromosomeDistribution

case class ChromosomeComposition(val left: TidComposition, val right: TidComposition)

trait ChromosomeCompositionBuilderComponent {
	this: TidCompositionBuilderComponent => 
	
	val chromosomeCompositionBuilder: ChromosomeCompositionBuilder
		
	class ChromosomeCompositionBuilder {
		def apply(cDist: ChromosomeDistribution): ChromosomeComposition = {
			ChromosomeComposition(
				tidCompositionBuilder(cDist.samples.map(_.left)), 
				tidCompositionBuilder(cDist.samples.map(_.right))
			)
		}
	}
}