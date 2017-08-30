import sbt.ModuleID
import sbt._
import play.sbt.Play.autoImport._


object Dependencies{

  val reactiveMongoVer = "0.12.6"

  val mongoDrivers: Seq[ModuleID] = Seq(
                                         "org.reactivemongo" %% "reactivemongo"
                                       ).map(_ % reactiveMongoVer)

  val mongo = "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0"

  val play: Seq[ModuleID] = Seq(cache, ws, specs2 % Test)

  val async = "org.scala-lang.modules" % "scala-async_2.11" % "0.9.6"

  val aws = "com.amazonaws" % "aws-java-sdk" % "1.11.172"

  val bcrypt = "com.github.t3hnar" % "scala-bcrypt_2.11" % "2.5"

  val pdfbox = "org.apache.pdfbox" % "pdfbox" % "2.0.0"

  val it = "com.typesafe.play" %% "play-iteratees-reactive-streams" % "2.6.1"

  val project: Seq[ModuleID] = play ++ Seq(mongo, async, aws, bcrypt, pdfbox)


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