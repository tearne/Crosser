package org.tearne.crosser.distribution.components

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import sampler.data.EmpiricalMetricComponent
import org.specs2.specification.Scope
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.Species
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlantDistMetricSpec extends Specification with Mockito{
	val name = "plantName"
	val threeChromSpecies =  Species("mySpecies", 4,5,6)
	
	"PlantDistMetric" should{
		"return Double,maxValue if an empty distribution is passed in" in todo
		"use max on each chromosme" in new Instance{ 
			val d1a, d1b = mock[ChromosomeDistribution]
			val d2a, d2b = mock[ChromosomeDistribution]
			val d3a, d3b = mock[ChromosomeDistribution]
			
			implicit val empiricalMetric = mock[EmpiricalMetric]
			
			val instanceA = new PlantDistribution(Seq(d1a, d2a, d3a), name, threeChromSpecies, 10)
			val instanceB = new PlantDistribution(Seq(d1b, d2b, d3b), name, threeChromSpecies, 10)
			
			metric.max(d1a, d1b) returns 1
			metric.max(d2a, d2b) returns 2
			metric.max(d3a, d3b) returns 3
			
			instance(instanceA, instanceB) mustEqual (1+2+3)
		}
	}
	
	trait Instance extends Scope
			with PlantDistMetricComponent
			with EmpiricalMetricComponent{
		override val metric = mock[EmpiricalMetric]
		val instance = plantDistMetric
	}
}