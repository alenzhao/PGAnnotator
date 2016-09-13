name := "CordAnnotator"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "args4j" % "args4j" % "2.0.26",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "org.specs2" %% "specs2" % "2.3.11" % "test",
  "uk.ac.ebi.pride" % "jmztab-modular-model" % "3.0.3",
  "uk.ac.ebi.pride" % "jmztab-modular-util"  % "3.0.3",
  "uk.ac.ebi.pride" % "jmztab-modular-converters" % "3.0.3"
)

resolvers ++= Seq("Maven" at "http://repo1.maven.org/maven2",
                 "nexus-ebi-release-repo"  at "http://www.ebi.ac.uk/Tools/maven/repos/content/groups/ebi-repo",
                 "nexus-ebi-snapshot-repo" at "http://www.ebi.ac.uk/Tools/maven/repos/content/groups/ebi-snapshots/",
                 Resolver.mavenLocal

)


    