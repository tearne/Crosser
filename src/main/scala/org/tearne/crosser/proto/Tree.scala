	package org.tearne.crosser.proto
import scala.annotation.tailrec

case class ProbabilityException(msg: String = "", cause: Throwable = null) extends Exception
case class Probability(val value : Double){ 
	if(value < 0 || value > 1) throw ProbabilityException("Invalid value "+value) 
}
object Probability{
	val half = Probability(0.5)
}

// Something which is to be selected for, either heterozygous
// or homozygously
case class Locus(rootPlant: RootPlant, linkGrp: Int, cM: Int, name: String){
	val linkGroupIndex = linkGrp - 1
	val cMindex = cM - 1
}

object Zygosity extends Enumeration{
	type Zygosity = Value
	val Homozygous, Heterozygous = Value
}

// The protocol provides the overall specification of a cross
case class Protocol(val zygocity: Zygosity.Value, val requiredTraits: Set[Locus]){
	def satisfiedBy(offspring: ConcretePlant): Boolean = {
		if(zygocity == Zygosity.Heterozygous){
			requiredTraits.exists{t => 
				//Todo factor out common code
				val result = offspring.chromosomes(t.linkGroupIndex).left(t.cMindex) == t.rootPlant ||
				offspring.chromosomes(t.linkGroupIndex).right(t.cMindex) == t.rootPlant
				//println("required "+t.rootPlant+" at "+t.cMindex+","+result)
				result
			}
		}else{
			throw new UnsupportedOperationException("Not supported yet")
		}
	}
}

// The plant spec defines how many chromosomes there are and
// how many centimorgans each of them have
case class Species(val cMLengths: IndexedSeq[Int]){
	//TODO
	def buildChromosomesFrom(plant: RootPlant): IndexedSeq[Chromosome] = {
		cMLengths.map{length => 
			Chromosome(Tid(length, plant),Tid(length, plant))
		}.toIndexedSeq
	}
}

// A chromosome has a left and right half (tid)
case class Chromosome(val left: Tid, val right: Tid){
	//Todo check left and right same size
	val size = left.alleles.size
	def proportionOf(p: RootPlant): Double = {
		def numberInTid(t: Tid): Int = {
			t.alleles.foldLeft(0){case (acc, a) => acc + {if(a==p) 1 else 0}}
		}
		(numberInTid(left) + numberInTid(right))/(2.0 * size)
	}
}

// A Tid consists of a length of centimorgans
case class Tid(val alleles: IndexedSeq[RootPlant]){
	def apply(i: Int) = alleles(i)
}
object Tid{
	def apply(length: Int, plant: RootPlant): Tid = Tid((1 to length).map(i => plant))
}

// Things which can be crossed, they might be concrete plants,
// or 'yet to be evaluated' crosses (a cross). Sampling from a
// Crossable yields a concrete offspring plant, and crossing 
// with another crossable will give another cross.
trait Crossable{ 
	val name: String
	val species: Species

	def canEqual(other: Any): Boolean
	override def equals(other: Any): Boolean = {
		other match {
			case that: Crossable => 
				(that canEqual this) &&
				name == that.name &&
				species == that.species
			case _ => false
		}
	}
	override def hashCode(): Int = {
		41 * (
			41 + name.hashCode()
		) + species.hashCode()
	}
	
	def crossWith(that: Crossable, proto: Protocol, name: String): Crossable = new Cross(this, that, proto, name)
	
	def sample(cBank: ChromosomeBank, rnd: Random): ConcretePlant
}

// A ConcretePlant is a Crossable which has a set of Chromosomes.
// If we sample from it we get itself (no stochasticity), and if 
// we cross it then we introduce stochasticity in the Cross.
trait ConcretePlant extends Crossable{
	val chromosomes: IndexedSeq[Chromosome]
	
	def sample(cBank: ChromosomeBank, rnd: Random): ConcretePlant = this
	
	def proportionOf(p: RootPlant): Double = {
		val numAlleles = chromosomes.foldLeft(0)((acc, c) => acc + c.size)
		chromosomes.map(c => c.proportionOf(p) * c.size / numAlleles).sum
	}
	
