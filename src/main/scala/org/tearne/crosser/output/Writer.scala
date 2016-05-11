package org.tearne.crosser.output

import java.nio.file.Path
import org.slf4j.LoggerFactory
import sampler.io.CSV

class Writer{
	val log = LoggerFactory.getLogger(getClass())
	
	def write(path: Path, lines: Seq[Seq[Any]]){
		log.info("Wrote " + path.getFileName())
		CSV.writeLines(path, lines)
	}
}
