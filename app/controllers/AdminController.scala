package controllers

import javax.inject._

import models._
import play.api.libs.json.Json
import play.api.mvc._
import security.{Secured, Authentication}

import scala.concurrent.ExecutionContext

case class LoginData(login: String, password: String)

object LoginData {
  implicit val reader = Json.reads[LoginData]
}

case class LectureData(speaker: String, date: Long, organization: String, title: String, abst: String) {

  def toLecture: Lecture = {
    val spkr = Speaker(speaker, organization)
    Lecture(spkr, title, date, abst)
  }
}

object LectureData {
  implicit val reader = Json.reads[LectureData]
}

@Singleton
class AdminController @Inject()(
                                 authentication: Authentication,
                                 secured: Secured,
                                 participants: Participants,
                                 lectures: Lectures)(implicit exec: ExecutionContext) extends Controller {

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

  def newLecture = secured.async(parse.json[LectureData]) { implicit request =>
    lectures.save(request.body.toLecture).map(lecture => Ok(Json.toJson(lecture)))
  }

  def listLectures = Action.async(lectures.list[List]().map(xs => Ok(Json.toJson(xs))))

  def removeLecture(id: Id) = secured.async(lectures.remove(id).map(_ => Ok))

}
