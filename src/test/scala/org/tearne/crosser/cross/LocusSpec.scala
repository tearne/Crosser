package org.tearne.crosser.cross

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.tearne.crosser.plant.RootPlant
import org.specs2.mock.Mockito
import org.tearne.crosser.plant.Species

@RunWith(classOf[JUnitRunner])
class LocusSpec extends Specification with Mockito{
	val plant = RootPlant("Plant", mock[Species])
	
	"Locus" should {
		"convert linkage group to index" in {
			Locus(plant, 5, 10, "locus").linkGroupIndex mustEqual 4
		}
		"convert cM position to index" in {
			Locus(plant, 5, 10, "locus").cMIndex mustEqual 9
		}
		"have value based hashcode and equals" in {
			val instance1a = Locus(plant, 5, 10, "locus")
			val instance1b = Locus(plant, 5, 10, "locus")
			val instance2 = Locus(mock[RootPlant], 5, 10, "locus")
			val instance3 = Locus(plant, 6, 10, "locus")
			val instance4 = Locus(plant, 6, 11, "locus")
			val instance5 = Locus(plant, 5, 10, "fred")
		
			(instance1a mustEqual instance1b) and
			(instance1a mustNotEqual instance2) and
			(instance1a mustNotEqual instance3) and
			(instance1a mustNotEqual instance4) and
			(instance1a mustNotEqual instance5) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a.hashCode mustNotEqual instance2.hashCode) and
			(instance1a.hashCode mustNotEqual instance3.hashCode) and
			(instance1a.hashCode mustNotEqual instance4.hashCode) and
			(instance1a.hashCode mustNotEqual instance5.hashCode)
		}
	}
}