package org.tearne.crosser.cross

import org.tearne.crosser.plant.Tid
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.TidBuilder
import org.tearne.crosser.plant.Gameter
import org.tearne.crosser.plant.ChromosomeFactory
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.plant.ChromosomeCrosserException
import org.mockito.Mockito._
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar

class ChromosomeCrosserTest extends FreeSpec with MockitoSugar{
	"ChromosomeCrosser should" -{
		"Cross pairs of chromosomes" in {
			val leftChromosome = mock[Chromosome]
			val rightChromosome = mock[Chromosome]
			val offspringChromosome = mock[Chromosome]
			
			val gameter = mock[Gameter]
			val leftGamete = mock[Tid]
			val rightGamete = mock[Tid]
			when(gameter(leftChromosome)) thenReturn leftGamete
			when(gameter(rightChromosome)) thenReturn rightGamete
			
			val cFactory = mock[ChromosomeFactory]
			when(cFactory(leftGamete, rightGamete)) thenReturn offspringChromosome
			
			val instance = new ChromosomeCrosser(cFactory, gameter)
			assertResult(offspringChromosome)(instance(leftChromosome, rightChromosome))
		}
		"Throw exception if chromosomes are of differnet lengths" in {
			val leftChromosome = mock[Chromosome]
			val rightChromosome = mock[Chromosome]
			when(leftChromosome.size) thenReturn 3
			when(rightChromosome.size) thenReturn 4
			
			val instance = new ChromosomeCrosser(null, null)
			intercept[ChromosomeCrosserException]{
				instance(leftChromosome, rightChromosome)
			}
		}
	}
}