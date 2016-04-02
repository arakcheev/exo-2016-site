/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.Inject
import database.Driver
import models._

import scala.concurrent.{Future, ExecutionContext}

class ParticipantsImpl @Inject()(driver: Driver, implicit val executionContext: ExecutionContext) extends Participants {

  val collection = driver.collection("participants")

  override def emailAvailable(email: String): Future[Boolean] = {
    val query = document("email" -> email)
    collection.find(query).one[Participant].map(_.isDefined)
  }

  override def update(id: Id, participant: Participant): Future[Unit] = ???

  override def phoneAvailable(email: String): Future[Boolean] = {
    val query = document("phone" -> email)
    collection.find(query).one[Participant].map(_.isDefined)
  }

  override def delete(id: Id): Future[Unit] = ???

  override def register(participant: Participant): Future[Unit] = {
    collection.insert(participant).map(_ => ())
  }

  override def list(): Future[List[Participant]] = ???
}
