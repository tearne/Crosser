package org.tearne.crosser.util

class Discrete[T](val elements: IndexedSeq[T]) {
	override def toString() = "DiscreteDistribution: "+countsMap.toString
	
	val countsMap = elements.groupBy(identity).mapValues(_.size)
	val size = countsMap.values.sum //foldLeft(0)((acc,count) => acc+count) 
	val densityMap = countsMap.mapValues(count => count.asInstanceOf[Double]/size)
	
	def distanceTo(that: Discrete[T]): Double = {
		val indexes = countsMap.keySet ++ that.countsMap.keySet
		def distAtIndex(i: T) = math.abs(this(i)-that(i))
		indexes.map(distAtIndex(_)).max
	}
	
	def apply(index: T): Double = {
		if(densityMap.contains(index)) densityMap(index)
		else 0
	}
	
	def sample(random: Random): T = {
		val index = random.nextInt(size)
		elements(index)
	}
	
	def + (that: Discrete[T]): Discrete[T] = new Discrete(
		this.elements ++ that.elements
	)
	
	def ++ (elements: Seq[T]) = new Discrete(
		this.elements ++ elements
	)
	
	def canEqual[T: Manifest](other: Any): Boolean = other.isInstanceOf[Discrete[_]]	
	
	override def equals(other: Any) = other match {
		case that: Discrete[T] => 
			(that canEqual this) && (that.countsMap equals countsMap)
		case _ => false
	}
	
	override def hashCode() = countsMap.hashCode	 
}
object Discrete{
	def apply[T]() = new Discrete[T](IndexedSeq[T]())
	def apply[T](elements: T*) = new Discrete[T](elements.toIndexedSeq)
}