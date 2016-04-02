/*
 * Copyright 2015 Arakcheev Artem (artem.arakcheev@phystech.edu)
 *
 */

package models

import play.api.libs.json.Json
import reactivemongo.bson.Macros

import scala.concurrent.Future

case class Participant(_id: Id, name: String, surname: String, email: String, phone: String)

object Participant {
  implicit val jsonFormat = Json.format[Participant]
  implicit val handler = Macros.handler[Participant]
}

trait Participants {

  def emailAvailable(email: String): Future[Boolean]

  def phoneAvailable(email: String): Future[Boolean]

  def register(participant: Participant): Future[Unit]

  def list(): Future[List[Participant]]

  def delete(id: Id): Future[Unit]

  def update(id: Id, participant: Participant): Future[Unit]
}
