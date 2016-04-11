/*
 * Copyright 2015 Arakcheev Artem (artem.arakcheev@phystech.edu)
 *
 */

package models

import play.api.libs.json.Json
import reactivemongo.bson.Macros

import scala.concurrent.Future

case class Participant(_id: Id, name: String, surname: String,
                       email: String, status: String, organization: String, age: String,
                       position: String, lastname: Option[String])

object Participant {

  def apply(name: String, surname: String, email: String, status: String, organization: String, age: String, position: String, lastname: Option[String]): Participant = {
    Participant(newId, name, surname, email, status, organization, age, position, lastname)
  }

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
