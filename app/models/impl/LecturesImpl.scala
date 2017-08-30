/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.{Inject, Provider}
import com.mongodb.client.model.{FindOneAndUpdateOptions, ReturnDocument}
import database.Driver
import models.{Lectures, _}
import org.bson.conversions.Bson
import org.mongodb.scala.bson.BsonDocument
import play.api.Logger

import scala.collection.generic.CanBuildFrom
import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

class LecturesImpl(collection: Collection[Lecture])(implicit val executionContext: ExecutionContext) extends Lectures{

  private val logger = Logger(getClass)

  /**
    * Save new lecture
    */
  override def save(lecture: Lecture): Future[Lecture] = {
    collection.insertOne(lecture).toFuture().map { _ =>
      &(lecture)
      lecture
    }
  }

  /**
    * Get all lectures
    * @tparam M collection type to return (ex, List, Seq, Set)
    */
  override def list[M[_]]()(implicit cbf: CanBuildFrom[M[_], Lecture, M[Lecture]]): Future[M[Lecture]] = {
    val query: Bson = BsonDocument()
    collection.find(query).toFuture().map{lectures ⇒
      val res = cbf.apply()
      lectures.foreach(res += _)
      res.result()
    }
  }

  /**
    * Just Remove lecture.
    */
  override def remove(id: Id): Future[Unit] = {
    import org.mongodb.scala.model.Filters._
    val query = equal("_id", id)
    collection.findOneAndDelete(query).toFuture().map{lecture ⇒
      if(lecture != null) &(lecture)
    }
  }

  override def update(id: Id, lecture: Lecture): Future[Lecture] = {
    import org.mongodb.scala.model.Filters._
    import org.mongodb.scala.model.Updates._
    val query = equal("_id", id)
    val updater = and(set("speaker", lecture.speaker),
                       set("title", lecture.title),
                       set("date", lecture.date),
                       set("abstr", lecture.abstr)
                     )
    val opts = new FindOneAndUpdateOptions()
    opts.returnDocument(ReturnDocument.AFTER)
    collection.findOneAndUpdate(query, updater, opts).toFuture.map { lecture =>
      if(lecture != null) {
        &(lecture)
        lecture
      } else throw new RuntimeException(s"Updated lecture for id ${id.getValue.toHexString} is null.")
    }
  }
}

class LecturesImplProvider @Inject()(driver: Driver)(implicit val executionContext: ExecutionContext) extends Provider[Lectures] {

  val collection = driver.database.withCodecRegistry(Lecture.codecRegistry).getCollection[Lecture]("lectures")

  override def get(): Lectures = new LecturesImpl(collection)
}
