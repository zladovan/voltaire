name := "voltaire"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.3.play24",
  "com.softwaremill.macwire" %% "macros" % "1.0.5",
  "com.softwaremill.macwire" %% "runtime" % "1.0.5",
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
