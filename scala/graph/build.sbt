import Dependencies._

ThisBuild / scalaVersion := "2.13.15"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "graph",
    assembly / assemblyJarName := "graph.jar",
    libraryDependencies += munit % Test,
    libraryDependencies += "com.lihaoyi" %% "pprint" % "0.9.0"
  )
