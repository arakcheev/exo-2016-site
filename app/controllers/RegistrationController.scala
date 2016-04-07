/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package controllers

import com.google.inject.{Inject, Singleton}
import models.{Participant, Participants}
import play.api.libs.json.{Json, JsError, JsSuccess, Reads}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{Future, ExecutionContext}

case class RegistrationData(name: String, surname: String, email: String, status: String, organization: String) {

  def asParticipant = Participant(name, surname, email, status, organization)
}

object RegistrationData {
  //todo: Validate field in custom reader
  implicit val format = Json.format[RegistrationData]
}

@Singleton
class RegistrationController @Inject()(participants: Participants, implicit val executionContext: ExecutionContext) extends Controller {

  def emailAvailable() = Action.async { implicit request =>
    request.body.asJson.flatMap(_.\("email").validate(Reads.email).asOpt) match {
      case None => Future(BadRequest)
      case Some(email) =>
        participants.emailAvailable(email).map {
          case true => Ok
          case false => BadRequest
        }
    }
  }

  def phoneAvailable() = Action.async { implicit request =>
    request.body.asJson.flatMap(_.\("phone").validate[String].asOpt) match {
      case None => Future(BadRequest)
      case Some(phone) =>
        participants.phoneAvailable(phone).map {
          case true => Ok
          case false => BadRequest
        }
    }
  }

  def register = Action.async(parse.json) { implicit request =>
    request.body.validate[RegistrationData] match {
      case JsSuccess(p, _) => participants.register(p.asParticipant).map(_ => Ok)
      case JsError(errors) => Future(BadRequest(JsError.toJson(errors)))
    }
  }
}
