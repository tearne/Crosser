package org.tearne.crosser.output

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant
import sampler.math.StatisticsComponent
import org.tearne.crosser.plant.Species
import org.tearne.crosser.Services
import sampler.io.CSV
import sampler.Implicits._

trait Output{
	val name: String
	val fileName: String
	def buildCSVLines(services: Services): Seq[Seq[Any]]
}

case class ProportionDistribution(cross: Cross, donor: RootPlant) extends Output{
	val name = s"${donor.name}_in_${cross.name}"
	val fileName = name + ".density"
	def buildCSVLines(services: Services): Seq[Seq[Any]] = {
		val dist = services.crossingService.getDistribution(cross).map(_.alleleCount(donor).proportion)
		val proportions = services.statisticsService.gatherSamples(dist)
		(name +: proportions).map(Seq(_))
	}
}
case class SuccessTable(requirements : Seq[Tuple3[Cross, Int, Double]]) extends Output{
	val name = "SuccessTable"
	val fileName = name
	def buildCSVLines(services: Services): Seq[Seq[Any]] = {
		val crossNames = requirements.map(_._1.name)//Column(requirements.map(_._1.name), "CrossName")
		val successProb = requirements.map{row => 
			services.crossingService.getSuccessProbability(row._1)
		}
//			Column(
//			requirements.map{row => 
//				services.crossingService.getSuccessProbability(row._1)
//			},
//			"SuccessProbability"
//		)
		val numRequired = requirements.map(_._2)
//			Column(
//			requirements.map(_._2),
//			"NumRequired"
//		)
		val confLevel = requirements.map(_._3)
//			Column(
//			requirements.map(_._3),
//			"ConfidenceLevel"
//		)
		val numOffspringForConfidence = requirements.map{ case (cross, numSuccess, conf) =>
			val successProb = services.crossingService.getSuccessProbability(cross)
			//TODO sort out component/service
			services.statisticsService.numPlantsForConfidence(conf, successProb, numSuccess)
		}
//		Column(
//			requirements.map{case (cross, numSuccess, conf) =>
//				val successProb = services.crossingService.getSuccessProbability(cross)
//				//TODO sort out component/service
//				services.statisticsService.numPlantsForConfidence(conf, successProb, numSuccess)
//			},
//			"NumForConfidence"
//		)
		
		val columns = Seq(
			"CrossName" +: crossNames,
			"SuccessProbability" +: successProb,
			"NumRequired" +: numRequired,
			"ConfidenceLevel" +: confLevel,
			"NumForConfidence" +: numOffspringForConfidence
		)
		
//		Seq(names, probs, numRequired, confLevel, numOffspringForConfidence)
		columns.transpose
	}
}
case class LociComposition(cross: Cross, donors: Seq[RootPlant]) extends Output {
	val name = cross.name
	val fileName = name+".composition"
	def buildCSVLines(services: Services): Seq[Seq[Any]] = {
		import services._
		val comp = compositionService.buildComposition(crossingService.getPlantDistribution(cross))
		
		val cmLengths = donors(0).species.cMLengths
		val rows: Seq[Seq[Any]] = for{
			(chromComp, cId) <- comp.chromosomeCompositions.zipWithIndex
			(tidComp, sideId) <- List(chromComp.left, chromComp.right).zipWithIndex
			cmId <- 0 until cmLengths(cId)
		} yield {
			val tidId = cId * 2 + sideId
			val contribs = donors.map{donor => tidComp.proportions(cmId).getOrElse(donor, -.0)}
			tidId +: cmId +: contribs 
		}
		
//		val cols = rows.transpose
//		val tidId = Column(cols(0).asInstanceOf[Seq[Int]], "tidId")
//		val cmId = Column(cols(1).asInstanceOf[Seq[Int]], "cMId")
//		val proportions =  cols.takeRight(donors.size).asInstanceOf[IndexedSeq[IndexedSeq[Double]]].zip(donors).map{case (col, donor) => 
//			Column(col, donor.name)
//		}
		
		val header = Seq("tidId", "cMId") ++: donors.map(_.name)
		
		header +: rows
//		
//		Seq(tidId, cmId) ++: proportions
	}
}
//TODO test
case class MeanCrossComposition(crosses: Seq[Cross], donors: Seq[RootPlant]) extends Output {
	val name = "MeanCrossComposition"
	val fileName = name
	def buildCSVLines(services: Services): Seq[Seq[Any]] = {
		import services._
		val rows: Seq[Seq[Any]] = for{
				cross <- crosses
				donor <- donors
			} yield (
				IndexedSeq(
					cross.name, 
					donor.name, 
					services.statistics.mean(
						statisticsService.gatherSamples(crossingService.getDistribution(cross).map(_.alleleCount(donor).proportion)).toEmpiricalSeq
					)
				)
			)
		
//		val cols = rows.transpose
//		val cross = Column(cols(0).asInstanceOf[Seq[String]], "Cross")
//		val donor = Column(cols(1).asInstanceOf[Seq[String]], "Donor")
//		val proportion =  Column(cols(2).asInstanceOf[Seq[Double]], "MeanProportion")
//		Seq(cross, donor, proportion)
		
		val header = Seq("Cross", "Donor", "MeanProportion")
		
		header +: rows
	}
}