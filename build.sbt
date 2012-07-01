name := "Crosser"

version := "0.2.0"

organization := "org.tearne"

libraryDependencies ++= Seq(
	"junit" % "junit" % "4.8" % "test->default",
	"org.specs2" %% "specs2" % "1.11" % "test",
	"org.mockito" % "mockito-all" % "1.8.5" %"test->default",
	"com.typesafe" % "config" % "0.4.1"
)

scalaVersion := "2.9.2"
