package org.tearne.crosser.config

import java.nio.file.Path
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Buffer
import com.typesafe.config.ConfigException
import org.tearne.crosser.plant._
import org.tearne.crosser.cross._
import com.typesafe.config.Config
import scala.collection.immutable.ListMap
import org.tearne.crosser.output._

class SchemeConfig(path: Path){
	import scala.collection.JavaConversions._
	
	private val config = ConfigFactory.parseFile(path.toFile())
	
	val chunkSize = config.getInt("system.convergence.chunkSize")
	val tolerance = config.getDouble("system.convergence.tolerance")
	
	val dbURL = config.getString("system.db.url")
	val dbProfile = config.getString("system.db.profile")
	val dbDriver = config.getString("system.db.driver")
	
	val name: String = config.getString("name")
	
	val plants: Map[String, RootPlant] = {
		val species = Species(
				config.getString("species.name"),
				scala.collection.JavaConversions.asScalaBuffer(config.getIntList("species.chromosome_lengths")).map{_.intValue()}.toIndexedSeq
		)
		
		config.getConfigList("plants").map{ plantConfig =>
			val name = plantConfig.getString("name")
			name -> new RootPlant(
				name,
				species
			)
		}.toMap	
	}
	
	val crosses: ListMap[String, Cross] = {
		val loci: Buffer[Locus] = config.getConfigList("plants").flatMap{ plantConfig =>
			val plant = plants(plantConfig.getString("name"))
			try{
				val lociFromPlant = plantConfig.getConfigList("loci").map{ locusConfig =>
					val name = locusConfig.getString("name")
					Locus(
						plant,
						locusConfig.getInt("linkage_group"),
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
	
	val outputs: List[Output] = {
		config.getConfigList("output").map{ conf => 
			conf.getString("type") match {
				case "proportion_distribution" => 
					ProportionDistribution(
						crosses(conf.getString("cross")), 
						plants(conf.getString("donor"))
					)
				case "success_probability" => 
					SuccessProbability(crosses(conf.getString("cross")))
				case "loci_composition" => 
					LociComposition(crosses(conf.getString("cross")))
				case "cross_composition" =>
					CrossComposition(crosses(conf.getString("cross")))
			}
		}.toList
	}
		
	private def makeHetProtocol(crossConfig: Config, lociMap: Map[String, Locus]): HeterozygousProtocol = {
		HeterozygousProtocol(
				crossConfig.getStringList("protocol.loci").map(name => lociMap(name)).toSet,
				try{
					Some(crossConfig.getInt("protocol.num_homozygously"))
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