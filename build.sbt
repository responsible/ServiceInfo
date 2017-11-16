name := """ServiceInfo_BE"""
organization := "com.thoughtworks"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "com.typesafe.slick" %% "slick-testkit" % "3.2.1" % "test",
  "com.novocode" % "junit-interface" % "0.10" % "test",
  "ch.qos.logback" % "logback-classic" % "0.9.28" % "test",
  "com.h2database" % "h2" % "1.4.187",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.postgresql" % "postgresql" % "42.1.4",
  "org.flywaydb" % "flyway-core" % "4.2.0",
  "org.flywaydb" %% "flyway-play" % "4.0.0",
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "com.typesafe.play" %% "play-slick" % "3.0.0"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v", "-s", "-a")

parallelExecution in Test := false

logBuffered := false

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

flywayUrl := "jdbc:postgresql://localhost:5432/service_info"

flywayUser := "postgres"

flywayPassword := "postgres"

flywayLocations += "filesystem:db/migration"
