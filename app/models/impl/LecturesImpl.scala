/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.{Inject, Provider}
import database.Driver
import models.{Lectures, _}
import play.api.Logger

import scala.collection.generic.CanBuildFrom
import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

class LecturesImpl(collection: Collection)(implicit val executionContext: ExecutionContext) extends Lectures{

  private val logger = Logger(getClass)

  /**
    * Save new lecture
    */
  override def save(lecture: Lecture): Future[Lecture] = {
    collection.insert(lecture).map { result =>
      if (result.n != 1) throw new Exception(s"Error save new lecture to database. Return number of inserted documents is ${result.n} but must be 1")
      &(lecture)
      lecture
    }
  }

  /**
    * Get all lectures
    * @tparam M collection type to return (ex, List, Seq, Set)
    */
  override def list[M[_]]()(implicit cbf: CanBuildFrom[M[_], Lecture, M[Lecture]]): Future[M[Lecture]] = {
    val query = document()
    collection.find(query).cursor[Lecture]().collect[M]()
  }

  /**
    * Just Remove lecture.
    */
  override def remove(id: Id): Future[Unit] = {
    val query = document("_id" -> id)
    collection.find(query).one[Lecture].flatMap {
      case None => Future((): Unit)
      case Some(lecture) => collection.remove(query).map(_ => &(lecture))
    }
  }

  override def update(id: Id, lecture: Lecture): Future[Lecture] = {
    val query = document("_id" -> id)
    val updater = document("$set" -> lecture.setId(id))
    collection.findAndUpdate(query, updater, fetchNewObject = true).map { data =>
      val lecture = data.result[Lecture].getOrElse(throw new Exception(s"Invalid result in update lecture operation. Expected updater lecture, got ${data.value}"))
      &(lecture)
      lecture
    }
  }
}

class LecturesImplProvider @Inject()(driver: Driver)(implicit val executionContext: ExecutionContext) extends Provider[Lectures] {

  override def get(): Lectures = new LecturesImpl(driver.collection("lectures"))
}
