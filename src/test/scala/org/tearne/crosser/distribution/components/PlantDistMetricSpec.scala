package org.tearne.crosser.distribution.components

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.Species
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import sampler.math.StatisticsComponent

@RunWith(classOf[JUnitRunner])
class PlantDistMetricSpec extends Specification with Mockito{
	val name = "plantName"
	val threeChromSpecies =  Species("mySpecies", 4,5,6)
	
	"PlantDistMetric" should{
		"return Double,maxValue if an empty distribution is passed in" in todo
		"sum the max distance on each chromosme" in new Instance{ 
			val d1a, d1b = makeMockChromosomeDistribution
			val d2a, d2b = makeMockChromosomeDistribution
			val d3a, d3b = makeMockChromosomeDistribution
			
			def makeMockChromosomeDistribution = {
				val t = mock[ChromosomeDistribution]
				t.size returns 10 
				t
			}
			
			val instanceA = new PlantDistribution(Seq(d1a, d2a, d3a), name, threeChromSpecies, 10)
			val instanceB = new PlantDistribution(Seq(d1b, d2b, d3b), name, threeChromSpecies, 10)
			
			statistics.maxDistance(d1a, d1b) returns 1
			statistics.maxDistance(d2a, d2b) returns 2
			statistics.maxDistance(d3a, d3b) returns 3
			
			instance(instanceA, instanceB) mustEqual (1+2+3)
		}
	}
	
	trait MockStatisticsComponent extends StatisticsComponent{
		
	}
	
	trait Instance extends Scope with PlantDistMetricComponent{
		val statistics = mock[StatisticsComponent]
		val instance = plantDistMetric
	}
}