package org.tearne.crosser

import java.nio.channels.ServerSocketChannel
import java.net.InetSocketAddress
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor
import scala.io.BufferedSource
import java.nio.ByteBuffer
import java.util.Date
import java.nio.channels.SocketChannel

object TestClient extends App{
	val socChannel = SocketChannel.open
	socChannel.connect(new InetSocketAddress("localhost",8888))
	
	val msg = new Date().toString() + " > " + "Hello"
	val bb = ByteBuffer.allocate(msg.getBytes().length)
	bb.clear();  
	bb.put(msg.getBytes());  
	bb.flip();
	while (bb.hasRemaining()) {
		socChannel.write(bb);  
  }  
}

object SocketApplication extends App{
	val ssc = ServerSocketChannel.open()
    ssc.socket().bind(new InetSocketAddress("localhost",8888));
    //ssc.configureBlocking(false);

    val system = ActorSystem("MySystem")
    val mainActor = system.actorOf(Props(new Actor{
    	def receive = {
    		case m => println("Got message "+m)
    	}
    }))
    
    while(true){
    	mainActor ! {
    		val s = ssc.accept()
    		val bb = ByteBuffer.allocate(1024);
    		s.read(bb)
    		s.close
    		bb.flip().toString()
    	}
    }
}