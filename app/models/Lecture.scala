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

/**
  * Represents speaker
  *
  * @param fullname speaker full name
  * @param organization speaker organization (institute, where located)
  */
case class Speaker(fullname: String, organization: String)

object Speaker {
  implicit val format = Json.format[Speaker]
  implicit val handler = Macros.handler[Speaker]
}

/**
  * Represents lecture class
  *
  * @param _id lecture id
  * @param speaker lecture speaker
  * @param title lecture title
  * @param date lecture date
  * @param abstr lecture abstract
  */
case class Lecture(_id: Id, speaker: Speaker, title: String, date: DateTime, abstr: String)

object Lecture {
  implicit val format = Json.format[Lecture]
  implicit val handler = Macros.handler[Lecture]

  def apply(speaker: Speaker, title: String, date: Long, abstr: String): Lecture = {
    val dateTime = new DateTime(date)
    Lecture(newId, speaker, title, dateTime, abstr)
  }
}

/**
  * Main API for lectures.
  */
trait Lectures {

  /**
    * Save new lecture
    */
  def save(lecture: Lecture): Future[Lecture]

  /**
    * Get all lectures
    * @tparam M collection type to return (ex, List, Seq, Set)
    */
  def list[M[_]]()(implicit cbf: CanBuildFrom[M[_], Lecture, M[Lecture]]): Future[M[Lecture]]

  /**
    * Just Remove lecture.
    */
  def remove(id: Id): Future[Unit]
}