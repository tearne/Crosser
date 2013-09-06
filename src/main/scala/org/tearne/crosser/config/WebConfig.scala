package org.tearne.crosser.config

import java.nio.file.Path

import scala.collection.JavaConversions.asScalaBuffer

import org.tearne.crosser.output.LociComposition
import org.tearne.crosser.output.MeanCrossComposition
import org.tearne.crosser.output.Output
import org.tearne.crosser.output.ProportionDistribution
import org.tearne.crosser.output.SuccessProbability

import com.typesafe.config.{ConfigFactory => TypesafeConfigFactory}

class WebConfig(val path: Path) extends Config{
	import scala.collection.JavaConversions._
	
	val outputs: List[Output] = {
		config.getConfigList("output").map{ conf => {
			val innerDataConfig = TypesafeConfigFactory.parseString(conf.getString("data"))
			val outputType = conf.getString("type")
			outputType match {
				case "proportion_distribution" => 
					ProportionDistribution(
						crosses(innerDataConfig.getString("cross")), 
						plants(innerDataConfig.getString("donor"))
					)
				case "success_probability" => 
					SuccessProbability(innerDataConfig.getStringList("crosses").map(c => crosses(c)))
				case "loci_composition" => 
					LociComposition(crosses(innerDataConfig.getString("cross")), plants.values.toSeq)
				case "mean_cross_composition" =>
					MeanCrossComposition(innerDataConfig.getStringList("crosses").map{c => crosses(c)}, plants.values.toSeq)
			}
		}}.toList
	}
}