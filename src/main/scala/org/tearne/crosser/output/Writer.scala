package org.tearne.crosser.output

import java.nio.file.Path
import sampler.data.Types.Column
import sampler.io.CSVTableWriter

class Writer{
	def apply(path: Path, columns: Column[_]*){
		println(path.toAbsolutePath())
		new CSVTableWriter(path, true).apply(columns: _*)
	}
}
