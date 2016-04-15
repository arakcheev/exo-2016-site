/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import models.{ProgramAPI, ProgramPdf}
import play.api.cache.CacheApi
import services.ProgramPdfBuilder

import scala.async.Async._
import scala.concurrent.{ExecutionContext, Future}

class ProgramPdfImpl @Inject()(programPdfBuilder: ProgramPdfBuilder, cacheApi: CacheApi, programApi: ProgramAPI)(implicit val executionContext: ExecutionContext) extends ProgramPdf with LazyLogging {

  val cacheKey = "programpdf"

  override def get(): Future[Array[Byte]] = {
    async {
      cacheApi.get[Array[Byte]](cacheKey) match {
        case Some(pdf) => pdf
        case None =>
          logger.debug(" Generate pdf")
          val program = await(programApi.get())
          val pdf = programPdfBuilder.build(program)
          cacheApi.set(cacheKey, pdf)
          pdf
      }
    }
  }

  override def update(): Unit = {
    logger.debug(" Remove cache")
    cacheApi.remove(cacheKey)
  }
}
