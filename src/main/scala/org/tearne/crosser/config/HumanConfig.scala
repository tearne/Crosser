package org.tearne.crosser.config

import java.nio.file.Path

import scala.collection.JavaConversions.asScalaBuffer

import org.tearne.crosser.output.LociComposition
import org.tearne.crosser.output.MeanCrossComposition
import org.tearne.crosser.output.Output
import org.tearne.crosser.output.ProportionDistribution
import org.tearne.crosser.output.SuccessProbability
import com.typesafe.config.{Config => TypesafeConfig}

class HumanConfig(val typesafeConfig: TypesafeConfig) extends Config{
	import scala.collection.JavaConversions._
	
	val outputs: List[Output] = {
		typesafeConfig.getConfigList("outputs").map{ conf => 
			conf.getString("type") match {
				case "proportion_distribution" => 
					ProportionDistribution(
						crosses(conf.getString("cross")), 
						plants(conf.getString("donor"))
					)
				case "success_probability" => 
					SuccessProbability(conf.getStringList("crosses").map(c => crosses(c)))
				case "loci_composition" => 
					LociComposition(crosses(conf.getString("cross")), plants.values.toSeq)
				case "mean_cross_composition" =>
					MeanCrossComposition(conf.getStringList("crosses").map{c => crosses(c)}, plants.values.toSeq)
			}
		}.toList
	}
}