package org.tearne.crosser.util

case class AlleleCount(success: Int, total: Int) {
	def +(that: AlleleCount) = 
		AlleleCount(
			this.success + that.success, 
			this.total + that.total
		)
		
	lazy val proportion = success.asInstanceOf[Double] / total.asInstanceOf[Double]
}