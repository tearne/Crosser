package org.tearne.crosser.scheme

import java.nio.file.Path
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Buffer
import com.typesafe.config.ConfigException
import org.tearne.crosser.plant._
import org.tearne.crosser.cross._
import com.typesafe.config.Config

class Scheme(path: Path) {
	import scala.collection.JavaConversions._
	
	private val config = ConfigFactory.parseFile(path.toFile())
	
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
	
	val crosses: Map[String, Cross] = {
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
		val crossesBuiltSoFar = collection.mutable.Map[String, Cross]()
		
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
		
		crossesBuiltSoFar.toMap
	}
	
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
	
	private def makeHomProtocol(crossConfig: Config, lociMap: Map[String, Locus]): HeterozygousProtocol = {
		HeterozygousProtocol(
				crossConfig.getStringList("protocol.loci").map(name => lociMap(name)).toSet
		)
	}
}