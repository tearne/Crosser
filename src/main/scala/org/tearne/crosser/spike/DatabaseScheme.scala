///*
// * Copyright (c) 2012-13 Oliver Tearne (tearne at gmail dot com)
// * 
// * This program is free software: you can redistribute it and/or modify it under the terms of
// * the GNU General Public License as published by the Free Software Foundation, either version
// * 3 of the License, or (at your option) any later version.
// * 
// * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
// * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
// * See the GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License along with this program.  
// * If not, see <http://www.gnu.org/licenses/>.
// */
//
//package org.tearne.crosser.scheme
//
//import scala.slick.driver.PostgresDriver.simple._
//import org.tearne.crosser.plant.Species
//import org.tearne.crosser.plant.RootPlant
//
//object Test extends App{
//	val s = new DatabaseScheme(1)
//	
//	println("------")
//	println(s"Chunk size = ${s.chunkSize}")
//	println(s"Name size = ${s.name}")
//	println(s"Species = ${s.species.cMLengths}")
//	println(s.plants)
//}
//
//class DatabaseScheme(schemeId: Int) /*extends Scheme*/ {
//	implicit val session = Database.forURL("jdbc:postgresql://localhost:5432/crosser_web_develop?user=crosser_web", driver = "org.postgresql.Driver").createSession// withSession {
//	
//	case class Query(admin: DatabaseScheme.SpeciesRow, species: DatabaseScheme.SpeciesRow)
//	
//	val test = for{
//		admin <- DatabaseScheme.Admin if admin.id === schemeId
//		species <- DatabaseScheme.Species if admin.species_id === species.id
//		plant <- DatabaseScheme.Plant if plant.scheme_id === admin.id
//	} yield(admin, species, plant)
//	
//	println("-----------")
//	test.list.foreach(println)
//	println("-----------")
//	
//	val adminQ = for{
//		admin <- DatabaseScheme.Admin if admin.id === schemeId
//		//species <- DatabaseScheme.Species if admin.species_id === species.id
//	} yield(admin)//.list
//	
//	println(adminQ.first)
//	
//	val chunkSize: Int = adminQ.first.chunkSize
//	
//	val recombinationProb: Int = adminQ.first.recombProb	//TODO needs to be double
//	val tolerance: Double = adminQ.first.tolerance			//TODO needs to be double
//	
//	val name: String = adminQ.first.name
//	
//	val speciesQ = for{
//		species <- DatabaseScheme.Species if species.id === adminQ.first.speciesId
//	} yield(species)
//	
//	val plantQ = for{
//		plant <- DatabaseScheme.Plant if plant.scheme_id === adminQ.first.id
//	} yield(plant)
//	
//	println(speciesQ.first)
//	plantQ.list.foreach(println)
//	
//	val species = Species(
//			speciesQ.first.name,
//			speciesQ.first.chromosomeLengths.split(',').map(_.toInt)
//	)
//	
//	
//	val plants: Map[String, RootPlant] = {
//		plantQ.list.map{pRow =>
//			pRow.name -> new RootPlant(
//				pRow.name,
//				species
//			)
//		}.toMap
//	}
//	
//	
////	val crosses: ListMap[String, Cross]
////	
////	val outputTables: List[(Crossable, Crossable)]
//}
//
//object DatabaseScheme {
//	case class CrossRow(
//			id: Int, 
//			name: String, 
//			plantId: Int, 
//			leftRoot: Option[Int],
//			leftCross: Option[Int],
//			rightRoot: Option[Int],
//			rightCross: Option[Int],
//			zygosity: String
//	)
//	object Cross extends Table[CrossRow]("crosser_frontend_cross"){
//		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
//		def name = column[String]("name")
//		def schemeId = column[Int]("scheme_id")
//		def leftParent = column[Option[Int]]("left_plant_parent_id", O.Nullable)
//		def leftCross = column[Option[Int]]("left_cross_parent_id", O.Nullable)
//		def rightParent = column[Option[Int]]("right_plant_parent_id", O.Nullable)
//		def rightCross = column[Option[Int]]("right_cross_parent_id", O.Nullable)
//		def zygosity = column[String]("protocol_zygosity")
//		def * = id ~ name ~ schemeId ~ leftParent ~ leftCross ~ rightParent ~ rightCross ~ zygosity <> (CrossRow, CrossRow.unapply _)
//	}
//	case class CrossLociRow(id: Int, crossId: Int, locusId: Int)
//	object CrossLoci extends Table[CrossLociRow]("crosser_frontend_cross_loci"){
//		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
//		def crossId = column[Int]("cross_id")
//		def locusId = column[Int]("locus_id")
//		def * = id ~ crossId ~ locusId <> (CrossLociRow, CrossLociRow.unapply _)
//	}
//	
//	case class LocusRow(id: Int, name: String, locusType: String, linkGrp: Int, pos: Int, plantId: Int)
//	object Locus extends Table[LocusRow]("crosser_frontend_locus"){
//		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
//		def name = column[String]("name")
//		def locus_type = column[String]("locus_type")
//		def linkageGroup = column[Int]("linkageGroup")
//		def position = column[Int]("position")
//		def plant_id = column[Int]("plantId")
//		def * = id ~ name ~ locus_type ~ linkageGroup ~ position ~ plant_id <> (LocusRow, LocusRow.unapply _)
//	}	
//	
//	object Output extends Table[(Int, String, Int, Int)]("crosser_frontend_outputsubject"){
//		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
//		def contribution = column[String]("contribution")
//		def scheme_id = column[Int]("scheme_id")
//		def subject_id = column[Int]("subject_id")
//		def * = id ~ contribution ~ scheme_id ~ subject_id
//	}
//	
//	case class PlantRow(id: Int, name: String, scheme_id: Int)
//	object Plant extends Table[PlantRow]("crosser_frontend_plant"){
//		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
//		def name = column[String]("name")
//		def scheme_id = column[Int]("scheme_id")
//		def * = id ~ name ~ scheme_id <> (PlantRow, PlantRow.unapply _)
//	}
//	
//	//TODO tolerance should be double
//	//TODO recombProb should be double
//	//TODO Change Scheme to something else like 'admin'
//	case class AdminRow(id: Int, chunkSize: Int, recombProb: Int, tolerance: Int, name: String, speciesId: Int)
//	object Admin extends Table[AdminRow]("crosser_frontend_scheme"){
//		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
//		def conf_chunk_size = column[Int]("conf_chunk_size")
//		def conf_recombination_prob = column[Int]("conf_recombination_prob")
//		def conf_tolerance = column[Int]("conf_tolerance")
//		def name = column[String]("name")
//		def species_id = column[Int]("species_id")
//		def * = id ~ conf_chunk_size ~ conf_recombination_prob ~ conf_tolerance ~ name ~ species_id <> (AdminRow, AdminRow.unapply _)
//	}
//	
//	case class SpeciesRow(id: Int, name: String, chromosomeLengths: String)
//	object Species extends Table[SpeciesRow]("crosser_frontend_species"){
//		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
//		def name = column[String]("name")
//		def chromosome_lengths = column[String]("chromosome_lengths")
//		def * = id ~ name ~ chromosome_lengths <> (SpeciesRow, SpeciesRow.unapply _)
//	}
//}