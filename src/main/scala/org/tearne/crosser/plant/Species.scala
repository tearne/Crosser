package org.tearne.crosser.plant

class Species(val cMLengths: IndexedSeq[Int]){
	def buildChromosomesFrom(plant: RootPlant): IndexedSeq[Chromosome] = {
		cMLengths.map{length => 
			Chromosome(Tid(length, plant),Tid(length, plant))
		}.toIndexedSeq
	}
	
	def canEqual(other: Any): Boolean = other.isInstanceOf[Species]
	override def equals(other: Any): Boolean = {
		other match {
			case that: Species =>
				(that canEqual this) &&
				cMLengths == that.cMLengths
			case _ => false
		}
	}
	override def hashCode(): Int = {
		41 + cMLengths.hashCode
	}
}
object Species{
	def apply(lengths: Int*) = new Species(lengths.toIndexedSeq)
	def apply(lengths: IndexedSeq[Int]) = new Species(lengths)
}