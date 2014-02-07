package org.tearne.crosser.output.composition

import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.distribution.PlantEmpirical

trait CompositionComponent{
	val compositionService: CompositionService
}

trait CompositionServiceComponentImpl 
		extends CompositionComponent
		with TidCompositionBuilderComponent
		with ChromosomeCompositionBuilderComponent 
		with PlantCompositionBuilderComponent {
	val compositionService = new CompositionService{
		def buildComposition(plantDist: PlantEmpirical): PlantComposition  = plantCompositionBuilder(plantDist)
	}
		
	val tidCompositionBuilder = new TidCompositionBuilder
	val chromosomeCompositionBuilder = new ChromosomeCompositionBuilder
	val plantCompositionBuilder = new PlantCompositionBuilder
}

//trait CompositionServiceImpl extends CompositionService{
//	val tidCompositionBuilder = new TidCompositionBuilder
//	val chromosomeCompositionBuilder = new ChromosomeCompositionBuilder
//	val plantCompositionBuilder = new PlantCompositionBuilder
//}

trait CompositionService 
//		extends TidCompositionBuilderComponent 
//		with ChromosomeCompositionBuilderComponent 
//		with PlantCompositionBuilderComponent 
{
	def buildComposition(plantDist: PlantEmpirical): PlantComposition
}