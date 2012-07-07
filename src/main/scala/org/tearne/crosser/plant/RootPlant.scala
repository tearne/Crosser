package org.tearne.crosser.plant

import org.tearne.crosser.cross.Crossable

trait ConcretePlant extends Crossable
case class RootPlant(val name: String, val species: Species) extends ConcretePlant