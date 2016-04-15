/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models.impl

import java.io.InputStream

import com.google.inject.Inject
import models.ProgramPdf
import play.api.cache.CacheApi
import services.ProgramPdfBuilder

import scala.concurrent.{ExecutionContext, Future}

class ProgramPdfImpl @Inject()(programPdfBuilder: ProgramPdfBuilder, cacheApi: CacheApi)(implicit val executionContext: ExecutionContext) extends ProgramPdf {

  val cacheKey = "programpdf"

  override def get(): Future[InputStream] = {
    Future{
      cacheApi.getOrElse(cacheKey) {
        val program = programPdfBuilder.build(???)
        cacheApi.set(cacheKey, program)
        program
      }
    }
  }

  override def update(): Future[Unit] = {
    Future(cacheApi.remove(cacheKey))
  }
}
