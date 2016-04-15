/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models

import java.io.InputStream

import scala.concurrent.Future

/**
  * Program pdf file representation
  */
trait ProgramPdf {

  def get(): Future[Array[Byte]]

  def update(): Unit
}
