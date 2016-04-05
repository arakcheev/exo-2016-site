/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */
import models._
import play.api.mvc.PathBindable
import reactivemongo.bson.BSONObjectID

import scala.util.{Failure, Success}

package object binders {

  implicit object BSONObjectIDPathBindable extends PathBindable[Id] {
    val b = implicitly[PathBindable[String]]

    def bind(key: String, value: String): Either[String, Id] = {
      b.bind(key, value).right.flatMap { v =>
        BSONObjectID.parse(v) match {
          case Success(bson) => Right(bson)
          case Failure(ex) => Left("Error parse id from request.")
        }
      }
    }


    def unbind(key: String, value: Id): String =
      b.unbind(key, value.stringify)
  }

}