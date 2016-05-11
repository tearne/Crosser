package org.tearne.crosser.distribution.components

import org.tearne.crosser.distribution.ChromosomeEmpirical
import org.tearne.crosser.distribution.PlantEmpirical
import org.tearne.crosser.plant.Species
import sampler.math.Statistics
import org.mockito.Mockito._
import org.scalatest.FreeSpec
import org.scalatest.mock.MockitoSugar

class MetricTest extends FreeSpec with MockitoSugar{
	val name = "plantName"
	val threeChromSpecies =  Species("mySpecies", 4,5,6)
	
	"Metric should" - {
		"return Double,maxValue if an empty distribution is passed in" in {
			pending
		}
		"sum the max distance on each chromosme" in new Instance{ 
			val d1a, d1b = makeMockChromosomeDistribution
			val d2a, d2b = makeMockChromosomeDistribution
			val d3a, d3b = makeMockChromosomeDistribution
			
			def makeMockChromosomeDistribution = {
				val t = mock[ChromosomeEmpirical]
				when(t.size) thenReturn 10 
				t
			}
			
			val plantDistA = new PlantEmpirical(Vector(d1a, d2a, d3a), name, threeChromSpecies, 10)
			val plantDistB = new PlantEmpirical(Vector(d1b, d2b, d3b), name, threeChromSpecies, 10)
			
			when(statistics.maxDistance(d1a, d1b)) thenReturn 1
			when(statistics.maxDistance(d2a, d2b)) thenReturn 2
			when(statistics.maxDistance(d3a, d3b)) thenReturn 3
			
			assertResult(1+2+3)(instance(plantDistA, plantDistB))
		}
	}
	
	trait Instance {
		val statistics = mock[Statistics]
		val instance = new Metric(statistics)
	}
}