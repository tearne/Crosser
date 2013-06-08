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
	"org.specs2" %% "specs2" % "1.13" % "test",
	"org.mockito" % "mockito-all" % "1.9.0" %"test->default",
	"com.typesafe" % "config" % "0.4.1"
)

scalaVersion := "2.10.1"
