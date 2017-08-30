/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models

import org.joda.time.DateTime
import org.mongodb.scala.bson.codecs.Macros
import play.api.libs.json.Json
//import reactivemongo.bson.Macros

import scala.collection.generic.CanBuildFrom
import scala.concurrent.Future
import scala.language.higherKinds

trait Item {
  val date: DateTime
}

object Item {

  implicit object Ord extends Ordering[Item] {
    override def compare(x: Item, y: Item): Int = x.date.compareTo(y.date)
  }

}

case class WorkShopItem(_id: Id, date: DateTime, endDate: DateTime, title: String) extends Item

object WorkShopItem {
  implicit val format = Json.format[WorkShopItem]

  import org.mongodb.scala.bson.codecs.Macros._
  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromCodecs,fromRegistries}
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY

  val codecRegistry = fromRegistries(fromCodecs(dateTimeCodec), fromProviders(classOf[WorkShopItem]), DEFAULT_CODEC_REGISTRY )


//  def apply(startDate: DateTime, endDate: DateTime, title: String): WorkShopItem = {
//    WorkShopItem(newId, startDate, endDate, title)
//  }
}

trait WorkShop extends Callbacks[WorkShopItem]{

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
