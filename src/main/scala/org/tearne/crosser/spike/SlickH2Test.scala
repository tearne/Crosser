//package org.tearne.crosser.spike
//
////import scala.slick.session.Database
////import Database.threadLocalSession
//import scala.slick.driver.ExtendedProfile
//
//object SlickH2Test extends App {
//	def newInstance[T](name: String)(implicit m: Manifest[T]): T =
//      Class.forName(name + "$").getField("MODULE$").get(m.runtimeClass).asInstanceOf[T]
//	
//	val driver = "org.h2.Driver"
//	val profile = "scala.slick.driver.H2Driver"
//	//val url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
//	val url = "jdbc:h2:crosserDB"
//		
//    val ep: ExtendedProfile =  newInstance[ExtendedProfile](driver)
//    import ep.simple._
//    
//	Database.forURL(url, driver = driver) withSession { implicit session: Session =>
//		
////		new DAO()
//		CrossLociTable.ddl.create
//		
//		val data = List(
//			CrossLoci(1, 1, "f1", 3, 1, true, "donor1"),
//			CrossLoci(1, 1, "f1", 3, 1, false, "donor1"),
//			CrossLoci(1, 1, "f1", 3, 2, true, "donor1"),
//			CrossLoci(1, 1, "f1", 3, 2, false, "donor1"),
//			CrossLoci(1, 2, "f1", 3, 2, true, "donor2"),
//			CrossLoci(1, 1, "f1", 3, 3, false, "donor1")
//		)
//		
//		CrossLociTable.insertAll(data: _*)
//		
//		val q = Query(CrossLociTable)
//		
//		println(q.selectStatement)
//		q.foreach(println)
//	}
//
//	case class CrossLoci(sessionId: Int, realisationId: Int, crossName: String, chromosomeId: Int, cMId: Int, isLeftSide: Boolean, geneName: String)
//	
//	object CrossLociTable extends Table[CrossLoci]("cross_loci"){
//		def sessionId = column[Int]("session_id")
//		def realisationId = column[Int]("realisation_id")
//		def crossName = column[String]("cross_name")
//		def chromosomeId = column[Int]("chromosome_id")
//		def cMId = column[Int]("centimorgan_id")
//		def isLeftSide = column[Boolean]("is_left_side")
//		def geneName = column[String]("gene_name")
//		
//		def pk = primaryKey("pk", (sessionId, realisationId, chromosomeId, cMId, isLeftSide))
//		
//		def * = sessionId ~ realisationId ~ crossName ~ chromosomeId ~ cMId ~ isLeftSide ~ geneName <> (CrossLoci, CrossLoci.unapply _)
//	}
//}