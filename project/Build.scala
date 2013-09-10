import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys

object SamplerBuild extends Build{
	val buildOrganization 	= "org.tearne"
	val buildVersion 	= "0.2.4"
	val buildScalaVersion	= "2.10.2"
		
	lazy val root = Project(
		id = "Crosser",
		base = file("."),
		settings = buildSettings ++ assySettings
	) 
	
	val assySettings = assemblySettings ++ Seq(
		test in assembly := {},
		mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
			case "application.conf" => MergeStrategy.discard
			case "reference.conf" => MergeStrategy.discard
			case "logback.xml" => MergeStrategy.discard
			case x => old(x)
		}}
	)
	
	lazy val buildSettings = Defaults.defaultSettings ++ Seq(
		organization := buildOrganization,
		version		 := buildVersion,
		scalaVersion := buildScalaVersion,

		retrieveManaged	:= false,
		
		resolvers ++= Seq(
			"Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
			"Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
			"Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
		),
		
		libraryDependencies ++= Seq(
			"junit" % "junit" % "4.8" % "test->default",
			"org.specs2" %% "specs2" % "1.14" % "test",
			"org.mockito" % "mockito-all" % "1.9.0" %"test->default",
			"org.scalatest" % "scalatest_2.10" % "2.0.M5b" % "test",
			"org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test",
			"com.typesafe.akka" %% "akka-slf4j" % "2.2.1",
			"com.typesafe" % "config" % "0.4.1",
			"com.typesafe.akka" %% "akka-actor" % "2.2.1",
			"org.apache.commons" % "commons-math3" % "3.0",
			"ch.qos.logback" % "logback-classic" % "1.0.12",
			"ahvla" % "sampler-core_2.10" % "0.0.15"
		)
	)
}
