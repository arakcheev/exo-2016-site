/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package database

import com.google.inject.{Inject, Singleton}
import com.typesafe.scalalogging.LazyLogging
import play.api.Configuration
import play.api.inject.ApplicationLifecycle
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection

import reactivemongo.api.commands.WriteConcern
import reactivemongo.api.gridfs.GridFS
import reactivemongo.core.nodeset.Authenticate

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

sealed trait Driver {

  val driver: MongoDriver

  def connection: MongoConnection

  def db: DefaultDB

  val gfs: GridFS[BSONSerializationPack.type]

  def collection(name: String): BSONCollection = db.collection(name)
}


@Singleton
final class DefaultDriverImpl @Inject()(
                                         configuration: Configuration,
                                         applicationLifecycle: ApplicationLifecycle) extends Driver with LazyLogging {

  private lazy val parsedUri = DefaultReactiveMongoApi.parseConf(configuration)

  override val driver: MongoDriver = new MongoDriver(Some(configuration.underlying))

  override def connection: MongoConnection = {
    val con = driver.connection(parsedUri)
    registerDriverShutdownHook(con, driver)
    con
  }


  override def db: DefaultDB = {
    import scala.concurrent.ExecutionContext.Implicits.global
    logger.info("ReactiveMongoApi starting...")
    connection(dbName)
  }

  private lazy val dbName: String = parsedUri.db.fold[String](
    throw configuration.globalError(
      s"cannot resolve the database name from URI: $parsedUri")) { name =>
    logger.info( s"""ReactiveMongoApi successfully configured with DB '$name'! Servers:\n\t\t${parsedUri.hosts.map { s => s"[${s._1}:${s._2}]" }.mkString("\n\t\t")}""")
    name
  }

  override val gfs: GridFS[BSONSerializationPack.type] = GridFS[BSONSerializationPack.type](db)

  private def registerDriverShutdownHook(connection: MongoConnection, mongoDriver: MongoDriver): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    applicationLifecycle.addStopHook { () =>
      Future {
        logger.info("ReactiveMongoApi stopping...")
        val f = connection.askClose()(10.seconds)

        f.onComplete {
          case e => logger.info(s"ReactiveMongoApi connections stopped. [$e]")
        }

        //see https://github.com/ReactiveMongo/Play-ReactiveMongo/blob/master/src/main/scala/play/modules/reactivemongo/ReactiveMongoApi.scala#L156
        Await.ready(f, 10.seconds)
        mongoDriver.close()
      }
    }
  }
}

