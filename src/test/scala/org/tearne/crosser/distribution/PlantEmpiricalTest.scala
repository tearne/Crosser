package org.tearne.crosser.distribution

import org.tearne.crosser.plant.Chromosome
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.Species
import org.tearne.crosser.cross.Cross
import sampler.math.Random
import sampler.data.Samplable
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import sampler.data.Empirical
import sampler.data.Distribution
import org.scalatest.FreeSpec

class PlantEmpiricalTest extends FreeSpec with MockitoSugar{
	val name = "plantName"
	val threeCSpecies =  Species("mySpecies", 4,5,6)
	
	def mockChromosome(size: Int) = {
		val chrom = mock[Chromosome]
		when(chrom.size).thenReturn(size)
		chrom
	}

	"PlantEmpirical should" - {
		"Generate plants from supplied samplable" in {
			implicit val random = mock[Random]
			val ce1 = mock[ChromosomeEmpirical] 
			val c1 = mockChromosome(4)
			val cs1 = Distribution.continually(c1) 
			
			val ce2 = mock[ChromosomeEmpirical]; 
			val c2 = mockChromosome(5)
			val cs2 = Distribution.continually(c2)
			
			val ce3 = mock[ChromosomeEmpirical]; 
			val c3 = mockChromosome(6)
			val cs3 = Distribution.continually(c3)
			
			when(ce1.toDistribution(random)).thenReturn(cs1)
			when(ce2.toDistribution(random)).thenReturn(cs2)
			when(ce3.toDistribution(random)).thenReturn(cs3)
			
			val instance = new PlantEmpirical(IndexedSeq(ce1, ce2, ce3), name, threeCSpecies, 2)
			val samplable = instance.toDistribution
			
			assert(samplable.sample === Plant(
				name,
				IndexedSeq(c1, c2, c3),
				threeCSpecies
			))
		}
		
		"Throw exception if species inconsistent with num chromosomes" in {
			val chromoDists = Vector(mock[ChromosomeEmpirical], mock[ChromosomeEmpirical])//Two chromosomes
			intercept[PlantEmpiricalException]{
				new PlantEmpirical(chromoDists, "myPlant", threeCSpecies, 0)
			}
		}
		
		"Throw exception if chromosomes dont contain same number of observations" in {	
			val dist1 = mock[ChromosomeEmpirical]
			when(dist1.size).thenReturn(90)
			val dist2 = mock[ChromosomeEmpirical]
			when(dist2.size).thenReturn(89)
			val chromoDists = IndexedSeq(dist1, dist1, dist2)
			
			intercept[PlantEmpiricalException]{
				new PlantEmpirical(chromoDists, name, threeCSpecies, 10)
			}
		}
		
		"Calculate non-zero success probability" in {
			val dist = mock[ChromosomeEmpirical]
			when(dist.size).thenReturn(90)
			val chromoDists = IndexedSeq(dist, dist, dist)
			
			val instance = new PlantEmpirical(chromoDists, null, threeCSpecies, 10)
			assert(instance.successProbability === 0.9)
		}
		
		"Calculate zero success probability" in {
			val dist = mock[ChromosomeEmpirical]
			when(dist.size).thenReturn(0)
			val chromoDists = IndexedSeq(dist, dist, dist)
		
			val instance = new PlantEmpirical(chromoDists, null, threeCSpecies, 0)
			assert(instance.successProbability === 0)
		}
		
		"Support adding of samples" in {
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
		
		"Calculate number of samples and failures" in {
			val c = mock[Chromosome]
			val d1 = new ChromosomeEmpirical(IndexedSeq(c,c,c,c))
			val d2 = new ChromosomeEmpirical(IndexedSeq(c,c,c,c))
			val d3 = new ChromosomeEmpirical(IndexedSeq(c,c,c,c))
			val instance = new PlantEmpirical(IndexedSeq(d1,d2,d3), name, threeCSpecies, 10)
			
			assert(instance.numSamples === 14)
			assert(instance.numFailures === 10)
			assert(instance.numSuccess === 4)
		}
		
		"Initialise from a cross" in {
			val cross = mock[Cross]
			when(cross.species).thenReturn(threeCSpecies)
			when(cross.name).thenReturn(name)
			
			val instance = PlantEmpirical(cross)
			assert(instance.chromoDistSeq.size === 3) 
			assert(instance.name === name)
		}
	}
}