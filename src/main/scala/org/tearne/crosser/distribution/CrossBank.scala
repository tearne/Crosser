package org.tearne.crosser.distribution

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.util.Random
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.cross.Crossable

class CrossBank(crosser: Crosser) {
	def sample(cross: Crossable): Plant = throw new UnsupportedOperationException("todo")
}
class CrossBankException(msg: String, cause: Throwable) extends RuntimeException(msg, cause){
	def this(msg: String) = this(msg, null)
}