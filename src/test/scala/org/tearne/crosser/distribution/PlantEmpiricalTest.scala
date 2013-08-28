package org.tearne.crosser.distribution

import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.Species
import org.tearne.crosser.cross.Cross
import sampler.math.Random
import sampler.data.Samplable
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.mock.MockitoSugar
import org.junit.Test
import org.mockito.Mockito._
import sampler.data.Empirical

class PlantEmpiricalSpec extends AssertionsForJUnit with MockitoSugar{
	val name = "plantName"
	val threeCSpecies =  Species("mySpecies", 4,5,6)
	
	def mockChromosome(size: Int) = {
		val chrom = mock[Chromosome]
		when(chrom.size).thenReturn(size)
		chrom
	}

	@Test def getPlantSamplable{
		implicit val random = mock[Random]

		val chrom1Dist = mock[ChromosomeEmpirical] 
		val samp1: Samplable[Chromosome] = mock[Samplable[Chromosome]]
		when(chrom1Dist.toSamplable).thenReturn(samp1)

		val chrom2Dist = mock[ChromosomeEmpirical] 
		val samp2 = mock[Samplable[Chromosome]]
		when(chrom2Dist.toSamplable).thenReturn(samp2)
		
		val chrom3Dist = mock[ChromosomeEmpirical] 
		val samp3 = mock[Samplable[Chromosome]]
		when(chrom3Dist.toSamplable).thenReturn(samp3)
		
		val chrom1samp1 = mockChromosome(4)
		val chrom1samp2 = mockChromosome(4)
		when(samp1.sample).thenReturn(chrom1samp1, chrom1samp2)
		
		val chrom2samp1 = mockChromosome(5)
		val chrom2samp2 = mockChromosome(5)
		when(samp2.sample).thenReturn(chrom2samp1, chrom2samp2)
		
		val chrom3samp1 = mockChromosome(6)
		val chrom3samp2 = mockChromosome(6)
		when(samp3.sample).thenReturn(chrom3samp1, chrom3samp2)

		val instance = new PlantEmpirical(IndexedSeq(chrom1Dist, chrom2Dist, chrom3Dist), name, threeCSpecies, 2)
		val samplable: Samplable[Plant] = instance.toSamplable
		
		assert(samplable.sample === Plant(name, IndexedSeq(chrom1samp1, chrom2samp1, chrom3samp1), threeCSpecies))
		assert(samplable.sample === Plant(name, IndexedSeq(chrom1samp2, chrom2samp2, chrom3samp2), threeCSpecies))
	}
	
	@Test def exceptionIfSpeciesInconsistentWithNumChromosomes{
		val chromoDists = Vector(mock[ChromosomeEmpirical], mock[ChromosomeEmpirical])//Two chromosomes
		intercept[PlantEmpiricalException]{
			new PlantEmpirical(chromoDists, "myPlant", threeCSpecies, 0)
		}
	}
	
	@Test def exceptionIfChromosomesDontContainSameNumberOfObservations{
		val dist1 = mock[ChromosomeEmpirical]
		when(dist1.size).thenReturn(90)
		val dist2 = mock[ChromosomeEmpirical]
		when(dist2.size).thenReturn(89)
		val chromoDists = IndexedSeq(dist1, dist1, dist2)
		
		intercept[PlantEmpiricalException]{
			new PlantEmpirical(chromoDists, name, threeCSpecies, 10)
		}
	}
	
	@Test def calculateNonZeroSuccessProbability{
		val dist = mock[ChromosomeEmpirical]
		when(dist.size).thenReturn(90)
		val chromoDists = IndexedSeq(dist, dist, dist)
		
		val instance = new PlantEmpirical(chromoDists, null, threeCSpecies, 10)
		assert(instance.successProbability === 0.9)
	}
	
	@Test def calculateZeroSuccessProbability{
		val dist = mock[ChromosomeEmpirical]
		when(dist.size).thenReturn(0)
		val chromoDists = IndexedSeq(dist, dist, dist)
	
		val instance = new PlantEmpirical(chromoDists, null, threeCSpecies, 0)
		assert(instance.successProbability === 0)
	}
	
