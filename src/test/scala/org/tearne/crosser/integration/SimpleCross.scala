package org.tearne.crosser.integration

import org.specs2.mutable.Specification
import java.nio.file.Paths
import org.tearne.crosser.scheme.Scheme
import org.tearne.crosser.distribution.CrossSamplerService
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.specification.Scope
import sampler.math.Random
import sampler.math.Probability
import org.tearne.crosser.CrosserServiceFactory
import org.tearne.crosser.util.AlleleCount

@RunWith(classOf[JUnitRunner])
class SimpleCross extends Specification{
	"this example" in new CrosserServiceFactory with Scope{
		val scheme = new Scheme(Paths.get("src/test/resource/simpleScheme.config"))
		
		val tolerance = scheme.tolerance
		val recombinationProb = scheme.recombinationProb
		val chunkSize = scheme.chunkSize
		
		val result = crossSamplerService.getDistributionFor(scheme.crosses.last._2)
		val ac = (for(i <- 1 to 1000) yield result.sample.alleleCount(scheme.plants("Donor1"))).foldLeft(AlleleCount(0,0)){_+_}
		
		ac.proportion must beCloseTo(0.745, 0.01)
	}
}