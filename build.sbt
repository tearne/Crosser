name := "Crosser"

version := "0.2.1"

organization := "org.tearne"

resolvers ++= Seq(
	"Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
	"Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
	"Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
	"junit" % "junit" % "4.8" % "test->default",
	"org.specs2" %% "specs2" % "1.14" % "test",
	"org.mockito" % "mockito-all" % "1.9.0" %"test->default",
	"com.typesafe.akka" %% "akka-slf4j" % "2.1.4",
	"com.typesafe" % "config" % "0.4.1",
	"com.typesafe.akka" %% "akka-actor" % "2.1.4",
	"com.typesafe.akka" %% "akka-remote" % "2.1.4",
	"com.typesafe.akka" %% "akka-cluster-experimental" % "2.1.4"
)

scalaVersion := "2.10.1"
