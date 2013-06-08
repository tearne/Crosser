package org.tearne.crosser.cross

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.Species
import org.tearne.crosser.plant.PlantFactory
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.plant.Plant
import sampler.math.Random

@RunWith(classOf[JUnitRunner])
class CrosserSpec extends Specification with Mockito{
	val name = "myCross"

	"Crosser" should {
		"generate offspring plants from concrete parent plants" in {
			val species = mock[Species]
			val plantFactory = mock[PlantFactory]
			val chromosomeCrosser = mock[ChromosomeCrosser]
			
			val cross = mock[Cross]
			cross.name returns name
			cross.species returns species
			
			
			val left = mock[ConcretePlant]
			val c1left = mock[Chromosome]
			val c2left = mock[Chromosome]
			val c3left = mock[Chromosome]
			left.chromosomes returns IndexedSeq(c1left, c2left, c3left)
			
			val right = mock[ConcretePlant]
			val c1right = mock[Chromosome]
			val c2right = mock[Chromosome]
			val c3right = mock[Chromosome]
			right.chromosomes returns IndexedSeq(c1right, c2right, c3right)
			
			val c1offspring = mock[Chromosome]
			val c2offspring = mock[Chromosome]
			val c3offspring = mock[Chromosome]
			chromosomeCrosser(c1left, c1right) returns c1offspring
			chromosomeCrosser(c2left, c2right) returns c2offspring
			chromosomeCrosser(c3left, c3right) returns c3offspring
			
			val offspringPlant = mock[Plant]
			plantFactory(name, IndexedSeq(c1offspring, c2offspring, c3offspring), species) returns offspringPlant
			val instance = new Crosser(plantFactory, chromosomeCrosser)
			
			instance(left, right, cross) mustEqual offspringPlant
		}
		"throw exception if left and right are different species" in {
			val left = mock[ConcretePlant]
			left.species returns mock[Species]
			
			val right = mock[ConcretePlant]
			right.species returns mock[Species]
			
			new Crosser(null, null)(left, right, null) must throwA[CrosserException]
		}
	}
}