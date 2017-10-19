/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.{Inject, Provider}
import com.mongodb.client.model.{FindOneAndUpdateOptions, ReturnDocument, UpdateOptions}
import database.{CrudOps, Driver}
import models.{Lectures, _}
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

class LecturesImpl(val collection: Collection[Lecture])(implicit val executionContext: ExecutionContext) extends Lectures{

  override def addPresentaion(id: Id, presentationUrl: String): Future[Lecture] = {
    val query = equal("_id", id)
    val setter = set("url", presentationUrl)
    val opts = new FindOneAndUpdateOptions
    opts.upsert(false)
    opts.returnDocument(ReturnDocument.AFTER)
    collection.findOneAndUpdate(query, setter, opts).toFuture
  }
}

class LecturesImplProvider @Inject()(driver: Driver)(implicit val executionContext: ExecutionContext) extends Provider[Lectures] {

  val collection = driver.getCollection[Lecture]("lectures", Lecture)

  override def get(): Lectures = new LecturesImpl(collection)
}
