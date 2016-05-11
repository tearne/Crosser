package org.tearne.crosser.spike

import java.net.URL
import java.io.InputStreamReader
import java.io.BufferedReader
import scala.io.Source
import com.typesafe.config.ConfigFactory

object URLRequest extends App{
	val stream = new URL("""http://crosser.callsar.com/api/backend/scheme/7?format=json""").openStream()

//	val reader = new InputStreamReader(stream), "UTF-8");

	val lines = Source.fromInputStream(stream).getLines
	val conf = ConfigFactory.parseString(lines.next)
	assert(!lines.hasNext)
}