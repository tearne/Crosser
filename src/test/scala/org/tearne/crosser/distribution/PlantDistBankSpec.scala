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
		"build distributions with parent dists obtained from self via the cross sampler" in new Instance {
			val cross = mock[Cross]
			val leftParent = mock[Crossable]
			val rightParent = mock[Crossable]
			cross.left returns leftParent
			cross.right returns rightParent
			
			val leftParentDistribution = mock[PlantDistribution]
			val rightParentDistribution = mock[PlantDistribution]
			val crossDistribution = mock[PlantDistribution]
	
			crossSampler.getDistributionFor(leftParent) returns leftParentDistribution
			crossSampler.getDistributionFor(rightParent) returns rightParentDistribution
			
			//TODO pimping with implicits, so it looks like...
			// leftPDist x rightPDist
			plantDistCrosser.build(leftParentDistribution, rightParentDistribution, cross) returns crossDistribution
			
			instance.get(cross) mustEqual crossDistribution
		}
		"return the (samplable) concrete plant when asked for distribution of a concrete plant" in new Instance {
			val concretePlant = mock[ConcretePlant]
			instance.get(concretePlant) mustEqual concretePlant
		}
		"cache results once calculated" in new Instance {
			val cross = mock[Cross]
			val leftParent = mock[Crossable]
			val rightParent = mock[Crossable]
			cross.left returns leftParent
			cross.right returns rightParent
			
			val leftParentDistribution = mock[PlantDistribution]
			val rightParentDistribution = mock[PlantDistribution]
			val crossDistribution = mock[PlantDistribution]
	
			crossSampler.getDistributionFor(leftParent) returns leftParentDistribution
			crossSampler.getDistributionFor(rightParent) returns rightParentDistribution
			
			//TODO pimping with implicits, so it looks like...
			// leftPDist x rightPDist
			plantDistCrosser.build(leftParentDistribution, rightParentDistribution, cross) returns crossDistribution
			
			(instance.get(cross) mustEqual crossDistribution) and
			(instance.get(cross) mustEqual crossDistribution) and
			(instance.get(cross) mustEqual crossDistribution) and
			(there was one(plantDistCrosser).build(any, any, any))
		}
	}
	trait Instance extends 
			Scope with 
			CrossSamplerService with 
			PlantDistBankComponent{
		val plantDistCrosser = mock[PlantDistCrosser]
		
		val crossSampler = mock[CrossSampler]
		val plantDistBank = new PlantDistBank(plantDistCrosser)
		val instance = plantDistBank
	}
}