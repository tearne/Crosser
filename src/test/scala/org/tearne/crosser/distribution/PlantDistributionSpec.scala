package org.tearne.crosser.distribution

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.Species
import org.tearne.crosser.cross.Cross
import sampler.math.Random
import sampler.data.Samplable
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlantDistributionSpec extends Specification with Mockito{
	val name = "plantName"
	val threeCSpecies =  Species("mySpecies", 4,5,6)
	implicit val r: Random = null
	
	"PlantDistribution" should {
		"be samplable" in{
			mock[PlantDistribution] must beAnInstanceOf[Samplable[ConcretePlant]]
		}
		"throw exception if species inconsistent with distributions" in{
			val chromoDists = Vector(mock[ChromosomeDistribution], mock[ChromosomeDistribution])//Two chromosomes
			
			new PlantDistribution(chromoDists, "myPlant", threeCSpecies, 0) must throwA[PlantDistributionException]
		}
		"throw exception if inconsistent number of observations across chromosomes" in todo
		"determine non-zero success probability given inputs" in {
			val dist = mock[ChromosomeDistribution]
			dist.size returns 90
			val chromoDists = IndexedSeq(dist, dist, dist)
			
			val instance = new PlantDistribution(chromoDists, null, threeCSpecies, 10)
			instance.successProbability mustEqual 0.9
		}
		"determine zero success probability given inputs" in {
			val dist = mock[ChromosomeDistribution]
			dist.size returns 0
			val chromoDists = IndexedSeq(dist, dist, dist)
			
			val instance = new PlantDistribution(chromoDists, null, threeCSpecies, 0)
			instance.successProbability mustEqual 0
		}
		"have ++ to generate augmented distribution" in {
			//These are the chromosomes which will are already in the plant distribution instance
			val c0_1 = mock[Chromosome]; val c0_2 = mock[Chromosome]; val c0_3 = mock[Chromosome]
			val d1 = new ChromosomeDistribution(IndexedSeq(c0_1))
			val d2 = new ChromosomeDistribution(IndexedSeq(c0_2))
			val d3 = new ChromosomeDistribution(IndexedSeq(c0_3))
			val instance = new PlantDistribution(IndexedSeq(d1,d2,d3), name, threeCSpecies, 10)
			
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
			
			(resultD1.samples must containAllOf(Seq(c0_1, c1_1, c2_1, c3_1))) and
			(resultD2.samples must containAllOf(Seq(c0_2, c1_2, c2_2, c3_2))) and
			(resultD3.samples must containAllOf(Seq(c0_3, c1_3, c2_3, c3_3)))
		}
		"accept ++ with Nil new samples" in todo
		"know how many samples it contains, including failures" in {
			val c = mock[Chromosome]
			val d1 = new ChromosomeDistribution(IndexedSeq(c,c,c,c))
			val d2 = new ChromosomeDistribution(IndexedSeq(c,c,c,c))
			val d3 = new ChromosomeDistribution(IndexedSeq(c,c,c,c))
			val instance = new PlantDistribution(IndexedSeq(d1,d2,d3), name, threeCSpecies, 10)
			
			instance.numSamples === 14
		}
		"generate individual plants" in {
			val d1 = mock[ChromosomeDistribution]
			val d2 = mock[ChromosomeDistribution]
			val d3 = mock[ChromosomeDistribution]
			val instance = new PlantDistribution(IndexedSeq(d1, d2, d3), name, threeCSpecies, 2)
			
			val sampledC1 = mock[Chromosome]; sampledC1.size returns 4; d1.sample returns sampledC1
			val sampledC2 = mock[Chromosome]; sampledC2.size returns 5; d2.sample returns sampledC2
			val sampledC3 = mock[Chromosome]; sampledC3.size returns 6; d3.sample returns sampledC3
			
			instance.sample mustEqual Plant(
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