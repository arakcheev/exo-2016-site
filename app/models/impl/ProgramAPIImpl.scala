/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.{Provider, Inject}
import models.{Lectures, WorkShop, Program, ProgramAPI}

import scala.async.Async._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ProgramAPIImpl(workShop: WorkShop, lectures: Lectures) extends ProgramAPI {
  override def get(): Future[Program] = {
    async(new Program(await(workShop.list[List]()), await(lectures.list[List]())))
  }
}

class ProgramAPIImplProvider @Inject()(workShop: WorkShop, lectures: Lectures) extends Provider[ProgramAPI] {
  override def get(): ProgramAPI = new ProgramAPIImpl(workShop, lectures)
}
