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
	"this example" in{
		import CrosserServiceFactory._
		
		val path = Paths.get("src/test/resource/SimpleScheme.config")
		val myConfig = new Scheme(path)
		
		val result = crossSamplerService.getDistributionFor(myConfig.crosses.last._2)
		
		val ac = (for(i <- 1 to 100) yield result.sample.alleleCount(myConfig.plants("Donor1"))).foldLeft(AlleleCount(0,0)){_+_}
		ac.proportion must beCloseTo(0.745, 0.01)
	}
}