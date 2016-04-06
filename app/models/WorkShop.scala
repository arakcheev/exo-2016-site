/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models

import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.Macros

import scala.collection.generic.CanBuildFrom
import scala.concurrent.Future
import scala.language.higherKinds

case class WorkShopItem(var _id: Id, startDate: DateTime, endDate: DateTime, title: String)

object WorkShopItem {
  implicit val format = Json.format[WorkShopItem]
  implicit val handler = Macros.handler[WorkShopItem]

  def apply(startDate: DateTime, endDate: DateTime, title: String): WorkShopItem = {
    WorkShopItem(newId, startDate, endDate, title)
  }
}

trait WorkShop {

  /**
    * Save new workshop
    *
    * @return saved workshop
    */
  def save(item: WorkShopItem): Future[WorkShopItem]

  /**
    * Get all workshops
    * @tparam M collection type to return (ex, List, Seq, Set)
    */
  def list[M[_]]()(implicit cbf: CanBuildFrom[M[_], WorkShopItem, M[WorkShopItem]]): Future[M[WorkShopItem]]

  /**
    * Remove workshop.
    */
  def remove(id: Id): Future[Unit]

  def update(id: Id, item: WorkShopItem): Future[WorkShopItem]
}
