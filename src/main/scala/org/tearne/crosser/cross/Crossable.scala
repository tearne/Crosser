package org.tearne.crosser.cross

trait Crossable

case class Cross(left: Crossable, right: Crossable, protocol: Protocol, name: String) extends Crossable{
	
}