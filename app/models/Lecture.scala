/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models

import database.{CrudOps, EntityCompanion}
import org.joda.time.DateTime
import org.mongodb.scala.bson.codecs.Macros
import play.api.libs.json.Json

import scala.collection.generic.CanBuildFrom
import scala.concurrent.Future
import scala.language.higherKinds
import scala.reflect.ClassTag

/**
  * Represents speaker
  *
  * @param fullname speaker full name
  * @param organization speaker organization (institute, where located)
  */
case class Speaker(fullname: String, organization: String)

object Speaker {
  implicit val format = Json.format[Speaker]
  implicit val handler = Macros.createCodecProvider[Speaker]()
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
case class Lecture(var _id: Id, speaker: Speaker, title: String, date: DateTime, abstr: String) extends Item {
  def setId(newId: Id) = {
    _id = newId
    this
  }

}

object Lecture extends EntityCompanion{
  implicit val format = Json.format[Lecture]
  override implicit val codecProviders = Seq(Macros.createCodecProvider[Lecture](), Speaker.handler)

  def apply(speaker: Speaker, title: String, date: Long, abstr: String): Lecture = {
    val dateTime = new DateTime(date)
    Lecture(newId, speaker, title, dateTime, abstr)
  }
}

/**
  * Main API for lectures.
  */
trait Lectures extends Callbacks[Lecture] with CrudOps[Lecture]{

}