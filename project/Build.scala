import sbt._
import Keys._

object FundamentalsBuild extends Build {
  val buildVersion = "0.1-SNAPSHOT"

  val scalaVer = "2.10.2"

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization  := "com.dennishorte",
    version       := buildVersion,
    scalaVersion  := scalaVer
  )

  lazy val fundamentals = Project(
    id = "Fundamentals",
    base = file("."),
    settings = buildSettings ++
      Seq(
        libraryDependencies ++= Dependencies.common,
        resolvers ++= Resolvers.resolvers,
        scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
      )
  )
}

object Resolvers {
  val resolvers = Seq(
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
    "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    "Sonatype Repo" at "https://oss.sonatype.org/content/repositories/releases/"
  )
}

object Dependencies {
  def scalaVersion = FundamentalsBuild.scalaVer
  val scalatestVersion = "2.0.M5b"

  val common = Seq(
    "org.scalatest" % "scalatest_2.10" % scalatestVersion % "test"
  )
}
