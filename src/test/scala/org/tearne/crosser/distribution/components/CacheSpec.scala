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
import sampler.data.Samplable
import org.tearne.crosser.plant.Plant

@RunWith(classOf[JUnitRunner])
class CacheSpec extends Specification with Mockito{
	val name = "myCross"
	
	"Cache" should {
		"build distributions from parent plant samplables from the cross sampler" in new Instance {
			val cross = mock[Cross]
			val leftParent = mock[Crossable]
			val rightParent = mock[Crossable]
			cross.left returns leftParent
			cross.right returns rightParent
			
			val leftParentSamplable = mock[Samplable[Plant]]
			val rightParentSamplable = mock[Samplable[Plant]]
			val offspringDistribution = mock[PlantEmpirical]
	
			crossSamplable.get(leftParent) returns leftParentSamplable
			crossSamplable.get(rightParent) returns rightParentSamplable
			
			distributionCrosser.build(leftParentSamplable, rightParentSamplable, cross) returns offspringDistribution
			
			instance.get(cross) mustEqual offspringDistribution
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
			
			val leftParentSamplable = mock[Samplable[Plant]]
			val rightParentSamplable = mock[Samplable[Plant]]
			val crossDistribution = mock[PlantEmpirical]
	
			crossSamplable.get(leftParent) returns leftParentSamplable
			crossSamplable.get(rightParent) returns rightParentSamplable
			
			//TODO pimping so it looks like... leftPDist x rightPDist
			distributionCrosser.build(leftParentSamplable, rightParentSamplable, cross) returns crossDistribution
			
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
			with StatisticsComponent {
		val statistics = null//mock[StatisticsComponent]
		val metric = null//mock[Metric]
		val distributionCrosser = mock[DistributionCrosser]
		val crossSamplable = mock[CrossSamplable]
		val cache = new Cache
		
		val instance = cache
	}
}