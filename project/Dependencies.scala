import sbt.ModuleID
import sbt._
import play.sbt.Play.autoImport._


object Dependencies{

  val reactiveMongoVer = "0.11.10"

  val mongoDrivers: Seq[ModuleID] = Seq(
                                         "org.reactivemongo" %% "reactivemongo"
                                       ).map(_ % reactiveMongoVer)

  val play: Seq[ModuleID] = Seq(cache, ws, specs2 % Test)

  val async = "org.scala-lang.modules" % "scala-async_2.11" % "0.9.6"

  val aws = "com.amazonaws" % "aws-java-sdk" % "1.11.172"

  val bcrypt = "com.github.t3hnar" % "scala-bcrypt_2.11" % "2.5"

  val pdfbox = "org.apache.pdfbox" % "pdfbox" % "2.0.0"

  val project: Seq[ModuleID] = mongoDrivers ++ play ++ Seq(async, aws, bcrypt, pdfbox)


}

object WebjarDependencies{
  val webJarDependesies = Seq(
                               "org.webjars" % "requirejs" % "2.1.22",
                               "org.webjars" % "underscorejs" % "1.8.3",
                               "org.webjars" % "jquery" % "1.11.3",
                               "org.webjars" % "angularjs" % "1.4.9" exclude("org.webjars", "jquery"),
                               "org.webjars" % "nervgh-angular-file-upload" % "2.1.1",
                               "org.webjars" % "angular-ui-bootstrap" % "0.14.3"
                             )
}