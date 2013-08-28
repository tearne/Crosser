package org.tearne.crosser.distribution

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import sampler.data.Empirical
import org.tearne.crosser.plant.Chromosome
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mock.MockitoSugar
import sampler.math.Random
import sampler.data.Samplable

class ChromosomeEmpiricalTest extends AssertionsForJUnit with MockitoSugar{
	val samples = (1 to 3).map{i => mock[Chromosome]}

	@Test def isAnEmpirical{
		assert(ChromosomeEmpirical(null).isInstanceOf[Empirical[Chromosome]] === true)
	}
	
	@Test def addingSamples{
		val instance = ChromosomeEmpirical(samples)
		
		val moreSamples = (1 to 3).map{i => mock[Chromosome]}
		assert(instance.++(moreSamples) === ChromosomeEmpirical(samples.++(moreSamples)))
	}
	
	@Test def buildingSamplable{
		val instance = ChromosomeEmpirical(samples)
		implicit val random = mock[Random]; when(random.nextInt(3)).thenReturn(0,1,2,0,2)
		
		val samplable: Samplable[Chromosome] = instance.toSamplable
		val results = (1 to 5).map(i => samplable.sample)
		val expected = IndexedSeq(samples(0),samples(1),samples(2),samples(0),samples(2))
		
		assert(results === expected)
	}
}