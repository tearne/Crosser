package org.tearne.crosser.plant

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import sampler.math.Random
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameterSpec extends Specification with Mockito{
	val half = 0.5
	
	"Gameter" should {
		"build from chromosomes" in {
			val pA = mock[RootPlant]
			val pB = mock[RootPlant]
			
			val tidA = Tid(IndexedSeq(pA,pA,pA,pA))
			val tidB = Tid(IndexedSeq(pB,pB,pB,pB))
			
			val chromosome = Chromosome(tidA, tidB)
			val recombProb = 0.01
			val rnd = mock[Random]
			rnd.nextBoolean(half) returns true //start on A
			rnd.nextBoolean(recombProb) returns (false, true, true, false) //A, B, A, A
			
			val instance = new Gameter(rnd, recombProb)
			instance(chromosome) mustEqual Tid(IndexedSeq(pA,pB,pA,pA))
		}
	}
}