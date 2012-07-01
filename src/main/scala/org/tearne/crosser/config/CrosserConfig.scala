package org.tearne.crosser.config
import java.nio.file.Path
import org.tearne.crosser.proto.RootPlant
import org.tearne.crosser.proto.Cross
import com.typesafe.config.ConfigFactory
import org.tearne.crosser.proto.Species
import org.tearne.crosser.proto.Zygosity
import org.tearne.crosser.proto.Protocol
import org.tearne.crosser.proto.Locus
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Buffer
import com.typesafe.config.ConfigException

class CrosserConfig(path: Path) {
	import scala.collection.JavaConversions._
	
	private val config = ConfigFactory.parseFile(path.toFile())
	//println(path.toAbsolutePath().toString())
	//println(config.root().render())	
	
	val name: String = config.getString("name")
	
	val plants: Map[String, RootPlant] = {
		val species = Species(
				scala.collection.JavaConversions.asScalaBuffer(config.getIntList("species")).map{_.intValue()}.toIndexedSeq
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
			val protocol = Protocol(
				Zygosity.withName(crossConfig.getString("protocol.zygosity")),
				crossConfig.getStringList("protocol.loci").map(name => lociMap(name)).toSet
			)
			crossesBuiltSoFar += name -> Cross(left, right, protocol, name)
		}
		
		crossesBuiltSoFar.toMap
	}
}