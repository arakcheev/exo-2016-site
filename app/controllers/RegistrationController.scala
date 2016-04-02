/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package controllers

import com.google.inject.{Inject, Singleton}
import models.Participants
import play.api.libs.json.Reads
import play.api.mvc.{Action, Controller}

import scala.concurrent.{Future, ExecutionContext}

@Singleton
class RegistrationController @Inject()(participants: Participants, implicit val executionContext: ExecutionContext) extends Controller {

  def emailAvailable() = Action.async { implicit request =>
    request.body.asJson.flatMap(_.\("email").validate(Reads.email).asOpt) match {
      case None => Future(BadRequest)
      case Some(email) =>
        participants.emailAvailable(email).map(_.toString).map(Ok(_))
    }
  }

  def phoneAvailable() = Action.async { implicit request =>
    request.body.asJson.flatMap(_.\("phone").validate[String].asOpt) match {
      case None => Future(BadRequest)
      case Some(phone) =>
        participants.phoneAvailable(phone).map(_.toString).map(Ok(_))
    }
  }
}
