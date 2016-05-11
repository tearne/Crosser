package org.tearne.crosser.plant

import org.junit.runner.RunWith
import sampler.math.Random
import org.mockito.Mockito._
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar

class GameterTest extends FreeSpec with MockitoSugar{
	val half = 0.5
	
	"Gameter should" - {
		"build from chromosomes" in {
			val pA = mock[RootPlant]
			val pB = mock[RootPlant]
			
			val tidA = Tid(IndexedSeq(pA,pA,pA,pA))
			val tidB = Tid(IndexedSeq(pB,pB,pB,pB))
			
			val chromosome = Chromosome(tidA, tidB)
			val recombProb = 0.01
			val rnd = mock[Random]
			when(rnd.nextBoolean(half)) thenReturn true //start on A side
			when(rnd.nextBoolean(recombProb)) thenReturn (false, true, true, false) //A, B, A, A
			
			val instance = new Gameter(rnd, recombProb)
			assertResult(Tid(IndexedSeq(pA,pB,pA,pA)))(instance(chromosome))
		}
	}
}