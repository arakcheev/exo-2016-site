package controllers

import javax.inject._

import models._
import org.joda.time.DateTime
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
                                 workShop: WorkShop)(implicit exec: ExecutionContext) extends Controller {

  def isLogged = secured(Ok)

  def login = Action.async(parse.json[LoginData]) { request =>
    authentication.auth(request.body.login, request.body.password).map {
      case Some(user) => Ok.withSession(authentication.SESSION_NAME -> user._id.stringify)
      case None => BadRequest("Invalid login/password")
    }
  }

  def logout = secured(Ok.withNewSession)

  def getProgram = Action.async {
    for {
      items <- workShop.list[Seq]()
      sessions <- lectures.list[Seq]()
    } yield {

      val grouped: Map[String, Seq[WorkShopItem]] = items.groupBy(_.startDate.getDayOfMonth.toString)

      val program = grouped.mapValues { items =>
        items.map { item =>
          val itemSessions = sessions.filter { s: Lecture =>
            val lectureStartMillis = s.date.getMillis + 1
            val itemStartMillis = item.startDate.getMillis - 1
            val itemEndMillis = item.endDate.getMillis
            /*println(item.title)
            println(s.speaker.fullname)
            println(s" lectureStartMillis ${new DateTime(lectureStartMillis)}")
            println(s" itemStartMillis ${new DateTime(itemStartMillis)}")
            println(s" itemEndMillis ${new DateTime(itemEndMillis)}")
            println(s" result ${lectureStartMillis >= itemStartMillis && lectureStartMillis <= itemEndMillis}")
            println()*/
            lectureStartMillis >= itemStartMillis && lectureStartMillis <= itemEndMillis
          }.sortBy(_.date.getMillis)
          ProgramItem(item, itemSessions)
        }
      }

      val json = Json.toJson(program)

      Ok(json).as(JSON)
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

  def removeParticipant(id: Id) = secured.async(participants.delete(id).map(_ => Ok))
}
