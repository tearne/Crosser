package org.tearne.crosser.output

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.distribution.CrosserService
import sampler.data.Types.Column

trait Output{
	val name: String
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]]
}

case class ProportionDistribution(cross: Cross, donor: RootPlant) extends Output{
	val name = s"${donor.name}_in_${cross.name}"
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]] = {
		val samplable = service.getSamplable(cross).map(_.alleleCount(donor).proportion)
		val values = distBuilder(samplable)
		Seq(new Column(values, name))
	}
}
case class SuccessProbability(crosses: Seq[Cross]) extends Output{
	val name = "todo"
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]] = {
		assert(false)
		null
	}
}
case class LociComposition(cross: Cross) extends Output{
	val name = "todo"
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]] = {
		assert(false)
		null
	}
}
case class CrossComposition(cross: Cross) extends Output {
	val name = "todo"
	def buildData(service: CrosserService, distBuilder: DistributionBuilder): Seq[Column[_]] = {
		assert(false)
		null
	}
}