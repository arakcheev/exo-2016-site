/*
 * Copyright 2015 Arakcheev Artem (artem.arakcheev@phystech.edu)
 *
 */

package models

import org.mongodb.scala.bson.codecs.Macros
import play.api.libs.json.Json

import scala.concurrent.Future

case class Participant(_id: Id, name: String, surname: String,
                       email: String, status: String, organization: String, age: String,
                       position: String, lastname: Option[String])

object Participant {

  def apply(name: String, surname: String, email: String, status: String, organization: String, age: String, position: String, lastname: Option[String]): Participant = {
    Participant(newId, name, surname, email, status, organization, age, position, lastname)
  }

  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY

  implicit val jsonFormat = Json.format[Participant]
  implicit val handler = Macros.createCodecProviderIgnoreNone[Participant]()

  val codecRegistry = fromRegistries(fromProviders(handler), DEFAULT_CODEC_REGISTRY )

}

trait Participants {

  def emailAvailable(email: String): Future[Boolean]

  def phoneAvailable(email: String): Future[Boolean]

  def register(participant: Participant): Future[Unit]

  def list(): Future[List[Participant]]

  def delete(id: Id): Future[Unit]

  def update(id: Id, name: String, surname: String, middleName: String,
             organization: String, age: String, position: String): Future[Participant]
}
