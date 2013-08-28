package org.tearne.crosser.output.composition

import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.distribution.PlantEmpirical

trait CompositionServiceImpl extends CompositionService{
	val tidCompositionBuilder = new TidCompositionBuilder
	val chromosomeCompositionBuilder = new ChromosomeCompositionBuilder
	val plantCompositionBuilder = new PlantCompositionBuilder
}

trait CompositionService 
		extends TidCompositionBuilderComponent 
		with ChromosomeCompositionBuilderComponent 
		with PlantCompositionBuilderComponent {
	
	def buildComposition(plantDist: PlantEmpirical): PlantComposition 
		= plantCompositionBuilder(plantDist)
}