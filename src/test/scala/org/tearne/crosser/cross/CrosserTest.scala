package org.tearne.crosser.cross

import org.junit.runner.RunWith
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.Species
import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.plant.Plant
import sampler.math.Random
import org.scalatest.mock.MockitoSugar
import org.scalatest.FreeSpec
import org.mockito.Mockito._
import org.tearne.crosser.plant.ConcretePlant

class CrosserTest extends FreeSpec with MockitoSugar{
	val name = "myCross"

	"Crosser should" - {
		"Generate offspring plants from concrete parent plants" in {
			val species = mock[Species]
			val plantFactory = mock[PlantFactory]
			val chromosomeCrosser = mock[ChromosomeCrosser]
			
			val cross = mock[Cross]
			assertResult(name)(cross.name)
			assertResult(species)(cross.species)
			
			
			val left = mock[ConcretePlant]
			val c1left = mock[Chromosome]
			val c2left = mock[Chromosome]
			val c3left = mock[Chromosome]
			when(left.chromosomes).thenReturn(IndexedSeq(c1left, c2left, c3left))
			
			val right = mock[ConcretePlant]
			val c1right = mock[Chromosome]
			val c2right = mock[Chromosome]
			val c3right = mock[Chromosome]
			when(right.chromosomes).thenReturn(IndexedSeq(c1right, c2right, c3right))
			
			val c1offspring = mock[Chromosome]
			val c2offspring = mock[Chromosome]
			val c3offspring = mock[Chromosome]
			when(chromosomeCrosser(c1left, c1right)).thenReturn(c1offspring)
			when(chromosomeCrosser(c2left, c2right)).thenReturn(c2offspring)
			when(chromosomeCrosser(c3left, c3right)).thenReturn(c3offspring)
			
			val offspringPlant = mock[Plant]
			when(plantFactory(name, IndexedSeq(c1offspring, c2offspring, c3offspring), species)).thenReturn(offspringPlant)
			val instance = new Crosser(plantFactory, chromosomeCrosser)
			
			assertResult(offspringPlant)(instance(left, right, cross)) 
		}
		"throw exception if left and right are different species" in {
			val left = mock[ConcretePlant]
			when(left.species).thenReturn(mock[Species])
			
			val right = mock[ConcretePlant]
			when(right.species).thenReturn(mock[Species])
			
			intercept[CrosserException]{
				new Crosser(null, null)(left, right, null)
			}
		}
	}
}