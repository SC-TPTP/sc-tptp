val scala3Version = "3.10.1"

ThisBuild / homepage := Some(uri("https://github.com/SC-TPTP/sc-tptp"))
ThisBuild / startYear := Some(2024)
ThisBuild / scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked"
)
ThisBuild / javacOptions ++= Seq("-encoding", "UTF-8")
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "sc-tptp",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq(
      "-language:implicitConversions"
    ),
    // The proof fixture tests open classpath resources as files.
    Test / exportJars := false,
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % Test,
    libraryDependencies += "com.github.SC-TPTP" % "scala-tptp-parser_2.13" % "0b4ffa55c71415e925080608707c78ada1d750e5",
    libraryDependencies += "com.lihaoyi" %% "mainargs" % "0.7.6"
  )

name := "Utils"

version := "0.1"

enablePlugins(AssemblyPlugin)

assembly / mainClass := Some("sctptp.Main")
assembly / assemblyJarName := "sctptpUtils.jar"
assembly / assemblyOutputPath := baseDirectory.value / (assembly / assemblyJarName).value
