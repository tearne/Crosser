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
import com.typesafe.config.{ConfigFactory => TypesafeConfigFactory}
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test

class HumanConfigTest extends AssertionsForJUnit {
	trait Setup{
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
	}
	
	@Test def name {new Setup{
		assert(scheme.name === "MyCross")
	}}
	
	@Test def rootPlants { new Setup {
		assert(rootPlants === scheme.plants)
	}}
	
	@Test def correctCrosses { new Setup {
		val crosses: Map[String, Cross] = scheme.crosses
		assert(crosses.size === 3)
		assert(crosses("BC1") === bc1)
		assert(crosses("Self") === self)
	}}
	
	@Test def convergenceDetails { new Setup {
		assert(scheme.chunkSize === 100)
		assert(scheme.tolerance === 0.05)
		assert(scheme.fewestPlants === 100)
	}}
	
	@Test def requiredOutputs { new Setup {
		 val expected = List[Output](
			ProportionDistribution(bc1, prefVar),
			ProportionDistribution(self, prefVar),
			SuccessProbability(Seq(f1, bc1, self)),
			LociComposition(self, rootPlants.values.toSeq),
			MeanCrossComposition(Seq(f1, bc1, self), Seq(prefVar, donor1, donor2))
		)
		assert(scheme.outputs === expected)
	}}
}