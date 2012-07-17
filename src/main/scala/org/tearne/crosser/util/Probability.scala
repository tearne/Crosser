package org.tearne.crosser.util

case class Probability(val p: Double) {
if(p > 1.0 || p < 0.0) throw new ProbabilityException("Probability out of range: "+p)
	
}
object Probability{
	//def apply(p: Double) = new Probability(p)
	val half = new Probability(0.5)
	val onePercent = new Probability(0.01)
}

case class ProbabilityException(msg: String = "", cause: Throwable = null) extends Exception