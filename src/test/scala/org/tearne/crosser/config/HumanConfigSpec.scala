package org.tearne.crosser.config

import org.specs2.mutable._
import org.specs2.specification.Scope
import java.nio.file.Paths
import org.specs2.matcher.TraversableBaseMatchers
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import java.io.File
import java.nio.file.Files

import org.tearne.crosser.cross._
import org.tearne.crosser.plant.Species
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.output._

@RunWith(classOf[JUnitRunner])
class HumanConfigSpec extends Specification {
	
	val path = Paths.get("src/test/resource/testHuman.config")
	Files.exists(path) must beTrue
	val scheme: Config = new HumanConfig(path)
	
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
	
	"Example scheme" should {
		"have a name" in { 
			scheme.name must_== "MyCross"
		}
		
		"have root plants" in {
//			val rootPlants = scheme.plants
			rootPlants must_== scheme.plants
//			(rootPlants.size must_== 3) and
//			(rootPlants("Prefered_Variety") must_== prefVar) and
//			(rootPlants("Donor1") must_== donor1) and
//			(rootPlants("Donor2") must_== donor2)
		}
		
		"have correct crosses" in {
			val crosses: Map[String, Cross] = scheme.crosses
			(crosses.size must_== 3) and
			(crosses("BC1") must_== bc1) and
			(crosses("Self") must_== self)
		}
		
//		"specify db details" in {
//			(scheme.dbURL must_== "jdbc:etc") and
//			(scheme.dbProfile must_== "scala.slick.driver.AwesomeDB") and
//			(scheme.dbDriver must_== "org.database.AwesomeDriver")
//		}
		
		"specify convergence details size" in {
			(scheme.chunkSize must_== 100) and
			(scheme.tolerance must_== 0.05) and
			(scheme.fewestPlants must_== 100)
		}
		
		"list required outputs" in {
			 val expected = List[Output](
				ProportionDistribution(bc1, prefVar),
				ProportionDistribution(self, prefVar),
				SuccessProbability(Seq(f1, bc1, self)),
				LociComposition(self, rootPlants.values.toSeq),
				MeanCrossComposition(Seq(f1, bc1, self), Seq(prefVar, donor1, donor2))
			)
			scheme.outputs must_== expected
		}
	}
}