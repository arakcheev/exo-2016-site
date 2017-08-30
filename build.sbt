import com.typesafe.sbt.rjs.Import.RjsKeys._
import com.typesafe.sbt.rjs.Import.RjsKeys
import com.typesafe.sbt.web.Import.WebJs._
import com.typesafe.sbt.web.js.JS

name := """exo-conference-site"""

version := "0.1.14"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.11.8"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Dependencies.project

libraryDependencies ++= WebjarDependencies.webJarDependesies

routesImport += "binders._"

pipelineStages := Seq(rjs, digest, htmlMinifier, gzip)

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
RjsKeys.paths += ("templates" -> ("/templates" -> "empty:"))
RjsKeys.paths += ("textAngular-sanitize" -> ("/assets/javascripts/textAngular-sanitize.min.js" -> "/assets/javascripts/textAngular-sanitize.min.js"))

RjsKeys.paths += ("textAngular-rangy" -> ("/assets/javascripts/textAngular-rangy.min.js" -> "/assets/javascripts/textAngular-rangy.min.js"))

RjsKeys.paths += ("textAngular" -> ("/assets/javascripts/textAngular.min.js" -> "/assets/javascripts/textAngular.min.js"))

RjsKeys.paths += ("angular-simple-logger" -> ("/assets/javascripts/textAngular.min.js" -> "/assets/javascripts/textAngular.min.js"))

RjsKeys.mainModule := "app"
RjsKeys.mainConfig := "config"
RjsKeys.generateSourceMaps := false
//RjsKeys.webJarCdns := Map("org.webjars" -> "//webjars-file-service.herokuapp.com/files")