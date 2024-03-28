val scala3Version = "3.4.0"

ThisBuild / version      := "0.1.0"
ThisBuild / homepage := Some(url("https://github.com/SC-TPTP/sc-tptp"))
ThisBuild / startYear := Some(2024)
ThisBuild / scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
    )
ThisBuild / javacOptions ++= Seq("-encoding", "UTF-8")
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"


def githubProject(repo: String, commitHash: String) = RootProject(uri(s"$repo#$commitHash"))

lazy val customTstpParser = githubProject("https://github.com/SimonGuilloud/scala-tptp-parser.git", "eae9c1b7a9546f74779d77ff50fa6e8a1654cfa0")



lazy val root = project
  .in(file("."))
  .settings(
    name := "sc-tptp",

    scalaVersion := scala3Version,
    scalacOptions ++= Seq(
      "-language:implicitConversions",
    ),

    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    libraryDependencies += "io.github.leoprover" % "scala-tptp-parser_2.13" % "1.4"
  )
  .dependsOn(customTstpParser)
