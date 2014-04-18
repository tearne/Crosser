package org.tearne.crosser.distribution.components

import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.ConcretePlant
import sampler.math.StatisticsComponent
import org.tearne.crosser.distribution._
import org.tearne.crosser.distribution.components._
import sampler.data.Samplable
import org.tearne.crosser.plant.Plant
import sampler.data.Distribution
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._

class CacheTest extends FreeSpec with MockitoSugar{
	val name = "myCross"
	
	"Cache should" - {
		"Build distributions from parent plant samplables from the cross sampler" in new Instance{
			val cross = mock[Cross]
			val leftParent = mock[Crossable]
			val rightParent = mock[Crossable]
			when(cross.left).thenReturn(leftParent)
			when(cross.right).thenReturn(rightParent)
			
			val leftParentSamplable = mock[Distribution[Plant]]
			val rightParentSamplable = mock[Distribution[Plant]]
			val offspringDistribution = mock[PlantEmpirical]
	
			when(crossDistributions.get(leftParent)).thenReturn(leftParentSamplable)
			when(crossDistributions.get(rightParent)).thenReturn(rightParentSamplable)
			
			when(distributionCrosser.build(leftParentSamplable, rightParentSamplable, cross))
				.thenReturn(offspringDistribution)
			
			assertResult(offspringDistribution)(instance.get(cross))
		}
		"cache results once generated" in new Instance {
			val cross = mock[Cross]
			val leftParent = mock[Crossable]
			val rightParent = mock[Crossable]
			when(cross.left).thenReturn(leftParent)
			when(cross.right).thenReturn(rightParent)
			
			val leftParentSamplable = mock[Distribution[Plant]]
			val rightParentSamplable = mock[Distribution[Plant]]
			val crossDistribution = mock[PlantEmpirical]
	
			when(crossDistributions.get(leftParent)).thenReturn(leftParentSamplable)
			when(crossDistributions.get(rightParent)).thenReturn(rightParentSamplable)
			
			//TODO pimping so it looks like... leftPDist x rightPDist
			when(distributionCrosser.build(leftParentSamplable, rightParentSamplable, cross))
				.thenReturn(crossDistribution)
			
			assertResult(crossDistribution)(instance.get(cross))
			assertResult(crossDistribution)(instance.get(cross))
			assertResult(crossDistribution)(instance.get(cross))
			verify(distributionCrosser, times(1)).build(
					any.asInstanceOf[Distribution[ConcretePlant]], 
					any.asInstanceOf[Distribution[ConcretePlant]], 
					any.asInstanceOf[Cross]
			)
		}
	}
	trait Instance  
			extends CrossDistributionsComponent 
			with CacheComponent 
			with DistributionCrosserComponent
			with StatisticsComponent {
		val statistics = null
		val metric = null
		val distributionCrosser = mock[DistributionCrosser]
		val crossDistributions = mock[CrossDistributions]
		val cache = new Cache
		
		val instance = cache
	}
}