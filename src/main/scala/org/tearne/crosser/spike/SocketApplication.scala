//package org.tearne.crosser.spike
//
//import java.nio.channels.ServerSocketChannel
//import java.net.InetSocketAddress
//import akka.actor.ActorSystem
//import akka.actor.Props
//import akka.actor.Actor
//import java.nio.ByteBuffer
//import java.util.Date
//import java.nio.channels.SocketChannel
//import java.nio.charset.Charset
//import java.nio.CharBuffer
//import akka.actor.actorRef2Scala
//
///*
// * 
// * Playing with sockets
// * 
// */
//
//object TestClient extends App{
//	val encoder = Charset.forName("UTF-8").newEncoder
//	val socChannel = SocketChannel.open
//	socChannel.connect(new InetSocketAddress("localhost",8888))
//	
//	val msg = new Date().toString() + " --> " + "Hello"
//	println(s"message is: $msg")
//	val bb = encoder.encode(CharBuffer.wrap(msg))
//	while (bb.hasRemaining()) {
//		socChannel.write(bb);  
//  }  
//}
//
//object SocketApplication extends App{
//	val ssc = ServerSocketChannel.open()
//    ssc.socket().bind(new InetSocketAddress("localhost",8888));
//    //ssc.configureBlocking(false);
//
//    val system = ActorSystem("MySystem")
//    val mainActor = system.actorOf(Props(new Actor{
//    	def receive = {
//    		case m => println(s"Got message of type ${m.getClass}, $m")
//    	}
//    }))
//    
//    while(true){
//    	mainActor ! {
//    		val s = ssc.accept()
//    		val bb = ByteBuffer.allocate(1024);
//    		s.read(bb)
//    		s.close
//    		bb.flip()
//    		val str = Charset.forName("UTF-8").newDecoder.decode(bb).toString
//    		str
//    	}
//    }
//}