	override def equals(other: Any): Boolean = {
		other match {
			case that: ConcretePlant => 
				(that canEqual this) &&
				super.equals(that)
				chromosomes == that.chromosomes
			case _ => false
		}
	}
	override def hashCode(): Int = {
		41 * (
			41 + super.hashCode()
		) + chromosomes.hashCode()
	}
}

// A Cross represents an offspring which is yet to be evaluated  
// into a ConcretePlant, but it's still crossable since the 
// evaluation will be deferred until later.
class Cross( //A cross, waiting to be evaluated into a Plant
		val left: Crossable, 
		val right: Crossable, 
		val proto: Protocol, 
		val name: String
) extends Crossable {
	override def toString() = "Cross(%s, %s, %s)".format(left.name, right.name, proto, name)
	
	def canEqual(other: Any): Boolean = other.isInstanceOf[Cross]
	override def equals(other: Any): Boolean = {
		other match {
			case that: Cross => 
				(that canEqual this) &&
				super.equals(that)
				left == that.left &&
				right == that.right &&
				proto == that.proto
				//name == that.name //In super
			case _ => false
		}
	}
	override def hashCode: Int = {
		41 * (41 * (41 * (41 * (41 + left.hashCode) + 
		right.hashCode()) + proto.hashCode()) + name.hashCode())
	}
	
	
	//TODO Check both parents have the same spec
	//TODO make concrete offspring plant from chromosomes
	
	val species = left.species //TODO check left spec == right spec

	def sample(chromosomeBank: ChromosomeBank, rnd: Random): ConcretePlant = chromosomeBank.sample(this, rnd)
}
object Cross{
	def apply(
		left: Crossable,
		right: Crossable, 
		proto: Protocol, 
		name: String
	) = new Cross(left, right, proto, name)
}

// A plant which is a left (!) in the breedig heirachy
class RootPlant( //A plant at the top of the tree
		val name: String, 
		val species: Species
) extends ConcretePlant{
	//Chromosomes contain references to self
	val chromosomes = species.buildChromosomesFrom(this)
	
	override def toString() = name
	def canEqual(other: Any) = other.isInstanceOf[RootPlant]
	override def equals(other: Any) = {
		other match {
			case that: RootPlant => 
				(that canEqual this) &&
				//super.equals(that) Don't check super otherwise will check chromosomes and get stackoverflow
				name == that.name && 	//In super
				species == that.species		//In super
			case _ => false
		}
	}
	override def hashCode(): Int = {
		41* (41 + name.hashCode()) + species.hashCode()
	}
	
}
object RootPlant{
	def apply(name: String, spec: Species) = new RootPlant(name, spec)
}

// A Plant is a ConcretePlant which has been generated from
// a cross.
class Plant(
		val name: String, 
		val chromosomes: IndexedSeq[Chromosome]
) extends ConcretePlant{
	override def toString() = name
	def canEqual(other: Any) = other.isInstanceOf[Plant]
	val species: Species = Species(chromosomes.map(_.size))
	override def equals(other: Any) = {
		other match {
			case that: Plant => 
				(that canEqual this) &&
				super.equals(that)
				// In super
				//name == that.name && 	
				//spec == that.spec &&	
				//chromosomes == that.chromosomes
		}
	}
}

//object FailedOffspring extends ConcretePlant{
//	val name = "Failed Offspring"
//	val chromosomes = null
//	def canEqual(other: Any) = false
//	val species = null
//}


// ChromosomeBank holds and generates distributions
// of offspring plant chromosomes produced by crosses
class ChromosomeBank{
	private val crosser = Crosser(Probability(0.01))
	private val distBuilderChunkSize = 1000
	var distMaps = Map[Cross, ChromosomeDistributions]()
	
	def sample(cross: Cross, rnd: Random): ConcretePlant = {
		getChromosomeDistributions(cross, rnd).sample(rnd)
	}
	
	def getChromosomeDistributions(cross: Cross, rnd: Random): ChromosomeDistributions = {
		distMaps.get(cross).getOrElse{
			val cDists = buildChromosomeDistributions(cross, rnd)
			distMaps += cross -> cDists
			cDists
		}
	}
	
