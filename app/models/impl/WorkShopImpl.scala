/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.{Inject, Provider}
import com.typesafe.scalalogging.LazyLogging
import database.Driver
import models.{document, Id, WorkShopItem, WorkShop, Collection}

import scala.collection.generic.CanBuildFrom
import scala.concurrent.{Future, ExecutionContext}
import scala.language.higherKinds

class WorkShopImpl(collection: Collection)(implicit val executionContext: ExecutionContext) extends WorkShop with LazyLogging {

  /**
    * Save new workshop
    *
    * @return saved workshop
    */
  override def save(item: WorkShopItem): Future[WorkShopItem] = {
    collection.insert(item).map { result =>
      if (result.n != 1) throw new Exception(s"Error save new workshop item to database. Return number of inserted documents is ${result.n} but must be 1")
      item
    }
  }

  override def update(id: Id, item: WorkShopItem): Future[WorkShopItem] = {
    val query = document("_id" -> id)
    item._id = id //update item id due to $set operator will try update id field of document
    val updater = document("$set" -> item)
    collection.findAndUpdate(query, updater, fetchNewObject = true).map { data =>
      data.result[WorkShopItem].getOrElse(throw new Exception(s"Invalid result in update workshop item operation. Expected updater workshop, got ${data.value}"))
    }
  }

  /**
    * Remove workshop.
    */
  override def remove(id: Id): Future[Unit] = {
    val query = document("_id" -> id)
    collection.remove(query).map(_ => ())
  }

  /**
    * Get all workshops
    * @tparam M collection type to return (ex, List, Seq, Set)
    */
  override def list[M[_]]()(implicit cbf: CanBuildFrom[M[_], WorkShopItem, M[WorkShopItem]]): Future[M[WorkShopItem]] = {
    val query = document()
    collection.find(query).cursor[WorkShopItem]().collect[M]()
  }
}

class WorkShopProvider @Inject()(driver: Driver)(implicit val executionContext: ExecutionContext) extends Provider[WorkShop] {
  override def get(): WorkShop = new WorkShopImpl(driver.collection("workshop"))
}
