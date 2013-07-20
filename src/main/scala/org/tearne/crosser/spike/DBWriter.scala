//package org.tearne.crosser.spike
//
//import org.tearne.crosser.scheme.Scheme
//import scala.slick.driver.ExtendedProfile
//import org.tearne.crosser.distribution.components.PlantDistribution
//import scala.slick.session.Database
//import scala.slick.session.Session
//
//class DBWriter(config: Scheme, sessionId: Int) {
//	def newInstance[T](name: String)(implicit m: Manifest[T]): T =
//      Class.forName(name + "$").getField("MODULE$").get(m.runtimeClass).asInstanceOf[T]
////	
////	val driver = "org.h2.Driver"
////	val profile = "scala.slick.driver.H2Driver"
////	//val url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
////	val url = "jdbc:h2:crosserDB"
//		
//    val ep: ExtendedProfile =  newInstance[ExtendedProfile](config.dbProfile)
//    
//    val dao = new DAO(ep)
//	val database = Database.forURL(config.dbURL, driver = config.dbDriver)
//	
//	def writeDistribution(pDist: PlantDistribution){
//		val crossLoci = for{
//				(cDist, cId) <- pDist.chromoDists.zipWithIndex
//				(cSample, realisationId) <- cDist.samples.zipWithIndex
//				(tid, isLeft) <- (cSample.left, true) :: (cSample.right, false) :: Nil
//				(cM, cMId) <- tid.alleles.zipWithIndex
//		} yield {
//			CrossLoci(
//				sessionId, 
//				pDist.name, 
//				cId, 
//				realisationId, 
//				cMId, 
//				isLeft, 
//				cM.name.hashCode())
//		}
//		
//		println(crossLoci.size+" rows to write")
//		//crossLoci.foreach(println)
//		
//		val a = crossLoci.groupBy(identity).filter{case (k,v) => v.size > 1}
//		println(a)
//		
//		database.withSession{implicit session: Session =>
//			dao.write(crossLoci)
//		}
//	}
//	
//	case class CrossLoci(sessionId: Int, crossName: String, chromosomeId: Int, realisationId: Int, cMId: Int, isLeftSide: Boolean, geneName: Int)
//	
//	class DAO(val profile: ExtendedProfile){
//		import profile.simple._
//		
//		val sessionId = 1
//		
//		
//		def write(crossLoci: Seq[CrossLoci])(implicit session: Session){
//			CrossLociTable.ddl.create
//			CrossLociTable.insertAll(crossLoci: _*)
//			val q = Query(CrossLociTable)
//			
//			println(q.list.size + "records")
//		}
//		
//		object CrossLociTable extends Table[CrossLoci]("cross_loci"){
//			def sessionId = column[Int]("session_id")
//			def crossName = column[String]("cross_name")
//			def chromosomeId = column[Int]("chromosome_id")
//			def realisationId = column[Int]("realisation_id")
//			def cMId = column[Int]("centimorgan_id")
//			def isLeftSide = column[Boolean]("is_left_side")
//			def geneName = column[Int]("gene_name")
//			
//			def pk = primaryKey("pk", (sessionId, crossName, chromosomeId, realisationId, cMId, isLeftSide))
//			
//			def * = sessionId ~ crossName ~ chromosomeId ~ realisationId ~ cMId ~ isLeftSide ~ geneName <> (CrossLoci, CrossLoci.unapply _)
//		}
//	}
//}
//
//
