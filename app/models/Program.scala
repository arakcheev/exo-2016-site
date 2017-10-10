/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

case class DayItem(item: WorkShopItem, sessions: Seq[Lecture])

object DayItem {
  implicit val writer = Json.writes[DayItem]
}

class DayItemBuilder(var item: WorkShopItem) {

  private val sessions: mutable.LinkedHashSet[Lecture] = mutable.LinkedHashSet.empty[Lecture]

  def setItem(item: WorkShopItem): Unit = {
    this.item = item
  }

  def addLecture(lecture: Lecture): Unit = {
    if (item == null) throw new NullPointerException("You must set workshop item before setting lecture")
    //    if (lecture.date.isBefore(item.endDate) && lecture.date.isAfter(item.startDate))
    //      throw new IllegalArgumentException("Lecture date must be between workshop dates")

    sessions += lecture
  }

  def build = DayItem(item, sessions.toSeq.sortBy(_.date.getMillis))
}

class Program extends mutable.Iterable[(DateTime, Seq[DayItem])] {

  private val entries: mutable.ArrayBuffer[Item] = new mutable.ArrayBuffer[Item]()

  def this(workShopItems: List[WorkShopItem], lectures: List[Lecture]) = {
    this()
    workShopItems.foreach(addWorkShopItem)
    lectures.foreach(addSession)
  }

  def addWorkShopItem(workShopItem: WorkShopItem): Unit = {
    entries += workShopItem
  }

  def addSession(lecture: Lecture): Unit = {
    entries += lecture
  }

  def toJson: JsValue = new ProgramIterator(entries).jsonMap

  override def iterator: Iterator[(DateTime, Seq[DayItem])] = new ProgramIterator(entries)

  private class ProgramIterator(items: mutable.ArrayBuffer[Item]) extends Iterator[(DateTime, Seq[DayItem])] {
    val DATE_FORMAT = "dd MM YYYY"

    val formatter = DateTimeFormat.forPattern(DATE_FORMAT)

    // Group by date
    val grouped: Map[String, mutable.ArrayBuffer[Item]] = items.groupBy(_.date.toString("dd MM YYYY"))

    val buildDayItems: Map[String, ArrayBuffer[DayItem]] = grouped.mapValues { buff ⇒
      val workShops = buff.filter(_.isInstanceOf[WorkShopItem]).map(_.asInstanceOf[WorkShopItem])
      val lectures = buff.filter(_.isInstanceOf[Lecture]).map(_.asInstanceOf[Lecture])

      workShops
        .map { workShop ⇒
          val builder = new DayItemBuilder(workShop)
          val start = workShop.date.getMillis
          val end = workShop.endDate.getMillis
          lectures.filter{l ⇒
            val ldate = l.date.getMillis
            ldate >= start && ldate < end
          }.foreach(builder.addLecture)
          builder.build
        }
    }

//    val buildDayItems: Map[String, Seq[DayItem]] = grouped.mapValues { tree =>
//      var last: DayItemBuilder = null
//      tree.sortBy(-_.date.getMillis).foldLeft(Seq.empty[DayItemBuilder]) { case (acc, item) =>
//        item match {
//          case w: WorkShopItem =>
//            last = new DayItemBuilder(w)
//            acc :+ last
//          case l: Lecture =>
//            if (last ne null) last.addLecture(l)
//            else println("last in null but lecture addedd")
//            acc
//        }
//      }.map(_.build).sortBy(_.item.date.getMillis)
//    }

    val iterator: Iterator[(DateTime, Seq[DayItem])] = buildDayItems.toSeq.map {
      case (dateString, xs) => (formatter.parseDateTime(dateString), xs)
    }.sortBy(_._1.getMillis).iterator

    override def hasNext: Boolean = iterator.hasNext

    override def next(): (DateTime, Seq[DayItem]) = iterator.next()

    def jsonMap = {
      Json.toJson(iterator.map {
        case (date, xs) => (date.toString(DATE_FORMAT), xs)
      }.toMap)
    }
  }


}

trait ProgramAPI {

  def get(): Future[Program]
}
