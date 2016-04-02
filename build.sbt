import com.typesafe.sbt.rjs.Import.RjsKeys._
import com.typesafe.sbt.rjs.Import.RjsKeys
import com.typesafe.sbt.web.Import.WebJs._
import com.typesafe.sbt.web.js.JS

name := """exo-conference-site"""

version := "0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scala-lang.modules" % "scala-async_2.11" % "0.9.6-RC2",
  "org.reactivemongo" %% "reactivemongo" % "0.11.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "com.amazonaws" % "aws-java-sdk" % "1.10.62",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0-RC1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

val webJarDependesies = Seq(
  "org.webjars" % "requirejs" % "2.1.22",
  "org.webjars" % "underscorejs" % "1.8.3",
  "org.webjars" % "jquery" % "1.11.3",
  "org.webjars" % "angularjs" % "1.4.9" exclude("org.webjars", "jquery"),
  "org.webjars" % "nervgh-angular-file-upload" % "2.1.1",
  "org.webjars" % "angular-ui-bootstrap" % "0.14.3",
  "org.webjars" % "ng-context-menu" % "1.0.1",
  "org.webjars.bower" % "angular-scroll" % "1.0.0"
)

libraryDependencies ++= webJarDependesies

pipelineStages := Seq(rjs, digest, gzip)

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

JsEngineKeys.parallelism := 4

(excludeFilter in rjs) := new FileFilter{
  def accept(file: File) = {
    file.getPath.contains("lib/angular") ||
      file.getPath.contains("lib/bootstrap") ||
      file.getPath.contains("lib/jquery") ||
      file.getPath.contains("lib/underscorejs") ||
      file.getPath.contains("vendor/node_modules/ng-fx/node_modules/") ||
      file.getPath.contains("vendor/jquery") ||
      file.getPath.contains("vendor/amcharts") ||
      file.getPath.contains("requirejs/require")
  }
}

RjsKeys.paths += ("jsRoutes" -> ("/jsroutes" -> "empty:"))
RjsKeys.mainModule := "app"
RjsKeys.mainConfig := "config"
RjsKeys.generateSourceMaps := false
RjsKeys.webJarCdns := Map("org.webjars" -> "//webjars-file-service.herokuapp.com/files")