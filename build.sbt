name := "CordAnnotator"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "args4j" % "args4j" % "2.0.26",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "org.specs2" %% "specs2" % "2.3.11" % "test"

)

resolvers += "Maven" at "http://repo1.maven.org/maven2"


    