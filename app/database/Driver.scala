/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package database

import com.google.inject.{Inject, Singleton}
import com.mongodb.ConnectionString
import com.mongodb.client.model.UpdateOptions
import models.{Id, dateTimeCodec}
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecRegistries.{fromCodecs, fromProviders, fromRegistries}
import org.bson.codecs.configuration.{CodecProvider, CodecRegistry}
import org.bson.conversions.Bson
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.{MongoClient, MongoCollection}
import play.api.{Configuration, Logger}

import scala.collection.generic.CanBuildFrom
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe ⇒ u}

@Singleton
class Driver @Inject()(configuration: Configuration){
  private val mongoUrl = configuration.getString("mongodb.uri").get
  private val conntectionString = new ConnectionString(mongoUrl)
  private val dbName = conntectionString.getDatabase
  val mongoClient: MongoClient = MongoClient(mongoUrl)
  val database = mongoClient.getDatabase(dbName)

  def getCollection[T](name: String, cl: AnyRef)(implicit ct: ClassTag[T]): MongoCollection[T] = {
    val runtimeClass: Class[_] = ct.runtimeClass
    val rootMirror: u.Mirror = u.runtimeMirror(runtimeClass.getClassLoader)
    val classSymbol: u.ClassSymbol = rootMirror.classSymbol(runtimeClass)
    classSymbol.companion.typeSignature.members.find{ m ⇒
      m.isMethod && m.name.toString == "getCodecRegistry"
    } match {
      case None ⇒ throw new RuntimeException(s"Cant find getCodecRegistry of class $runtimeClass")
      case Some(method) ⇒
        val instance = rootMirror.reflect(cl)
        val codecRegistry = instance.reflectMethod(method.asMethod)().asInstanceOf[CodecRegistry]
        database.withCodecRegistry(codecRegistry).getCollection[T](name)
    }
  }
}

trait CrudOps[T]{
  private val logger = Logger(getClass)
  protected def collection: MongoCollection[T]
  implicit val executionContext: ExecutionContext

  protected def afterSaveCallback(e: T): Future[T] = Future.successful(e)
  protected def afterRemoveCallback(e: T): Future[T] = Future.successful(e)
  protected def afterUpdateCallback(e: T): Future[T] = Future.successful(e)

  def save(e: T): Future[T] = {
    collection.insertOne(e).toFuture().flatMap { _ =>
      afterSaveCallback(e)
    }
  }

  def list[M[_]]()(implicit cbf: CanBuildFrom[M[_], T, M[T]], ct: ClassTag[T]): Future[M[T]] = {
    val query: Bson = BsonDocument()
    collection.find(query).toFuture().map{entities ⇒
      val res = cbf.apply()
      entities.foreach(res += _)
      res.result()
    }
  }

  def remove(id: Id): Future[T] = {
    import org.mongodb.scala.model.Filters._
    val query = equal("_id", id)
    collection.findOneAndDelete(query).toFuture().flatMap{entity ⇒
      if(entity != null) afterRemoveCallback(entity)
      else throw new RuntimeException("Cant remove doc from db.")
    }
  }

  def update(id: Id, e: T): Future[T] = {
    import org.mongodb.scala.model.Filters._
    val filter = equal("_id", id)
    val opts = new UpdateOptions
    opts.upsert(false)
    collection.replaceOne(filter, e, opts).toFuture.flatMap{result ⇒
      if(result.getModifiedCount != 1) throw new RuntimeException(s"Error update document ${id.getValue.toHexString}")
      afterUpdateCallback(e)
    }
  }
}

trait EntityCompanion{
  implicit val codecProviders: Seq[CodecProvider]
  implicit val codecs: Seq[Codec[_]] = Seq(dateTimeCodec)
  def getCodecRegistry = fromRegistries(fromCodecs(codecs: _*), fromProviders(codecProviders: _*), DEFAULT_CODEC_REGISTRY)
}