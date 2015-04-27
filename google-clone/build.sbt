name := """google-clone"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.jsoup" % "jsoup" % "1.8.2",
  "com.google.inject" % "guice" % "3.0"
)
