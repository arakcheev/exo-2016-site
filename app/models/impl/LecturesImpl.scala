/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.{Inject, Provider}
import database.{CrudOps, Driver}
import models.{Lectures, _}

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

class LecturesImpl(val collection: Collection[Lecture])(implicit val executionContext: ExecutionContext) extends Lectures{
}

class LecturesImplProvider @Inject()(driver: Driver)(implicit val executionContext: ExecutionContext) extends Provider[Lectures] {

  val collection = driver.getCollection[Lecture]("lectures", Lecture)

  override def get(): Lectures = new LecturesImpl(collection)
}
