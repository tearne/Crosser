package org.tearne.crosser.distribution

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.util.Discrete
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.util.Random
import org.tearne.crosser.plant.Species
import org.tearne.crosser.cross.Cross
import org.specs2.specification.Scope

@RunWith(classOf[JUnitRunner])
class PlantDistributionSpec extends Specification with Mockito{
	val name = "plantName"
	val threeCSpecies =  Species("mySpecies", 4,5,6)
	
	"PlantDistribution" should {
		"be samplable" in{
			mock[PlantDistribution] must beAnInstanceOf[Samplable]
		}
		"throw exception if species inconsistent with distributions" in{
			val chromoDists = Seq(mock[Discrete[Chromosome]], mock[Discrete[Chromosome]])//Two chromosomes
			
			new PlantDistribution(chromoDists, "myPlant", threeCSpecies, 0) must throwA[PlantDistributionException]
		}
		"determine non-zero success probability given inputs" in {
			val dist = mock[Discrete[Chromosome]]
			dist.size returns 100
			val chromoDists = Seq(dist, dist, dist)
			
			val instance = new PlantDistribution(chromoDists, null, threeCSpecies, 10)
			instance.successProbability mustEqual 0.1
		}
		"determine zero success probability given inputs" in {
			val dist = mock[Discrete[Chromosome]]
			dist.size returns 0
			val chromoDists = Seq(dist, dist, dist)
			
			val instance = new PlantDistribution(chromoDists, null, threeCSpecies, 0)
			instance.successProbability mustEqual 0
		}
		"have ++ to generate augmented distribution" in {
			//These are the chromosomes which will are already in the plant distribution instance
			val c0_1 = mock[Chromosome]; val c0_2 = mock[Chromosome]; val c0_3 = mock[Chromosome]
			val d1 = new Discrete[Chromosome](IndexedSeq(c0_1))
			val d2 = new Discrete[Chromosome](IndexedSeq(c0_2))
			val d3 = new Discrete[Chromosome](IndexedSeq(c0_3))
			val instance = new PlantDistribution(Seq(d1,d2,d3), name, threeCSpecies, 10)
			
			val c1_1 = mock[Chromosome]; val c1_2 = mock[Chromosome]; val c1_3 = mock[Chromosome]
			val p1 = mock[ConcretePlant]; p1.chromosomes returns IndexedSeq(c1_1, c1_2, c1_3)
			val c2_1 = mock[Chromosome]; val c2_2 = mock[Chromosome]; val c2_3 = mock[Chromosome]
			val p2 = mock[ConcretePlant]; p2.chromosomes returns IndexedSeq(c2_1, c2_2, c2_3)
			val c3_1 = mock[Chromosome]; val c3_2 = mock[Chromosome]; val c3_3 = mock[Chromosome]
			val p3 = mock[ConcretePlant]; p3.chromosomes returns IndexedSeq(c3_1, c3_2, c3_3)
			
			val result = instance ++(Seq(p1,p2,p3), 2)
			
			val resultD1 = result.chromoDists(0)
			val resultD2 = result.chromoDists(1)
			val resultD3 = result.chromoDists(2)
			
			(resultD1.elements must containAllOf(Seq(c0_1, c1_1, c2_1, c3_1))) and
			(resultD2.elements must containAllOf(Seq(c0_2, c1_2, c2_2, c3_2))) and
			(resultD3.elements must containAllOf(Seq(c0_3, c1_3, c2_3, c3_3)))
		}
		"support distanceTo" in {
			val d1a, d1b = mock[Discrete[Chromosome]]
			val d2a, d2b = mock[Discrete[Chromosome]]
			val d3a, d3b = mock[Discrete[Chromosome]]
			
			val instanceA = new PlantDistribution(Seq(d1a, d2a, d3a), name, threeCSpecies, 10)
			val instanceB = new PlantDistribution(Seq(d1b, d2b, d3b), name, threeCSpecies, 10)
			
			d1a.distanceTo(d1b) returns 1
			d2a.distanceTo(d2b) returns 2
			d3a.distanceTo(d3b) returns 3
			
			instanceA.distanceTo(instanceB) mustEqual (1+2+3)
		}
		"generate individual plants" in {
			val d1 = mock[Discrete[Chromosome]]
			val d2 = mock[Discrete[Chromosome]]
			val d3 = mock[Discrete[Chromosome]]
			val instance = new PlantDistribution(Seq(d1, d2, d3), name, threeCSpecies, 2)
			val rnd = mock[Random]
			
			val sampledC1 = mock[Chromosome]; sampledC1.size returns 4; d1.sample(rnd) returns sampledC1
			val sampledC2 = mock[Chromosome]; sampledC2.size returns 5; d2.sample(rnd) returns sampledC2
			val sampledC3 = mock[Chromosome]; sampledC3.size returns 6; d3.sample(rnd) returns sampledC3
			
			instance.sample(rnd) mustEqual Plant(
				name,
				IndexedSeq(sampledC1, sampledC2, sampledC3),
				threeCSpecies
			)
		}
		"have companion object for creating empty distribution for a cross" in {
			val cross = mock[Cross]
			cross.species returns threeCSpecies
			cross.name returns name
			
			val instance = PlantDistribution(cross)
			(instance.chromoDists.size mustEqual 3) and
			(instance.name mustEqual name)
		}
	}
}