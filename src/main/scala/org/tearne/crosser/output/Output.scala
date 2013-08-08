package org.tearne.crosser.output

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.distribution.CrosserService
import sampler.data.Types.Column
import sampler.data.Empirical._
import sampler.math.StatisticsComponent

trait Output{
	val name: String
	val fileName: String
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]]
}

case class ProportionDistribution(cross: Cross, donor: RootPlant) extends Output{
	val name = s"${donor.name}_in_${cross.name}"
	val fileName = name + ".density"
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]] = {
		val samplable = service.getSamplable(cross).map(_.alleleCount(donor).proportion)
		val values = distBuilder(samplable)
		Seq(new Column(values, name))
	}
}
case class SuccessProbability(crosses: Seq[Cross]) extends Output{
	val name = "ProbSuccess"
	val fileName = name
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]] = {
		val names = Column(crosses.map(_.name), "CrossName")
		val probs = Column(
			crosses.map{cross => 
				service.getSuccessProbability(cross)
			},
			"SuccessProbability"
		)
		Seq(names, probs)
	}
}
case class LociComposition(cross: Cross) extends Output{
	val name = "todo"
	val fileName = "todo"
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]] = {
		assert(false)
		null
	}
}
//TODO test
case class MeanCrossComposition(crosses: Seq[Cross], donors: Seq[RootPlant]) extends Output {
	val name = "MeanCrossComposition"
	val fileName = name
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]] = {
		val rows: Seq[Seq[Any]] = for{
				cross <- crosses
				donor <- donors
			} yield (
				IndexedSeq(
					cross.name, 
					donor.name, 
					StatisticsComponent.mean(
						distBuilder(service.getSamplable(cross).map(_.alleleCount(donor).proportion)).toEmpiricalSeq
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