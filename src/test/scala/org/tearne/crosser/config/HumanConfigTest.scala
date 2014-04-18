package org.tearne.crosser.config

import java.nio.file.Paths
import org.junit.runner.RunWith
import java.io.File
import java.nio.file.Files
import org.tearne.crosser.cross._
import org.tearne.crosser.plant.Species
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.output._
import com.typesafe.config.{ConfigFactory => TypesafeConfigFactory}
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import org.scalatest.FreeSpec

class HumanConfigTest extends FreeSpec {
	
	"HumanConfig should read" - {
		val path = Paths.get("src/test/resource/testHuman.config")
		val scheme: Config = new HumanConfig(TypesafeConfigFactory.parseFile(path.toFile()))
		
		val species: Species = Species("Phaseolus_Vulgaris", IndexedSeq(11,23,45,22,10,80,121))
		val prefVar = RootPlant("Prefered_Variety", species)
		
		val donor1 = RootPlant("Donor1", species)
		val locus1_1 = Locus(donor1, 5, 50, "D1C1")
		val locus1_2 = Locus(donor1, 6, 60, "D1C2")
		
		val donor2 = RootPlant("Donor2", species)
		val locus2 = Locus(donor2, 1, 10, "D2C1")
		
		val rootPlants = Map(
			"Prefered_Variety" -> prefVar,
			"Donor1" -> donor1,
			"Donor2" -> donor2
		)
		
		val f1 = Cross(
			donor1,
			donor2,
			HeterozygousProtocol(Set(locus1_1, locus1_2, locus2)),
			"F1"
		)
		
		val bc1 = Cross(
			f1,
			prefVar,
			HeterozygousProtocol(Set(locus1_1, locus1_2, locus2), Some(1)),
			"BC1"
		)
		
		val self = Cross(
			bc1,
			bc1,
			HomozygousProtocol(Set(locus1_1, locus1_2, locus2)),
			"Self"
		)
		
		"Cross name" in {
			assertResult("MyCross")(scheme.name)
		}
		
		"Root plants" in {
			assertResult(rootPlants)(scheme.plants)
		}
		
		"Cross configurations" in {
			val crosses: Map[String, Cross] = scheme.crosses
			assertResult(3)(crosses.size)
			assertResult(f1)(crosses("F1"))
			assertResult(bc1)(crosses("BC1"))
			assertResult(self)(crosses("Self"))
		}
		
		"Convergence settings" in {
			assertResult(100)(scheme.chunkSize)
			assertResult(0.05)(scheme.tolerance)
			assertResult(100)(scheme.fewestPlants)
		}
		
		"Required outputs" in {
			val expected = List[Output](
			ProportionDistribution(bc1, prefVar),
			ProportionDistribution(self, prefVar),
			SuccessTable(Seq(
					(f1, 20, 0.9), 
					(bc1, 30, 0.95), 
					(self, 40, 0.98)
					)),
					LociComposition(self, rootPlants.values.toSeq),
					MeanCrossComposition(Seq(f1, bc1, self), Seq(prefVar, donor1, donor2))
			)
			
			assertResult(expected)(scheme.outputs)
		}
	}
}