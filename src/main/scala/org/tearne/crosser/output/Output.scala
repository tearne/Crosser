package org.tearne.crosser.output

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant
import sampler.data.Types.Column
import sampler.data.Empirical._
import sampler.math.StatisticsComponent
import org.tearne.crosser.plant.Species
import org.tearne.crosser.Services

trait Output{
	val name: String
	val fileName: String
	def buildData(services: Services): Seq[Column[_]]
}

case class ProportionDistribution(cross: Cross, donor: RootPlant) extends Output{
	val name = s"${donor.name}_in_${cross.name}"
	val fileName = name + ".density"
	def buildData(services: Services): Seq[Column[_]] = {
		val samplable = services.crossingService.getSamplable(cross).map(_.alleleCount(donor).proportion)
		val values = services.statisticsDistributionService.build(samplable)
		Seq(new Column(values, name))
	}
}
case class SuccessProbability(crosses: Seq[Cross]) extends Output{
	val name = "ProbSuccess"
	val fileName = name
	def buildData(services: Services): Seq[Column[_]] = {
		val names = Column(crosses.map(_.name), "CrossName")
		val probs = Column(
			crosses.map{cross => 
				services.crossingService.getSuccessProbability(cross)
			},
			"SuccessProbability"
		)
		Seq(names, probs)
	}
}
case class LociComposition(cross: Cross, donors: Seq[RootPlant]) extends Output {
	val name = cross.name
	val fileName = name+".composition"
	def buildData(services: Services): Seq[Column[_]] = {
		import services._
		val comp = compositionService.buildComposition(crossingService.getPlantDistribution(cross))
		
		val cmLengths = donors(0).species.cMLengths
		val rows: IndexedSeq[Seq[AnyVal]] = for{
			(chromComp, cId) <- comp.chromosomeCompositions.zipWithIndex
			(tidComp, sideId) <- List(chromComp.left, chromComp.right).zipWithIndex
			cmId <- 0 until cmLengths(cId)
		} yield {
			val tidId = cId * 2 + sideId
			val contrib = donors.map{donor => tidComp.proportions(cmId).getOrElse(donor, -.0)}
			tidId +: cmId +: contrib 
		}
		
		val cols = rows.transpose
		val tidId = Column(cols(0).asInstanceOf[Seq[Int]], "tidId")
		val cmId = Column(cols(1).asInstanceOf[Seq[Int]], "cMId")
		val proportions =  cols.takeRight(donors.size).asInstanceOf[IndexedSeq[IndexedSeq[Double]]].zip(donors).map{case (col, donor) => 
			Column(col, donor.name)
		}
		
		Seq(tidId, cmId) ++: proportions
	}
}
//TODO test
case class MeanCrossComposition(crosses: Seq[Cross], donors: Seq[RootPlant]) extends Output {
	val name = "MeanCrossComposition"
	val fileName = name
	def buildData(services: Services): Seq[Column[_]] = {
		import services._
		val rows: Seq[Seq[Any]] = for{
				cross <- crosses
				donor <- donors
			} yield (
				IndexedSeq(
					cross.name, 
					donor.name, 
					StatisticsComponent.mean(
						statisticsDistributionService.build(crossingService.getSamplable(cross).map(_.alleleCount(donor).proportion)).toEmpiricalSeq
					)
				)
			)
		
		val cols = rows.transpose
		val cross = Column(cols(0).asInstanceOf[Seq[String]], "Cross")
		val donor = Column(cols(1).asInstanceOf[Seq[String]], "Donor")
		val proportion =  Column(cols(2).asInstanceOf[Seq[Double]], "MeanProportion")
		Seq(cross, donor, proportion)
	}
}