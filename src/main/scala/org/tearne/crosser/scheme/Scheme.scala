package org.tearne.crosser.scheme

import java.nio.file.Path
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Buffer
import com.typesafe.config.ConfigException
import org.tearne.crosser.plant._
import org.tearne.crosser.cross._
import com.typesafe.config.Config
import scala.collection.immutable.ListMap

class Scheme(path: Path) {
	import scala.collection.JavaConversions._
	
	private val config = ConfigFactory.parseFile(path.toFile())
	
	val chunkSize = config.getInt("config.chunkSize")
	val recombinationProb = config.getDouble("config.recombinationProb")
	val tolerance = config.getDouble("config.tolerance")
	
	val name: String = config.getString("name")
	
	val plants: Map[String, RootPlant] = {
		val species = Species(
				config.getString("species.name"),
				scala.collection.JavaConversions.asScalaBuffer(config.getIntList("species.chromosomeLengths")).map{_.intValue()}.toIndexedSeq
		)
		
		config.getConfigList("plants").map{ plantConfig =>
			val name = plantConfig.getString("name")
			name -> new RootPlant(
				name,
				species
			)
		}toMap	
	}
	
	val crosses: ListMap[String, Cross] = {
		val loci: Buffer[Locus] = config.getConfigList("plants").flatMap{ plantConfig =>
			val plant = plants(plantConfig.getString("name"))
			try{
				val lociFromPlant = plantConfig.getConfigList("loci").map{ locusConfig =>
					val name = locusConfig.getString("name")
					Locus(
						plant,
						locusConfig.getInt("linkageGroup"),
						locusConfig.getInt("position"),
						locusConfig.getString("name")
					)
				}
				lociFromPlant	
			} catch {
				case e: ConfigException.Missing => Nil
			}

		}
		val lociMap = loci.map(l => l.name -> l).toMap
		val crossesBuiltSoFar = collection.mutable.ListMap[String, Cross]()
		
		for(crossConfig <- config.getConfigList("crosses")){ 
			val everythingSoFar = crossesBuiltSoFar ++ plants
			val name = crossConfig.getString("name")
			val left = everythingSoFar(crossConfig.getString("left"))
			val right = everythingSoFar(crossConfig.getString("right"))
			
			Zygosity.withName(crossConfig.getString("protocol.zygosity"))
			val protocol = crossConfig.getString("protocol.zygosity") match {
				case s: String if s.toLowerCase == "heterozygous" => makeHetProtocol(crossConfig, lociMap)
				case s: String if s.toLowerCase == "homozygous" => makeHomProtocol(crossConfig, lociMap)
			} 
			crossesBuiltSoFar += name -> Cross(left, right, protocol, name)
		}
		
		collection.immutable.ListMap(crossesBuiltSoFar.toSeq.reverse: _*)
	}
	
	val output: Seq[Cross] = config
		.getStringList("output.for")
		.map(name => crosses{name})
		
	val outputDonor: RootPlant = plants(config.getString("output.proportionsOf"))
	
	private def makeHetProtocol(crossConfig: Config, lociMap: Map[String, Locus]): HeterozygousProtocol = {
		HeterozygousProtocol(
				crossConfig.getStringList("protocol.loci").map(name => lociMap(name)).toSet,
				try{
					Some(crossConfig.getInt("protocol.numHomozygously"))
				}catch{
					case e: ConfigException.Missing => None
				}
		)
	}
	
	private def makeHomProtocol(crossConfig: Config, lociMap: Map[String, Locus]): HomozygousProtocol = {
		HomozygousProtocol(
				crossConfig.getStringList("protocol.loci").map(name => lociMap(name)).toSet
		)
	}
}