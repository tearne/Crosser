package org.tearne.crosser.proto

class Random {
	def nextInt(range: Int): Int = (math.random*range).asInstanceOf[Int]
	def nextBoolean(p: Probability): Boolean = math.random < p.value
}