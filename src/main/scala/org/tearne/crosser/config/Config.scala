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
		val crossesBuiltSoFar = collection.mutable.ListMap[String, Cross]()
		
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
		}: _*)//.toMap
		
		//Created linked crosses from unlinked by resolving parents
		val t = unlinkedCrosses.keys.foldLeft(ListMap.empty[String, Cross]){case (acc, crossName) =>
			def linkCross(acc0: ListMap[String, Cross], name: String): (ListMap[String, Cross], Cross) = {
				if(acc0.contains(name)) (acc0, acc0(name))
				else{
					val unlinkedCross = unlinkedCrosses(name)
					val leftName = unlinkedCross.leftName
					val rightName = unlinkedCross.rightName
					
					val (acc1, leftParent) = 
						if(plants.contains(leftName)) {
							(acc0, plants(leftName))
						}
						else if(acc.contains(leftName)){
							(acc0, acc0(leftName))
						}
						else linkCross(acc0, leftName)
						
					val (acc2, rightParent) = 
						if(plants.contains(rightName)) {
							(acc1, plants(rightName))
						}
						else if(acc1.contains(rightName)) {
							(acc1, acc1(rightName))
						}
						else linkCross(acc1, rightName)
						
					val cross = unlinkedCross.addLinks(leftParent, rightParent)
					val acc3 = acc2 + (name -> cross)
					(acc3, cross)
				}
			}
			
			linkCross(acc, crossName)._1
		}
		
		t
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