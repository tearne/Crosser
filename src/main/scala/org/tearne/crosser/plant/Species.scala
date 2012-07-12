package org.tearne.crosser.plant

class Species(val name: String, val cMLengths: IndexedSeq[Int]){
	def buildChromosomesFrom(rootPlant: RootPlant): IndexedSeq[Chromosome] = {
		cMLengths.map{length => 
			Chromosome(Tid(length, rootPlant),Tid(length, rootPlant))
		}.toIndexedSeq
	}
	
	def canEqual(other: Any): Boolean = other.isInstanceOf[Species]
	override def equals(other: Any): Boolean = {
		other match {
			case that: Species =>
				(that canEqual this) &&
				cMLengths == that.cMLengths &&
				name == that.name
			case _ => false
		}
	}
	override def hashCode(): Int = {
		41 * (41 + cMLengths.hashCode) + name.hashCode
	}
}
object Species{
	def apply(name: String, lengths: Int*) = new Species(name, lengths.toIndexedSeq)
	def apply(name: String, lengths: IndexedSeq[Int]) = new Species(name, lengths)
}