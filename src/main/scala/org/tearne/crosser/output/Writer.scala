package org.tearne.crosser.output

import java.nio.file.Path
import sampler.data.Types.Column
import sampler.io.CSVTableWriter
import org.slf4j.LoggerFactory

class Writer{
	val log = LoggerFactory.getLogger(getClass())
	
	def apply(path: Path, columns: Column[_]*){
		log.info("Wrote " + path.toAbsolutePath())
		new CSVTableWriter(path, true).apply(columns: _*)
	}
}
