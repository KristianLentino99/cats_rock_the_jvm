ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "cats_rock_the_jvm"
  )

libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0"