	@Test def addingSamples{
		//These chromosomes will already be in the plant distribution instance
		val c0_1 = mock[Chromosome]; val c0_2 = mock[Chromosome]; val c0_3 = mock[Chromosome]
		val d1 = new ChromosomeEmpirical(IndexedSeq(c0_1))
		val d2 = new ChromosomeEmpirical(IndexedSeq(c0_2))
		val d3 = new ChromosomeEmpirical(IndexedSeq(c0_3))
		val instance = new PlantEmpirical(IndexedSeq(d1,d2,d3), name, threeCSpecies, 10)
		
		val c1_1 = mock[Chromosome]; val c1_2 = mock[Chromosome]; val c1_3 = mock[Chromosome]
		val p1 = mock[ConcretePlant]; when(p1.chromosomes).thenReturn(IndexedSeq(c1_1, c1_2, c1_3))
		val c2_1 = mock[Chromosome]; val c2_2 = mock[Chromosome]; val c2_3 = mock[Chromosome]
		val p2 = mock[ConcretePlant]; when(p2.chromosomes).thenReturn(IndexedSeq(c2_1, c2_2, c2_3))
		val c3_1 = mock[Chromosome]; val c3_2 = mock[Chromosome]; val c3_3 = mock[Chromosome]
		val p3 = mock[ConcretePlant]; when(p3.chromosomes).thenReturn(IndexedSeq(c3_1, c3_2, c3_3))
		
		val result = instance ++(Seq(p1,p2,p3), 2)
		
		val resultChromoDist1: ChromosomeEmpirical = result.chromoDistSeq(0)
		val resultChromoDist2: ChromosomeEmpirical = result.chromoDistSeq(1)
		val resultChromoDist3: ChromosomeEmpirical = result.chromoDistSeq(2)
		
		val t: Set[Chromosome] = resultChromoDist1.probabilityTable.keySet
		
		assert(resultChromoDist1.probabilityTable.keySet.subsetOf(Set(c0_1, c1_1, c2_1, c3_1)))
		assert(resultChromoDist2.probabilityTable.keySet.subsetOf(Set(c0_2, c1_2, c2_2, c3_2)))
		assert(resultChromoDist3.probabilityTable.keySet.subsetOf(Set(c0_3, c1_3, c2_3, c3_3)))
	}
	
	@Test def calculateNumSamplesAndFailures{
		val c = mock[Chromosome]
		val d1 = new ChromosomeEmpirical(IndexedSeq(c,c,c,c))
		val d2 = new ChromosomeEmpirical(IndexedSeq(c,c,c,c))
		val d3 = new ChromosomeEmpirical(IndexedSeq(c,c,c,c))
		val instance = new PlantEmpirical(IndexedSeq(d1,d2,d3), name, threeCSpecies, 10)
		
		assert(instance.numSamples === 14)
		assert(instance.numFailures === 10)
		assert(instance.numSuccess === 4)
	}
	
	@Test def generatePlantsFromSamplable {
		implicit val random = mock[Random]
		val ce1 = mock[ChromosomeEmpirical] 
		val c1 = mockChromosome(4)
		val cs1 = Samplable.diracDelta(c1) 
		
		val ce2 = mock[ChromosomeEmpirical]; 
		val c2 = mockChromosome(5)
		val cs2 = Samplable.diracDelta(c2)
		
		val ce3 = mock[ChromosomeEmpirical]; 
		val c3 = mockChromosome(6)
		val cs3 = Samplable.diracDelta(c3)
		
		when(ce1.toSamplable(random)).thenReturn(cs1)
		when(ce2.toSamplable(random)).thenReturn(cs2)
		when(ce3.toSamplable(random)).thenReturn(cs3)
		
		val instance = new PlantEmpirical(IndexedSeq(ce1, ce2, ce3), name, threeCSpecies, 2)
		val samplable = instance.toSamplable
		
		assert(samplable.sample === Plant(
			name,
			IndexedSeq(c1, c2, c3),
			threeCSpecies
		))
	}
	
