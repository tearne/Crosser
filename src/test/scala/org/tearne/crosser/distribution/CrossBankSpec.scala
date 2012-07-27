package org.tearne.crosser.distribution

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.tearne.crosser.cross.Crosser
import org.tearne.crosser.cross.Crossable
import org.tearne.crosser.cross.Cross
import org.tearne.crosser.plant.Plant
import org.tearne.crosser.plant.ConcretePlant
import org.tearne.crosser.plant.RootPlant

@RunWith(classOf[JUnitRunner])
class CrossBankSpec extends Specification with Mockito{
	val name = "myCross"
	
	"CrossBankSpec" should {
		"be able to sample from a cross" in {
			val leftPlant = mock[ConcretePlant]
			val rightPlant = mock[ConcretePlant]
			val crosser = mock[Crosser]
			
			val instance = new CrossBank(crosser)
			val cross = mock[Cross]
			cross.left returns leftPlant
			cross.right returns rightPlant
			cross.name returns name
						
			val sampledPlant = mock[Plant]
			crosser.cross(leftPlant, rightPlant, name) returns sampledPlant
			
			val results = for(i <- 1 to 100) yield instance.sample(cross)
			
			(results must contain(sampledPlant).only) and
			(there was atMostOne(crosser).cross(any, any, any))
		}
		"return RootPlant if asked to sample from RootPlant" in {	
			val instance = new CrossBank(null)
			val rootPlant = mock[RootPlant]
			
			instance.sample(rootPlant) mustEqual rootPlant
		} 
		"throw exception if asked to sample from Plant" in {
			new CrossBank(null).sample(mock[Plant]) must throwA[CrossBankException]
		}
	}
}