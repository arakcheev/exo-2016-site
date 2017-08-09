/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.{Inject, Provider}
import database.Driver
import models._
import play.api.Logger

import scala.collection.generic.CanBuildFrom
import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

class WorkShopImpl(collection: Collection)(implicit val executionContext: ExecutionContext) extends WorkShop{

  private val logger = Logger(getClass)

  /**
    * Save new workshop
    *
    * @return saved workshop
    */
  override def save(item: WorkShopItem): Future[WorkShopItem] = {
    collection.insert(item).map { result =>
      if (result.n != 1) throw new Exception(s"Error save new workshop item to database. Return number of inserted documents is ${result.n} but must be 1")
      &(item)
      item
    }
  }

  override def update(id: Id, item: WorkShopItem): Future[WorkShopItem] = {
    val query = document("_id" -> id)
    item._id = id //update item id due to $set operator will try update id field of document
    val updater = document("$set" -> item)
    collection.findAndUpdate(query, updater, fetchNewObject = true).map { data =>
      val item = data.result[WorkShopItem].getOrElse(throw new Exception(s"Invalid result in update workshop item operation. Expected updater workshop, got ${data.value}"))
      &(item)
      item
    }
  }

  /**
    * Remove workshop.
    */
  override def remove(id: Id): Future[Unit] = {
    val query = document("_id" -> id)
    collection.find(query).one[WorkShopItem].flatMap {
      case Some(item) => collection.remove(query).map(_ => &(item))
      case None => Future((): Unit)
    }
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