	private def buildChromosomeDistributions(cross: Cross, rnd: Random) : ChromosomeDistributions= {
		def buildOffspring(): ConcretePlant = 
			crosser(cross.left.sample(this, rnd), cross.right.sample(this, rnd), cross.name, rnd)
		
		val epsilon = 0.05
		
		@tailrec
		def addSamples(dist: ChromosomeDistributions, failures: Int, samples: Int): ChromosomeDistributions = {
			val oldDist = dist
			val offspringWithFailures = (1 to distBuilderChunkSize).map(i => buildOffspring).groupBy(o => cross.proto satisfiedBy o)
			
			val numNewFailures = if(offspringWithFailures.contains(false)) offspringWithFailures(false).size else 0
			
			val newDist = dist ++(offspringWithFailures(true), failures+numNewFailures, samples)
			val diff = oldDist.distanceTo(newDist)
			println(diff)
			if(diff < epsilon) newDist
			else addSamples(
					newDist, 
					failures + numNewFailures, 
					samples + distBuilderChunkSize
			)
		}

		val newDist = addSamples(ChromosomeDistributions.forCross(cross), 0, 0)
		distMaps = distMaps.+(cross -> newDist)
		newDist
	}
}

class ChromosomeDistributions(val distributions: Seq[Discrete[Chromosome]], name: String, failures: Int, total: Int){
	lazy val successProbability = {
		println("f="+failures+", t="+total)
		if(total > 0) (total-failures)/total.asInstanceOf[Double] else 0
	}
	
	def ++(plants: Seq[ConcretePlant], failures: Int, total: Int): ChromosomeDistributions = {
			val chromosomesByIndex = plants.map(_.chromosomes).transpose
			val newDistributions = (chromosomesByIndex zip distributions).map{case (chroms, dist) =>
				dist ++ chroms
			} 
			new ChromosomeDistributions(newDistributions.toIndexedSeq, name, this.failures+failures, this.total+total)
	}
	
	def distanceTo(that: ChromosomeDistributions): Double = {
		(distributions zip that.distributions).map{case (d1, d2) => d1.distanceTo(d2)}.sum
	}
	
	def sample(rnd: Random): Plant = {
		val chromosomes = distributions.map(_.sample(rnd))
		new Plant(name, chromosomes.toIndexedSeq)
	}
	
	def buildDistribution(f: Plant => Double, rnd: Random, tolerance: Double): Seq[Double] = {
		val chunkSize = 10000
		val startDist = Seq[Double]()
		for(i <- 1 to chunkSize) yield f(sample(rnd))
	}
}
object ChromosomeDistributions{
	def forCross(cross: Cross): ChromosomeDistributions = 
		new ChromosomeDistributions(cross.species.cMLengths.map(l => Discrete[Chromosome]()), cross.name, 0, 0)
}


case class Crosser(val recombProb: Probability){
	def apply(left: ConcretePlant, right: ConcretePlant, name: String, rnd: Random): ConcretePlant = {
		//TODO exception if left spec != right spec
		val chromosomes = (left.chromosomes zip right.chromosomes).map{case (lch, rch) => chromosomeCross(lch, rch, rnd)}
		new Plant(name, chromosomes)
	}

	private def chromosomeCross(left: Chromosome, right: Chromosome, rnd: Random): Chromosome = {
		import Probability.half
		def chooseTid(c: Chromosome) = if(rnd.nextBoolean(half)) c.left else c.right
		def recombines() = rnd.nextBoolean(recombProb)
		
		def makeTidFrom(chromosome: Chromosome): Tid = {
			val leftAlleles = chromosome.left.alleles
			val rightAlleles = chromosome.right.alleles
			
			val booleans = (1 to leftAlleles.size).scanLeft(recombines()){case (acc, next) => if(recombines) !acc else acc}.tail
			
			val triples = IndexedSeq(booleans, leftAlleles, rightAlleles).transpose
			
			//Would this be faster if done in a mutable way?
			val alleles = triples.map{case IndexedSeq(b, left, right) => 
				if(b.asInstanceOf[Boolean]) left.asInstanceOf[RootPlant] 
				else right.asInstanceOf[RootPlant]
			}
			
			Tid(alleles)
		}
		
		Chromosome(makeTidFrom(left), makeTidFrom(right))
	}
}
