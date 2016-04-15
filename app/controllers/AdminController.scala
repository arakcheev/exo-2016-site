package controllers

import javax.inject._

import models._
import models.impl.ProgramAPIImpl
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.mvc._
import security.{Authentication, Secured}

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

case class WorkShopItemData(startDate: Long, endDate: Long, title: String) {
  def toWorkShop = {
    val sd = new DateTime(startDate)
    val ed = new DateTime(endDate)
    WorkShopItem(sd, ed, title)
  }
}

object WorkShopItemData {
  implicit val reader = Json.reads[WorkShopItemData]
}

//TODO:  Move from controller
case class ProgramItem(item: WorkShopItem, sessions: Seq[Lecture])

object ProgramItem {
  implicit val writer = Json.writes[ProgramItem]
}

@Singleton
class AdminController @Inject()(
                                 authentication: Authentication,
                                 secured: Secured,
                                 participants: Participants,
                                 lectures: Lectures,
                                 workShop: WorkShop,
                                 programPdf: ProgramPdf)(implicit exec: ExecutionContext) extends Controller {

  lectures.callback { lecture =>
    programPdf.update()
  }

  workShop.callback { item =>
    programPdf.update()
  }

  private val programAPi = new ProgramAPIImpl(workShop, lectures)

  def isLogged = secured(Ok)

  def login = Action.async(parse.json[LoginData]) { request =>
    authentication.auth(request.body.login, request.body.password).map {
      case Some(user) => Ok.withSession(authentication.SESSION_NAME -> user._id.stringify)
      case None => BadRequest("Invalid login/password")
    }
  }

  def logout = secured(Ok.withNewSession)

  def getProgram = Action.async {
    programAPi.get().map { program =>
      Ok(program.toJson).as(JSON)
    }
  }

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

  def updateLecture(id: Id) = secured.async(parse.json[LectureData]) { implicit request =>
    lectures.update(id, request.body.toLecture).map(lecture => Ok(Json.toJson(lecture)))
  }

  //WorkShop methods

  def listWorkShopItems = Action.async {
    workShop.list[List]().map(xs => Ok(Json.toJson(xs)))
  }

  def newWorkShopItem = secured.async(parse.json[WorkShopItemData]) { request =>
    val item = request.body.toWorkShop
    workShop.save(item).map(saved => Ok(Json.toJson(saved)))
  }

  def updateWorkShopItem(id: Id) = secured.async(parse.json[WorkShopItemData]) { request =>
    val item = request.body.toWorkShop
    workShop.update(id, item).map(item => Ok(Json.toJson(item)))
  }

  def removeWorkShopItem(id: Id) = secured.async(workShop.remove(id).map(_ => Ok))

}
