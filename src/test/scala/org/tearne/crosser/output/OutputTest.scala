package org.tearne.crosser.output

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.scalatest.mock.MockitoSugar
import org.tearne.crosser.cross.Cross
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.tearne.crosser.plant.Plant
import sampler.data.Samplable
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.util.AlleleCount
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentCaptor
import org.scalatest.FreeSpec
import sampler.data.Distribution
import sampler.math.StatisticsComponent
import sampler.math.Statistics
import org.tearne.crosser.distribution.CrossingService
import org.tearne.crosser.Services
import org.tearne.crosser.output.composition.CompositionComponent
import sun.awt.geom.Crossings
import org.tearne.crosser.distribution.CrossingComponent

class OutputTest extends FreeSpec with MockitoSugar {
	
	"Save proportion distribution data" in {
		val cross = mock[Cross]; when(cross.name).thenReturn("cross")
		val donor = mock[RootPlant]; when(donor.name).thenReturn("donor")
		val crossRealisation = mock[ConcretePlant]
		val alleleCount = mock[AlleleCount]
		val crossDistribution = Distribution.continually(crossRealisation)
		
		when(alleleCount.proportion).thenReturn(0.1, 0.2, 0.3)
		when(crossRealisation.alleleCount(donor)).thenReturn(alleleCount)
		
		val expectedValues = Seq(0.1,0.2,0.3)
		
		val services = new Services{
			val statistics = null
			val crossStatistics = mock[CrossStatistics]
			val crossingService = mock[CrossingService]
			val compositionService = null
		}
		when(services.crossingService.getDistribution(cross)).thenReturn(crossDistribution)
		when(services.crossStatistics.gatherSamples(anyObject.asInstanceOf[Distribution[Double]])).thenReturn(expectedValues)
		
		val instance = ProportionDistribution(cross, donor)
		val result = instance.buildCSVLines(services)
		
		assert(result.size == 4)
		assert(result.head === Seq("donor_in_cross"))
		assert(result.tail.map(_.head) === expectedValues)
		
		//Verify the right distribution was sent to crossStatistics
		val argCaptor = ArgumentCaptor.forClass(classOf[Distribution[Double]])
		verify(services.crossStatistics, times(1)).gatherSamples(argCaptor.capture())
		assert(argCaptor.getValue().until(_.size == 3).sample === expectedValues)
	}
}