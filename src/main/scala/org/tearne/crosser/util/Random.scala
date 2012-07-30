package org.tearne.crosser.util

class Random {
	def nextInt(size: Int): Int = (math.random*size).asInstanceOf[Int]
	def nextBoolean(p: Probability): Boolean = math.random < p.p
}