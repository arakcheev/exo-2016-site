/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{Json, JsValue}

import scala.collection.mutable
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

  def build = DayItem(item, sessions.toSeq)
}

trait Item {
  val date: DateTime
}

object Item {

  implicit object Ord extends Ordering[Item] {
    override def compare(x: Item, y: Item): Int = x.date.compareTo(y.date)
  }

}

class Program extends mutable.Iterable[(DateTime, Seq[DayItem])] {

  private val entries: mutable.TreeSet[Item] = mutable.TreeSet.empty[Item]

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

  private class ProgramIterator(items: mutable.TreeSet[Item]) extends Iterator[(DateTime, Seq[DayItem])] {

    val DATE_FORMAT = "dd MM YYYY"

    val formatter = DateTimeFormat.forPattern(DATE_FORMAT)

    // Group by date
    val grouped: Map[String, mutable.TreeSet[Item]] = items.groupBy(_.date.toString("dd MM YYYY"))

    val buildDayItems: Map[String, Seq[DayItem]] = grouped.mapValues { tree =>
      var last: DayItemBuilder = null
      tree.foldLeft(Seq.empty[DayItemBuilder]) { case (acc, item) =>
        item match {
          case w: WorkShopItem =>
            last = new DayItemBuilder(w)
            acc :+ last
          case l: Lecture =>
            if( last ne null) last.addLecture(l)
            acc
        }
      }.map(_.build)
    }

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
