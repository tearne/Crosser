//package org.tearne.crosser.output
//
//import org.scalatest.junit.AssertionsForJUnit
//import org.junit.Test
//import org.scalatest.mock.MockitoSugar
//import org.tearne.crosser.distribution.CrosserService
//import org.tearne.crosser.cross.Cross
//import org.tearne.crosser.distribution.PlantDistribution
//import org.mockito.Mockito._
//import org.mockito.Matchers._
//import org.tearne.crosser.plant.Plant
//import sampler.data.Samplable
//import org.tearne.crosser.plant.ConcretePlant
//import org.tearne.crosser.plant.RootPlant
//import org.tearne.crosser.util.AlleleCount
//import org.tearne.crosser.distribution.CrosserService
//import org.mockito.ArgumentMatcher
//import org.mockito.ArgumentCaptor
//
//class OutputTest extends AssertionsForJUnit with MockitoSugar{
//	@Test def proportionDistributionData {
//		val cross = mock[Cross]; when(cross.name).thenReturn("cross")
//		val donor = mock[RootPlant]; when(donor.name).thenReturn("donor")
//		val crossRealisation = mock[ConcretePlant]
//		val alleleCount = mock[AlleleCount]
//		val samplable = Samplable.diracDelta(crossRealisation)
//		
//		when(alleleCount.proportion).thenReturn(0.1, 0.2, 0.3)
//		when(crossRealisation.alleleCount(donor)).thenReturn(alleleCount)
//		
//		val expectedValues = Seq(0.1,0.2,0.3)
//		val distBuilder = mock[DistributionBuilder]
//		when(distBuilder.apply(anyObject.asInstanceOf[Samplable[Double]])).thenReturn(expectedValues)
//		val service = mock[CrosserService]
//		when(service.getSamplable(cross)).thenReturn(samplable)
//		
//		val instance = ProportionDistribution(cross, donor)
//		val result = instance.buildData(services)
//		
//		assert(result.size == 1)
//		assert(result(0).values === expectedValues)
//		assert(result(0).name === "donor_in_cross")
//		
//		//Verify the right thing was sent to the distribution builder
//		val argCaptor = ArgumentCaptor.forClass(classOf[Samplable[Double]])
//		verify(distBuilder, times(1)).apply(argCaptor.capture())
//		assert(argCaptor.getValue().until(_.size == 3).sample === expectedValues)
//	}
//}