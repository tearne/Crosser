package org.tearne.crosser.output

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.RootPlant

trait Output

case class ProportionDistribution(cross: Cross, donor: RootPlant) extends Output
case class SuccessProbability(cross: Cross) extends Output
case class LociComposition(cross: Cross) extends Output
case class CrossComposition(cross: Cross) extends Output