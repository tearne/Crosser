package org.tearne.crosser.scheme

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

@RunWith(classOf[JUnitRunner])
class ConfigSchemeSpec extends Specification {
	
	val path = Paths.get("src/test/resource/schemeTest.config")
	Files.exists(path) must beTrue
	val scheme = new ConfigScheme(path)
	
	val species: Species = Species("Phaseolus_Vulgaris", IndexedSeq(11,23,45,22,10,80,121))
	val prefVar = RootPlant("Prefered_Variety", species)
	
	val donor1 = RootPlant("Donor1", species)
	val locus1_1 = Locus(donor1, 5, 50, "D1C1")
	val locus1_2 = Locus(donor1, 6, 60, "D1C2")
	
	val donor2 = RootPlant("Donor2", species)
	val locus2 = Locus(donor2, 1, 10, "D2C1")
	
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
		
		"have correct crosses" in {
			val crosses: Map[String, Cross] = scheme.crosses
			(crosses.size must_== 3) and
			(crosses("BC1") must_== bc1)
			(crosses("Self") must_== self)
		}
		
		"specify db url" in {
			scheme.dbURL must_== "jdbc:etc"
		}
		
		"specify chunk size" in {
			scheme.chunkSize must_== 100
		}
		
		"specify distribution tolerance" in {
			scheme.tolerance must_== 0.05
		}
	}
}