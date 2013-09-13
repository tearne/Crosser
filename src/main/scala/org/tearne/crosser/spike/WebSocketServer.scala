/*
 * Copyright (c) 2012-13 Oliver Tearne (tearne at gmail dot com)
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

package org.tearne.crosser.spike

import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import java.util.concurrent.Executors
import java.net.InetSocketAddress
import org.jboss.netty.channel.ChannelPipelineFactory
import org.jboss.netty.channel.ChannelPipeline
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder
import org.jboss.netty.handler.codec.frame.Delimiters
import org.jboss.netty.handler.codec.string.StringDecoder
import org.jboss.netty.handler.codec.string.StringEncoder
import org.jboss.netty.channel.SimpleChannelUpstreamHandler
import org.jboss.netty.channel.ChannelHandlerContext
import org.jboss.netty.channel.ChannelEvent
import org.jboss.netty.channel.ChannelStateEvent
import org.slf4j.LoggerFactory
import org.jboss.netty.channel.MessageEvent
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import org.jboss.netty.buffer._

object WebSocketServer extends App{
	// Configure server
	val bootstrap = new ServerBootstrap(
		new NioServerSocketChannelFactory(Executors.newCachedThreadPool, Executors.newCachedThreadPool)
	)
	
	val system = ActorSystem("MySystem")
    val mainActor = system.actorOf(Props(new Actor{
    	def receive = {
    		case m => println(s"Got message of type ${m.getClass}, $m")
    	}
    }))
	
	
	// Configure pipeline factory
	bootstrap.setPipelineFactory(new ServerPipelineFactory(mainActor: ActorRef))
	
	// Start the server
	bootstrap.bind(new InetSocketAddress(8080))
}

class ServerPipelineFactory(mainActor: ActorRef) extends ChannelPipelineFactory {
	override def getPipeline: ChannelPipeline = {
		val pipeline = org.jboss.netty.channel.Channels.pipeline
		
		// Add the text line codec combination first,
	    pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
	      8192, (Delimiters.lineDelimiter): _*))
	    pipeline.addLast("decoder", new StringDecoder)
	    pipeline.addLast("encoder", new StringEncoder)
	
	    // and then business logic.
	    pipeline.addLast("handler", new Handler(mainActor))
	
	    pipeline
	}
}

class Handler(mainActor: ActorRef) extends SimpleChannelUpstreamHandler {
	private val logger = LoggerFactory.getLogger(getClass.getName)
	
	override def handleUpstream(ctx: ChannelHandlerContext, e: ChannelEvent) {
		e match {
			case c: ChannelStateEvent => logger.info(e.toString)
			case _ =>
		}
		super.handleUpstream(ctx, e)
	}
	
	override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) {
		val request = e.getMessage.toString
		
		mainActor ! request
		
		e.getChannel().close()
	}
  
}
