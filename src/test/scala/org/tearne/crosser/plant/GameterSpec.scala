package org.tearne.crosser.plant

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import sampler.math.Probability
import sampler.math.Random

@RunWith(classOf[JUnitRunner])
class GameterSpec extends Specification with Mockito{
	val half = Probability(0.5)
	
	"Gameter" should {
		"build from chromosomes" in {
			val pA = mock[RootPlant]
			val pB = mock[RootPlant]
			
			val tidA = Tid(IndexedSeq(pA,pA,pA,pA))
			val tidB = Tid(IndexedSeq(pB,pB,pB,pB))
			
			val chromosome = Chromosome(tidA, tidB)
			val recombProb = Probability(0.01)
			val rnd = mock[Random]
			rnd.nextBoolean(half) returns true //start on A
			rnd.nextBoolean(recombProb) returns (false, true, true, false) //A, B, A, A
			
			val instance = new Gameter(rnd, recombProb)
			instance(chromosome) mustEqual Tid(IndexedSeq(pA,pB,pA,pA))
		}
	}
}