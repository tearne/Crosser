package org.tearne.crosser.spike

import scala.slick.session.Database
// Use H2Driver to connect to an H2 database
import scala.slick.driver.PostgresDriver.simple._

// Use the implicit threadLocalSession
import Database.threadLocalSession

object SlickTest extends App {
	Database.forURL("jdbc:postgresql://localhost:5432/crosser_web_develop?user=crosser_web", driver = "org.postgresql.Driver") withSession {
		val q = for{
			c <- Crosses
		} yield (c)
		
		println(q.selectStatement)
		q.foreach(println)
	}

	object Crosses extends Table[(Int, String, Int, Option[Int], Option[Int], Option[Int], Option[Int], String)]("crosser_frontend_cross"){
		def id = column[Int]("id", O.PrimaryKey)
		def name = column[String]("name")
		def planId = column[Int]("plan_id")
		def leftParent = column[Option[Int]]("left_plant_parent_id", O.Nullable)
		def leftCross = column[Option[Int]]("left_cross_parent_id", O.Nullable)
		def rightParent = column[Option[Int]]("right_plant_parent_id", O.Nullable)
		def rightCross = column[Option[Int]]("right_cross_parent_id", O.Nullable)
		def zygosity = column[String]("protocol_zygosity")
		def * = id ~ name ~ planId ~ leftParent ~ leftCross ~ rightParent ~ rightCross ~ zygosity
	}
}