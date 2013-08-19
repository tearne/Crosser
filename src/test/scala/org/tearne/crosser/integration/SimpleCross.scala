//package org.tearne.crosser.integration
//
//import org.specs2.mutable.Specification
//import java.nio.file.Paths
//import org.junit.runner.RunWith
//import org.specs2.runner.JUnitRunner
//import org.specs2.specification.Scope
//import sampler.math.Random
//import sampler.math.Probability
//import org.tearne.crosser.CrosserServiceFactory
//import org.tearne.crosser.util.AlleleCount
//import org.tearne.crosser.plant.ConcretePlant
//import sampler.math.StatisticsComponent
//import sampler.data.ParallelSampleBuilder
//import org.specs2.execute.Pending
//
//@RunWith(classOf[JUnitRunner])
//class SimpleCross extends Specification{
//	//	"this example" in new CrosserServiceFactory with StatisticsComponent with Scope{
//	//		val scheme = new Scheme(Paths.get("src/test/resource/integrationTest.config"))
//	//		
//	//		val tolerance = scheme.tolerance
//	//		val recombinationProb = scheme.recombinationProb
//	//		val chunkSize = scheme.chunkSize
//	//		
//	//		val resultPlantDist = crossSamplerService.getDistributionFor(scheme.crosses.last._2)
//	//		val donorOfInterest = scheme.plants("Donor1")
//	//		
//	//		val proportionD1 = new ParallelSampleBuilder(chunkSize)(resultPlantDist)(seq => 
//	//			seq.size == 100000
//	//		)
//	//		.map((_: ConcretePlant).alleleCount(donorOfInterest))
//	//		.foldLeft(AlleleCount(0,0)){_+_}.proportion
//	//		
//	//		proportionD1 must beCloseTo(0.745, 0.01)
//	//	}
//}