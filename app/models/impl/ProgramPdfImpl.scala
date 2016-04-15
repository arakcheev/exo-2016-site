/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import com.google.inject.Inject
import models.{ProgramAPI, ProgramPdf}
import play.api.cache.CacheApi
import services.ProgramPdfBuilder

import scala.async.Async._
import scala.concurrent.{ExecutionContext, Future}

class ProgramPdfImpl @Inject()(programPdfBuilder: ProgramPdfBuilder, cacheApi: CacheApi)(implicit val executionContext: ExecutionContext, programApi: ProgramAPI) extends ProgramPdf {

  val cacheKey = "programpdf"

  override def get(): Future[Array[Byte]] = {
    async {
      cacheApi.get[Array[Byte]](cacheKey) match {
        case Some(pdf) => pdf
        case None =>
          val program = await(programApi.get())
          val pdf = programPdfBuilder.build(program)
          cacheApi.set(cacheKey, pdf)
          pdf
      }
    }
  }

  override def update(): Unit = cacheApi.remove(cacheKey)
}