private[database] object DefaultReactiveMongoApi {
  val DefaultPort = 27017
  val DefaultHost = "localhost:27017"

  private def parseLegacy(configuration: Configuration): MongoConnection.ParsedURI = {
    val db = configuration.getString("mongodb.db").getOrElse(throw configuration.globalError("Missing configuration key 'mongodb.db'!"))
    val uris = configuration.getStringList("mongodb.servers") match {
      case Some(list) => scala.collection.JavaConversions.collectionAsScalaIterable(list).toList
      case None => List(DefaultHost)
    }

    val nodes = uris.map { uri =>
      uri.split(':').toList match {
        case host :: port :: Nil => host -> {
          try {
            val p = port.toInt
            if (p > 0 && p < 65536) p
            else throw configuration.globalError(s"Could not parse URI '$uri': invalid port '$port'")
          } catch {
            case _: NumberFormatException => throw configuration.globalError(s"Could not parse URI '$uri': invalid port '$port'")
          }
        }
        case host :: Nil => host -> DefaultPort
        case _ => throw configuration.globalError(s"Could not parse host '$uri'")
      }
    }

    var opts = MongoConnectionOptions()

    configuration.getInt("mongodb.options.nbChannelsPerNode").
      foreach { nb => opts = opts.copy(nbChannelsPerNode = nb) }

    configuration.getString("mongodb.options.authSource").
      foreach { src => opts = opts.copy(authSource = Some(src)) }

    configuration.getInt("mongodb.options.connectTimeoutMS").
      foreach { ms => opts = opts.copy(connectTimeoutMS = ms) }

    configuration.getBoolean("mongodb.options.tcpNoDelay").
      foreach { delay => opts = opts.copy(tcpNoDelay = delay) }

    configuration.getBoolean("mongodb.options.keepAlive").
      foreach { keepAlive => opts = opts.copy(keepAlive = keepAlive) }

    configuration.getBoolean("mongodb.options.ssl.enabled").
      foreach { ssl => opts = opts.copy(sslEnabled = ssl) }

    configuration.getBoolean("mongodb.options.ssl.allowsInvalidCert").
      foreach { allows => opts = opts.copy(sslAllowsInvalidCert = allows) }

    configuration.getString("mongodb.options.authMode").foreach {
      case "scram-sha1" =>
        opts = opts.copy(authMode = ScramSha1Authentication)

      case _ => ()
    }

    configuration.getString("mongodb.options.writeConcern").foreach {
      case "unacknowledged" =>
        opts = opts.copy(writeConcern = WriteConcern.Unacknowledged)

      case "acknowledged" =>
        opts = opts.copy(writeConcern = WriteConcern.Acknowledged)

      case "journaled" =>
        opts = opts.copy(writeConcern = WriteConcern.Journaled)

      case "default" =>
        opts = opts.copy(writeConcern = WriteConcern.Default)

      case _ => ()
    }

    val IntRe = "^([0-9]+)$".r

    configuration.getString("mongodb.options.writeConcernW").foreach {
      case "majority" => opts = opts.copy(writeConcern = opts.writeConcern.
        copy(w = WriteConcern.Majority))

      case IntRe(str) => opts = opts.copy(writeConcern = opts.writeConcern.
        copy(w = WriteConcern.WaitForAknowledgments(str.toInt)))

      case tag => opts = opts.copy(writeConcern = opts.writeConcern.
        copy(w = WriteConcern.TagSet(tag)))

    }

    configuration.getBoolean("mongodb.options.writeConcernJ").foreach { jed =>
      opts = opts.copy(writeConcern = opts.writeConcern.copy(j = jed))
    }

    configuration.getInt("mongodb.options.writeConcernTimeout").foreach { ms =>
      opts = opts.copy(writeConcern = opts.writeConcern.copy(
        wtimeout = Some(ms)))
    }

    configuration.getString("mongodb.options.readPreference").foreach {
      case "primary" =>
        opts = opts.copy(readPreference = ReadPreference.primary)

      case "primaryPreferred" =>
        opts = opts.copy(readPreference = ReadPreference.primaryPreferred)

      case "secondary" =>
        opts = opts.copy(readPreference = ReadPreference.secondary)

      case "secondaryPreferred" =>
        opts = opts.copy(readPreference = ReadPreference.secondaryPreferred)

      case "nearest" =>
        opts = opts.copy(readPreference = ReadPreference.nearest)

      case _ => ()
    }

    val authenticate: Option[Authenticate] = for {
      username <- configuration.getString("mongodb.credentials.username")
      password <- configuration.getString("mongodb.credentials.password")
    } yield Authenticate(opts.authSource.getOrElse(db), username, password)

    MongoConnection.ParsedURI(
      hosts = nodes,
      options = opts,
      ignoredOptions = Nil,
      db = Some(db),
      authenticate = authenticate)
  }

  def parseConf(configuration: Configuration): MongoConnection.ParsedURI =
    configuration.getString("mongodb.uri") match {
      case Some(uri) => MongoConnection.parseURI(uri) match {
        case Success(parsedURI) if parsedURI.db.isDefined =>
          parsedURI
        case Success(_) =>
          throw configuration.globalError(s"Missing database name in mongodb.uri '$uri'")
        case Failure(e) => throw configuration.globalError(s"Invalid mongodb.uri '$uri'", Some(e))
      }

      case _ => parseLegacy(configuration)
    }
}