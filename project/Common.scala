import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

/**
  * Settings that are common to all the SBT projects
  */
object Common extends AutoPlugin {
  override def trigger = allRequirements
  override def requires: sbt.Plugins = JvmPlugin

  override def projectSettings = Seq(
    organization := "com.panaseer.codingexercise",
    version := "1.0-SNAPSHOT",
    resolvers += Resolver.typesafeRepo("releases"),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8", // yes, this is 2 args
      "-target:jvm-1.8",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Ywarn-numeric-widen",
      "-Xfatal-warnings"
    ),
    Test / scalacOptions ++= Seq("-Yrangepos"),
    autoAPIMappings := true
  )
}
