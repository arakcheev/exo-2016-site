/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models

import org.joda.time.DateTime
import play.api.libs.json.JsValue

import scala.collection.mutable

case class ProgramItem(item: WorkShopItem, sessions: Seq[Lecture])

class Program {

  private type Entry = (DateTime, mutable.TreeSet[ProgramItem])

  private val dates = collection.mutable.TreeSet.empty[Entry]

  /**
    * Add new school day
    */
  def addDate(date: DateTime): Unit = {
    val entry = (date, mutable.TreeSet.empty[ProgramItem])
    dates += entry
  }

  /**
    * Add new workshop item
    */
  def addWorkShopItem(workShopItem: WorkShopItem): Unit = {
    dates
    ???
  }

  /**
    * Add new session
    */
  def addSession(lecture: Lecture): Unit = {
    ???
  }

  def toJson: JsValue = {
    ???
  }

  def foreach(block: (DateTime, Seq[ProgramItem]) => Unit): Unit = {

  }


}
