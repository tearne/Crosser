package org.tearne.crosser.config

import java.nio.file.Path

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.immutable.ListMap
import scala.collection.mutable.Buffer

import org.tearne.crosser.cross.Cross
import org.tearne.crosser.cross.HeterozygousProtocol
import org.tearne.crosser.cross.HomozygousProtocol
import org.tearne.crosser.cross.Locus
import org.tearne.crosser.cross.Zygosity
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.plant.Species

import com.typesafe.config.{Config => TypesafeConfig}
import com.typesafe.config.ConfigException
import com.typesafe.config.ConfigFactory

trait JsonConfig extends Config {
	val path: Path
	
	import scala.collection.JavaConversions._
	
	val config = ConfigFactory.parseFile(path.toFile())
	
	val chunkSize = config.getInt("system.convergence_chunk_size")
	val tolerance = config.getDouble("system.convergence_tolerance")

	val name: String = config.getString("name")
	
//	val dbURL = config.getString("system.db.url")
//	val dbProfile = config.getString("system.db.profile")
//	val dbDriver = config.getString("system.db.driver")
	
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
			
			Zygosity.withName(crossConfig.getString("zygosity"))
			val protocol = crossConfig.getString("zygosity") match {
				case s: String if s.toLowerCase == "heterozygous" => makeHetProtocol(crossConfig, lociMap)
				case s: String if s.toLowerCase == "homozygous" => makeHomProtocol(crossConfig, lociMap)
			} 
			crossesBuiltSoFar += name -> Cross(left, right, protocol, name)
		}
		
		collection.immutable.ListMap(crossesBuiltSoFar.toSeq.reverse: _*)
	}
		
	private def makeHetProtocol(crossConfig: TypesafeConfig, lociMap: Map[String, Locus]): HeterozygousProtocol = {
		HeterozygousProtocol(
				crossConfig.getStringList("loci").map(name => lociMap(name)).toSet,
				try{
					Some(crossConfig.getInt("num_homozygously"))
				}catch{
					case e: ConfigException.Missing => None
				}
		)
	}
	
	private def makeHomProtocol(crossConfig: TypesafeConfig, lociMap: Map[String, Locus]): HomozygousProtocol = {
		HomozygousProtocol(
				crossConfig.getStringList("loci").map(name => lociMap(name)).toSet
		)
	}
}