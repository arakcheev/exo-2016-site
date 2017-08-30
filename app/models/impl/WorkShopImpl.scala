/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.{Inject, Provider}
import com.mongodb.client.model.{FindOneAndUpdateOptions, ReturnDocument}
import database.Driver
import models._
import org.mongodb.scala.bson.BsonDocument
import play.api.Logger

import scala.collection.generic.CanBuildFrom
import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

class WorkShopImpl(collection: Collection[WorkShopItem])(implicit val executionContext: ExecutionContext) extends WorkShop{

  private val logger = Logger(getClass)

  /**
    * Save new workshop
    *
    * @return saved workshop
    */
  override def save(item: WorkShopItem): Future[WorkShopItem] = {
    collection.insertOne(item).toFuture.map { result =>
      &(item)
      item
    }
  }

  override def update(id: Id, item: WorkShopItem): Future[WorkShopItem] = {
    import org.mongodb.scala.model.Filters._
    import org.mongodb.scala.model.Updates._
    val query = equal("_id", id)
    val updater = and(
                      set("startDate", item.date),
                      set("endDate", item.endDate),
                      set("title", item.title)
                     )
    val opts = new FindOneAndUpdateOptions()
    opts.returnDocument(ReturnDocument.AFTER)
    collection.findOneAndUpdate(query, updater, opts).toFuture.map { item =>
      if(item == null) throw new Exception(s"Invalid result in update workshop item operation. Expected updater workshop, got null")
      &(item)
      item
    }
  }

  /**
    * Remove workshop.
    */
  override def remove(id: Id): Future[Unit] = {
    import org.mongodb.scala.model.Filters._
    val query = equal("_id", id)
    collection.findOneAndDelete(query).toFuture.map{item ⇒
      if(item != null) &(item)
    }
  }

  /**
    * Get all workshops
    * @tparam M collection type to return (ex, List, Seq, Set)
    */
  override def list[M[_]]()(implicit cbf: CanBuildFrom[M[_], WorkShopItem, M[WorkShopItem]]): Future[M[WorkShopItem]] = {
    val query = BsonDocument()
    collection.find(query).toFuture().map{seq ⇒
      val res = cbf.apply()
      seq.foreach(res += _)
      res.result()
    }
  }
}

class WorkShopProvider @Inject()(driver: Driver)(implicit val executionContext: ExecutionContext) extends Provider[WorkShop] {

  val collection: Collection[WorkShopItem] = driver.database.withCodecRegistry(WorkShopItem.codecRegistry).getCollection[WorkShopItem]("workshop")


  override def get(): WorkShop = new WorkShopImpl(collection)
}
