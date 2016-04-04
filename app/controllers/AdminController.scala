package controllers

import javax.inject._

import models.Participants
import play.api.libs.json.Json
import play.api.mvc._
import security.{Secured, Authentication}

import scala.concurrent.ExecutionContext

case class LoginData(login: String, password: String)

object LoginData {
  implicit val reader = Json.reads[LoginData]
}

@Singleton
class AdminController @Inject()(
                                 authentication: Authentication,
                                 secured: Secured,
                                 participants: Participants)(implicit exec: ExecutionContext) extends Controller {

  def isLogged = secured(Ok)

  def login = Action.async(parse.json[LoginData]) { request =>
    authentication.auth(request.body.login, request.body.password).map {
      case Some(user) => Ok.withSession(authentication.SESSION_NAME -> user._id.stringify)
      case None => BadRequest("Invalid login/password")
    }
  }

  def logout = secured(Ok.withNewSession)

  def listParticipants = secured.async {
    participants.list().map { xs =>
      Ok(Json.toJson(xs))
    }
  }

}
