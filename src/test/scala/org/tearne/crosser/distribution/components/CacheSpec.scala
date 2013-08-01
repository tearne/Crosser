package org.tearne.crosser.distribution.components

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.ConcretePlant
import org.specs2.specification.Scope
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import sampler.math.StatisticsComponent
import org.tearne.crosser.distribution._
import org.tearne.crosser.distribution.components._

@RunWith(classOf[JUnitRunner])
class PlantDistBankSpec extends Specification with Mockito{
	val name = "myCross"
	
	"PlantDistBank" should {
		"build distributions from parent dists obtained from self via the cross sampler" in new Instance {
			val cross = mock[Cross]
			val leftParent = mock[Crossable]
			val rightParent = mock[Crossable]
			cross.left returns leftParent
			cross.right returns rightParent
			
			val leftParentDistribution = mock[PlantDistribution]
			val rightParentDistribution = mock[PlantDistribution]
			val crossDistribution = mock[PlantDistribution]
	
			crossSamplable.get(leftParent) returns leftParentDistribution
			crossSamplable.get(rightParent) returns rightParentDistribution
			
			distributionCrosser.build(leftParentDistribution, rightParentDistribution, cross) returns crossDistribution
			
			instance.get(cross) mustEqual crossDistribution
		}
//		"return the (samplable) concrete plant when asked for distribution of a concrete plant" in new Instance {
//			val concretePlant = mock[ConcretePlant]
//			instance.get(concretePlant) mustEqual concretePlant
//		}
		"cache results once generated" in new Instance {
			val cross = mock[Cross]
			val leftParent = mock[Crossable]
			val rightParent = mock[Crossable]
			cross.left returns leftParent
			cross.right returns rightParent
			
			val leftParentDistribution = mock[PlantDistribution]
			val rightParentDistribution = mock[PlantDistribution]
			val crossDistribution = mock[PlantDistribution]
	
			crossSamplable.get(leftParent) returns leftParentDistribution
			crossSamplable.get(rightParent) returns rightParentDistribution
			
			//TODO pimping so it looks like... leftPDist x rightPDist
			distributionCrosser.build(leftParentDistribution, rightParentDistribution, cross) returns crossDistribution
			
			(instance.get(cross) mustEqual crossDistribution) and
			(instance.get(cross) mustEqual crossDistribution) and
			(instance.get(cross) mustEqual crossDistribution) and
			(there was one(distributionCrosser).build(any, any, any))
		}
	}
	trait Instance extends Scope  
			with CrossSamplableComponent 
			with CacheComponent 
			with DistributionCrosserComponent
			with MetricComponent
			with StatisticsComponent {
		val statistics = null//mock[StatisticsComponent]
		val metric = null//mock[Metric]
		val distributionCrosser = mock[DistributionCrosser]
		val crossSamplable = mock[CrossSamplable]
		val cache = new Cache
		
		val instance = cache
	}
}