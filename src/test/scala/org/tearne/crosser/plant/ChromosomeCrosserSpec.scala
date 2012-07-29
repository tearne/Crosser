package org.tearne.crosser.cross

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Tid
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.TidBuilder
import org.tearne.crosser.plant.Gameter
import org.tearne.crosser.plant.ChromosomeBuilder
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.util.Random

class ChromosomeCrosserSpec extends Specification with Mockito{
	"ChromosomeCrosser" should {
		"cross pairs of chromosomes" in {
			val leftChromosome = mock[Chromosome]
			val rightChromosome = mock[Chromosome]
			val offspringChromosome = mock[Chromosome]
			val rnd = mock[Random]
			
			val gameter = mock[Gameter]
			val leftGamete = mock[Tid]
			val rightGamete = mock[Tid]
			gameter.buildFrom(leftChromosome) returns leftGamete
			gameter.buildFrom(rightChromosome) returns rightGamete
			
			val cBuilder = mock[ChromosomeBuilder]
			cBuilder(leftGamete, rightGamete) returns offspringChromosome
			
			val instance = new ChromosomeCrosser(gameter, rnd)
			instance.cross(leftChromosome, rightChromosome) mustEqual offspringChromosome
		}
		"throw exception if chromosomes are of differnet lengths" in {
			todo
		}
	}
}