package org.tearne.crosser.config

import java.nio.file.Path
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.immutable.ListMap
import scala.collection.mutable.Buffer
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.cross.HeterozygousProtocol
import org.tearne.crosser.cross.HomozygousProtocol
import org.tearne.crosser.cross.Locus
import org.tearne.crosser.cross.Zygosity
import org.tearne.crosser.plant.RootPlant
import org.tearne.crosser.plant.Species
import com.typesafe.config.{Config => TypesafeConfig}
import com.typesafe.config.ConfigException
import org.tearne.crosser.output.Output
import org.tearne.crosser.cross.Protocol
import scala.annotation.tailrec
import scalaz._
import Scalaz._

trait Config {
	import scala.collection.JavaConversions._
	
	val typesafeConfig: TypesafeConfig
	
	val chunkSize = typesafeConfig.getInt("system.convergence_chunk_size")
	val tolerance = typesafeConfig.getDouble("system.convergence_tolerance")
	val fewestPlants = typesafeConfig.getInt("system.convergence_fewest_plants")

	val name: String = typesafeConfig.getString("name")
	
	val outputs: List[Output]
	
	val plants: ListMap[String, RootPlant] = {
		val species = Species(
				typesafeConfig.getString("species.name"),
				scala.collection.JavaConversions.asScalaBuffer(typesafeConfig.getIntList("species.chromosome_lengths")).map{_.intValue()}.toIndexedSeq
		)
		
		val plants = typesafeConfig.getConfigList("plants").map{ plantConfig =>
			val name = plantConfig.getString("name")
			name -> new RootPlant(
				name,
				species
			)
		}

		ListMap(plants :_*)
	}
	
	val crosses: ListMap[String, Cross] = {
		// Generate map of loci by name
		val loci: Buffer[Locus] = typesafeConfig.getConfigList("plants").flatMap{ plantConfig =>
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
		
		// Generate map of crosses without links to parents (parent names only)
		case class UnlinkedCross(leftName: String, rightName: String, protocol: Protocol, name: String){
			def addLinks(left: Crossable, right: Crossable) = {
				assert(left.name == leftName && right.name == rightName)
				Cross(left, right, protocol, name)
			}
		}
		
		val unlinkedCrosses = ListMap({
			val t = for(crossConfig <- typesafeConfig.getConfigList("crosses")) yield { 
				val name = crossConfig.getString("name")
				val leftName = crossConfig.getString("left")
				val rightName = crossConfig.getString("right")
				
				Zygosity.withName(crossConfig.getString("zygosity"))
				val protocol = crossConfig.getString("zygosity") match {
					case s: String if s.toLowerCase == "heterozygous" => makeHetProtocol(crossConfig, lociMap)
					case s: String if s.toLowerCase == "homozygous" => makeHomProtocol(crossConfig, lociMap)
				} 
				name -> UnlinkedCross(
					leftName, 
					rightName, 
					protocol, 
					name
				)
			}
			t.toSeq
		}: _*)
		
		type CrossMap = ListMap[String, Cross]
		def linkCross(name: String): State[CrossMap, Cross] = {
			def makeCross(name: String): State[CrossMap, Cross] = {
				val unlinkedCross = unlinkedCrosses(name)
				val leftName = unlinkedCross.leftName
				val rightName = unlinkedCross.rightName
				
				for{
					cMap <- get[CrossMap]
					leftParent <- getParent(leftName)
					rightParent <- getParent(rightName)
					cross = unlinkedCross.addLinks(leftParent, rightParent)
					_ <- modify[CrossMap](s => s+(name -> cross))
				} yield cross
			}
			
			def getParent(name: String): State[CrossMap, Crossable] = {
					get[CrossMap].flatMap{crossMap =>
					if(plants.contains(name)) state(plants(name))
					else if(crossMap.contains(name)) state(crossMap(name))
					else linkCross(name)
					}
			}

			for{
				oCross <- gets[CrossMap, Option[Cross]](_.get(name))
				cross <- oCross
					.map(state[CrossMap, Cross])
					.getOrElse(makeCross(name))
			} yield cross
		}
		
		//Created linked crosses from unlinked by resolving parents
		val crossMap = unlinkedCrosses.keys.foldLeft(state[CrossMap, Any]()){case (acc, crossName) =>
			acc.flatMap{_ => linkCross(crossName)}
		}.exec(ListMap.empty[String, Cross])

		crossMap
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