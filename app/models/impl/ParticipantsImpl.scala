/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.{Inject, Provider}
import com.mongodb.client.model.{FindOneAndUpdateOptions, ReturnDocument}
import database.Driver
import models._
import org.bson.BsonDocument

import scala.concurrent.{ExecutionContext, Future}

class ParticipantsImpl @Inject()(collection: Collection[Participant], implicit val executionContext: ExecutionContext) extends Participants {

  override def emailAvailable(email: String): Future[Boolean] = {
    import org.mongodb.scala.model.Filters._
    val query = equal("email", email)
    collection.find(query).head().map(_ == null)
  }

  override def update(id: Id, name: String, surname: String, middleName: String,
                      organization: String, age: String, position: String): Future[Participant] = {
    import org.mongodb.scala.model.Filters._
    import org.mongodb.scala.model.Updates._
    val query = equal("_id", id)
    val updater = and(
                       set("name", name),
                       set("surname", surname),
                       set("lastname", middleName),
                       set("organization" , organization),
                       set("age" , age),
                       set("position" , position)
                     )
    val opts = new FindOneAndUpdateOptions()
    opts.returnDocument(ReturnDocument.AFTER)
    collection.findOneAndUpdate(query, updater, opts).toFuture.map { participant =>
      if(participant == null) throw new RuntimeException("Error update participant.")
      participant
    }
  }

  override def phoneAvailable(phone: String): Future[Boolean] = {
    import org.mongodb.scala.model.Filters._
    val query = equal("phone", phone)
    collection.find(query).head().map(_ == null)
  }

  override def delete(id: Id): Future[Unit] = {
    import org.mongodb.scala.model.Filters._
    collection.deleteOne(equal("_id", id)).toFuture.map(_ => ())
  }

  override def register(participant: Participant): Future[Unit] = {
    collection.insertOne(participant).toFuture.map(_ => ())
  }

  override def list(): Future[List[Participant]] = {
    collection.find(new BsonDocument()).toFuture().map(_.toList)
  }
}


class ParticipantsImplProvider @Inject()(driver: Driver)(implicit val executionContext: ExecutionContext) extends Provider[Participants] {

  val collection: Collection[Participant] = driver.database.withCodecRegistry(Participant.codecRegistry).getCollection[Participant]("participants")

  override def get(): Participants = new ParticipantsImpl(collection, executionContext)
}