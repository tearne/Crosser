package org.tearne.crosser.distribution

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.RootPlant
import org.specs2.specification.Scope

@RunWith(classOf[JUnitRunner])
class PlantDistBankSpec extends Specification with Mockito{
	val name = "myCross"
	
	"PlantDistBank" should {
		"build distributions using parent samples obtained from the cross sampler" in new Instance {
			val cross = mock[Cross]
			val leftParent = mock[Crossable]
			val rightParent = mock[Crossable]
			
			val leftParentDistribution = mock[PlantDistribution]
			val rightParentDistribution = mock[PlantDistribution]
			val crossDistribution = mock[PlantDistribution]
	
			//TODO pimping with implicits, so it looks like...
			// leftPDist x rightPDist
			plantDistBuilder.build(leftParentDistribution, rightParentDistribution) returns crossDistribution
			
			instance.get(cross) mustEqual crossDistribution
		}
		"throw exception if asked for distribution of concrete plant" in new Instance {
			instance.get(mock[ConcretePlant]) must throwA[PlantDistBankException]
		}
	}
	trait Instance extends 
			Scope with 
			CrossSamplerService with 
			PlantDistBankComponent{
		val plantDistBuilder = mock[PlantDistBuilder]
		
		val crossSampler = mock[CrossSampler]
		val plantDistBank = new PlantDistBank(plantDistBuilder)
		val instance = plantDistBank
	}
}