	@Test def initialisingEmptyDistribution {
		val cross = mock[Cross]
		when(cross.species).thenReturn(threeCSpecies)
		when(cross.name).thenReturn(name)
		
		val instance = PlantEmpirical(cross)
		assert(instance.chromoDistSeq.size === 3) 
		assert(instance.name === name)
	}
	
//	"PlantDistribution" should {
//		"be samplable" in{
//			mock[PlantDistribution] must beAnInstanceOf[Samplable[ConcretePlant]]
//		}
//		"throw exception if species inconsistent with distributions" in{
//			val chromoDists = Vector(mock[ChromosomeDistribution], mock[ChromosomeDistribution])//Two chromosomes
//			
//			new PlantDistribution(chromoDists, "myPlant", threeCSpecies, 0) must throwA[PlantDistributionException]
//		}
//		"throw exception if inconsistent number of observations across chromosomes" in todo
//		"determine non-zero success probability given inputs" in {
//			val dist = mock[ChromosomeDistribution]
//			dist.size returns 90
//			val chromoDists = IndexedSeq(dist, dist, dist)
//			
//			val instance = new PlantDistribution(chromoDists, null, threeCSpecies, 10)
//			instance.successProbability mustEqual 0.9
//		}
//		"determine zero success probability given inputs" in {
//			val dist = mock[ChromosomeDistribution]
//			dist.size returns 0
//			val chromoDists = IndexedSeq(dist, dist, dist)
//			
//			val instance = new PlantDistribution(chromoDists, null, threeCSpecies, 0)
//			instance.successProbability mustEqual 0
//		}
//		"have ++ to generate augmented distribution" in {
//			//These are the chromosomes which will are already in the plant distribution instance
//			val c0_1 = mock[Chromosome]; val c0_2 = mock[Chromosome]; val c0_3 = mock[Chromosome]
//			val d1 = new ChromosomeDistribution(IndexedSeq(c0_1))
//			val d2 = new ChromosomeDistribution(IndexedSeq(c0_2))
//			val d3 = new ChromosomeDistribution(IndexedSeq(c0_3))
//			val instance = new PlantDistribution(IndexedSeq(d1,d2,d3), name, threeCSpecies, 10)
//			
//			val c1_1 = mock[Chromosome]; val c1_2 = mock[Chromosome]; val c1_3 = mock[Chromosome]
//			val p1 = mock[ConcretePlant]; p1.chromosomes returns IndexedSeq(c1_1, c1_2, c1_3)
//			val c2_1 = mock[Chromosome]; val c2_2 = mock[Chromosome]; val c2_3 = mock[Chromosome]
//			val p2 = mock[ConcretePlant]; p2.chromosomes returns IndexedSeq(c2_1, c2_2, c2_3)
//			val c3_1 = mock[Chromosome]; val c3_2 = mock[Chromosome]; val c3_3 = mock[Chromosome]
//			val p3 = mock[ConcretePlant]; p3.chromosomes returns IndexedSeq(c3_1, c3_2, c3_3)
//			
//			val result = instance ++(Seq(p1,p2,p3), 2)
//			
//			val resultD1 = result.chromoDists(0)
//			val resultD2 = result.chromoDists(1)
//			val resultD3 = result.chromoDists(2)
//			
//			(resultD1.samples must containAllOf(Seq(c0_1, c1_1, c2_1, c3_1))) and
//			(resultD2.samples must containAllOf(Seq(c0_2, c1_2, c2_2, c3_2))) and
//			(resultD3.samples must containAllOf(Seq(c0_3, c1_3, c2_3, c3_3)))
//		}
//		"accept ++ with Nil new samples" in todo
//		"know how many samples it contains, including failures" in {
//			val c = mock[Chromosome]
//			val d1 = new ChromosomeDistribution(IndexedSeq(c,c,c,c))
//			val d2 = new ChromosomeDistribution(IndexedSeq(c,c,c,c))
//			val d3 = new ChromosomeDistribution(IndexedSeq(c,c,c,c))
//			val instance = new PlantDistribution(IndexedSeq(d1,d2,d3), name, threeCSpecies, 10)
//			
//			instance.numSamples === 14
//		}
//		"build samplable" in{
//			val random = mock[Random]
//			
//			
//			val d1 = mock[ChromosomeDistribution]; d1.toSamplable(random) returns 
//			val d2 = mock[ChromosomeDistribution]
//			val d3 = mock[ChromosomeDistribution]
//			val instance = new PlantDistribution(IndexedSeq(d1, d2, d3), name, threeCSpecies, 2)
//			
//			instance.toSamplable(random)
//		}
//		
//		"generate individual plants" in {
//			val d1 = mock[ChromosomeDistribution]
//			val d2 = mock[ChromosomeDistribution]
//			val d3 = mock[ChromosomeDistribution]
//			val instance = new PlantDistribution(IndexedSeq(d1, d2, d3), name, threeCSpecies, 2)
//			
//			val sampledC1 = mock[Chromosome]; sampledC1.size returns 4; d1.sample returns sampledC1
//			val sampledC2 = mock[Chromosome]; sampledC2.size returns 5; d2.sample returns sampledC2
//			val sampledC3 = mock[Chromosome]; sampledC3.size returns 6; d3.sample returns sampledC3
//			
//			instance.sample mustEqual Plant(
//				name,
//				IndexedSeq(sampledC1, sampledC2, sampledC3),
//				threeCSpecies
//			)
//		}
//		"have companion object for creating empty distribution for a cross" in {
//			val cross = mock[Cross]
//			cross.species returns threeCSpecies
//			cross.name returns name
//			
//			val instance = PlantDistribution(cross)
//			(instance.chromoDists.size mustEqual 3) and
//			(instance.name mustEqual name)
//		}
//	}
}