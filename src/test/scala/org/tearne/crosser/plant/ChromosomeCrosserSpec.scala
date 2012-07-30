package org.tearne.crosser.cross

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Tid
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.TidBuilder
import org.tearne.crosser.plant.Gameter
import org.tearne.crosser.plant.ChromosomeFactory
import org.tearne.crosser.plant.ChromosomeCrosser
import org.tearne.crosser.util.Random
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.plant.ChromosomeCrosserException

@RunWith(classOf[JUnitRunner])
class ChromosomeCrosserSpec extends Specification with Mockito{
	"ChromosomeCrosser" should {
		"cross pairs of chromosomes" in {
			val leftChromosome = mock[Chromosome]
			val rightChromosome = mock[Chromosome]
			val offspringChromosome = mock[Chromosome]
			
			val gameter = mock[Gameter]
			val leftGamete = mock[Tid]
			val rightGamete = mock[Tid]
			gameter(leftChromosome) returns leftGamete
			gameter(rightChromosome) returns rightGamete
			
			val cFactory = mock[ChromosomeFactory]
			cFactory(leftGamete, rightGamete) returns offspringChromosome
			
			val instance = new ChromosomeCrosser(cFactory, gameter)
			instance(leftChromosome, rightChromosome) mustEqual offspringChromosome
		}
		"throw exception if chromosomes are of differnet lengths" in {
			val leftChromosome = mock[Chromosome]
			val rightChromosome = mock[Chromosome]
			leftChromosome.size returns 3
			rightChromosome.size returns 4
			
			val instance = new ChromosomeCrosser(null, null)
			instance(leftChromosome, rightChromosome) must throwA[ChromosomeCrosserException]
		}
	}
}