package org.tearne

import scala.collection.JavaConverters._
import util.control.Breaks._
import java.io.IOException
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds._
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService
import java.nio.file.FileSystems
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef
import scala.language.existentials
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileVisitResult
import scala.annotation.tailrec
 
class DirWatcher(val dir:Path, workerActor: ActorRef, recursive: Boolean) extends Runnable {
	val watchService = dir.getFileSystem().newWatchService()
	var trace = true
	var keys: Map[WatchKey,Path] = Map[WatchKey,Path]()
	
	if (recursive) {
        System.out.format("Scanning %s ...\n", dir);
        registerAll(dir);
        System.out.println("Done.");
    } else {
    	register(dir);
    }
	
	trace = true
	
	def register(dir: Path) = {
        val key = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)
        if (trace) {
            val prev = keys.get(key);
            if (prev == null) {
                printf("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    printf("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys = keys + ((key, dir))
    }
	
	def registerAll(start: Path) {
        Files.walkFileTree(start, new SimpleFileVisitor[Path]() {
            override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
                register(dir)
                FileVisitResult.CONTINUE
            }
        })
    }
	
	override def run(): Unit = {
		@tailrec
		def loop(){
			println("here")
			
			if(Thread.currentThread().isInterrupted) {}
			else{
				val watchKey = watchService.take()
				watchKey.pollEvents().asScala.foreach( event => {
					val fullPath  = dir.resolve(event.context.asInstanceOf[Path])
					format("%s: %s\n", event.kind().name(), fullPath);
					
	                if (recursive && 
	                		event.kind() == ENTRY_CREATE &&
	                		Files.isDirectory(fullPath, java.nio.file.LinkOption.NOFOLLOW_LINKS)) {
                        registerAll(fullPath)
	                }
					
					if(Files.isDirectory(fullPath)){
						val files = Files.newDirectoryStream(fullPath, "*.config").asScala.toSeq
						if(files.size == 1)
							workerActor ! files(0)
					}
					println("-- "+fullPath.subpath(0, dir.iterator.asScala.size))
					//Files.newDirectoryStream(fullPath, "*.config")
//					workerActor ! fullPath
				})
	            if (!watchKey.reset()) keys = keys - watchKey
	            if(!keys.isEmpty) loop()
			}
		}
		
		try {
			loop
		} catch {
			case ie: InterruptedException => println("InterruptedException: " + ie)
			case ioe: IOException => println("IOException: " + ioe)
			case e: Exception => println("Exception: " + e)
		} finally {
			watchService.close()
		}
	}
}
 
class Worker extends Actor {
	def receive = {
		case path: Path => println(s"Got some work at $path")
	}
}

object RunWatcher extends App {
	val system = ActorSystem("DirWatchSystem")
    val workerActor = system.actorOf(Props[Worker], name = "ben")
			
	println("Starting watch service...")
	val path = FileSystems.getDefault().getPath("/home/user/watchTest")
	val dir_watcher = new DirWatcher(path, workerActor, true)
	val watch_thread = new Thread(dir_watcher)
	watch_thread.start()
}