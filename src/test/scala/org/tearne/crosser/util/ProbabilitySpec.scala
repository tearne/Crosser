package org.tearne.crosser.util
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProbabilitySpec extends Specification{
	"Probability" should {
		"have useful constants" in {
			(Probability.half mustEqual Probability(0.5)) and
			(Probability.onePercent mustEqual Probability(0.01))
		}
		"throw exception if out of range" in {
			(Probability(1.000001) must throwA[ProbabilityException]) and
			(Probability(-0.000001) must throwA[ProbabilityException])
		}
		"have value based hashcode and equals" in {
			val instance1a = Probability(0.2)
			val instance1b = Probability(0.2)
			val instance2 = Probability(0.2000000000001)
			
			(instance1a mustEqual instance1b) and
			(instance1a mustNotEqual instance2) and
			(instance1a.hashCode mustEqual instance1b.hashCode) and
			(instance1a.hashCode mustNotEqual instance2.hashCode)
		}
	}
}