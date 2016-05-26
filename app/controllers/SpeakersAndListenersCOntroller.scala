/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package controllers

import com.google.inject.Inject
import models.{Lecture, Lectures, Participant, Participants}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global

private case class SpeakerJson(name: String, organization: String)

private object SpeakerJson {
  implicit val writer = Json.writes[SpeakerJson]

  def apply(lecture: Lecture): SpeakerJson =
    new SpeakerJson(lecture.speaker.fullname, lecture.speaker.organization)
}

private case class ListenerJson(name: String, organization: String)

private object ListenerJson {
  implicit val writer = Json.writes[ListenerJson]

  def apply(participant: Participant): ListenerJson = {
    val name = s"${participant.name} ${participant.surname}"
    new ListenerJson(name, participant.organization)
  }
}

class SpeakersAndListenersController @Inject()(participants: Participants,
                                               lectures: Lectures) extends Controller {


  def speakers = Action.async { implicit request =>
    lectures.list[List]().map { lectures =>
      val json = Json.toJson(lectures.map(SpeakerJson.apply))
      Ok(json).as(JSON)
    }
  }

  def listeners = Action.async { implicit request =>
    participants.list().map { participants =>
      val json = Json.toJson(participants.map(ListenerJson.apply))
      Ok(json).as(JSON)
    }
  }
}
