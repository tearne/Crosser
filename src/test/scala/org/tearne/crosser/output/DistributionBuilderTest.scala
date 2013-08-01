package org.tearne.crosser.output

import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.mock.MockitoSugar
import org.junit.Test
import org.mockito.Mockito._
import org.mockito.Matchers._
import sampler.math.StatisticsComponent
import sampler.data.Samplable
import sampler.data.Empirical

class DistributionBuilderTest extends AssertionsForJUnit with MockitoSugar{
	@Test def buildingFromSamplable {
		val stats = mock[StatisticsComponent]
		val tolerance = 2
		val chunkSize = 5
		val samplable = new Samplable[Int]{
			val it = (1 to 50).iterator
			def sample = it.next
		}
		when(stats.maxDistance(anyObject.asInstanceOf[Empirical[Int]], anyObject.asInstanceOf[Empirical[Int]]))
			.thenReturn(3,2,1)
		val instance = new DistributionBuilder(stats, tolerance, chunkSize)
		
		assert(instance.apply(samplable) === (1 to 15).toSeq)
	}
}