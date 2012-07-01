/*
 * Copyright (c) Oliver Tearne (tearne at gmail dot com)
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either version
 * 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  
 * If not, see <http://www.gnu.org/licenses/>.
 */

package org.tearne.crosser.proto

import java.io.File
import scala.collection.mutable.ListBuffer
import java.io.FileWriter

class Writer(val file: File) {
	private case class Column(data: Iterable[Any], header: Option[String])
	private val columns = ListBuffer[Column]()

	def addColumn(data: Iterable[Any], header: String) { addColumn(data, Some(header)) }
	def addColumn(data: Iterable[Any], header: Option[String]) {
		columns += Column(data, header)
	}
	
	def write() {
		def makeCSVLine(tokens: Iterable[Any]) = {
			val newLine: String = System.getProperty("line.separator")
			val it = tokens.iterator
			val builder = new StringBuilder()
			it.foreach(dat => {
				builder.append(dat)
				if(it.hasNext)
					builder.append(",")
			})
			builder.append(newLine)
			builder.toString
		}
		
		val fWriter = new FileWriter(file, false)
		val rows = columns.map(column => column.data.map(_.toString)).transpose
		
		fWriter.append(makeCSVLine(columns.map(_.header.get)))		
		rows.foreach( row => fWriter.append(makeCSVLine(row)) )
		fWriter.close
	}
	
	def writeMap(map: Map[_ <: Any, _ <: Any]) {
		val (keys, values) = map.unzip
		addColumn(keys, "Key")
		addColumn(values, "Value")
		write
	}
}
object Writer{
	def apply(file: File) = new Writer(file)
	def writeMap(file: File, map: Map[_ <: Any, _ <: Any]) = new Writer(file).writeMap(map)
}