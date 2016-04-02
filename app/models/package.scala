/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

import java.util.Date

import org.joda.time.DateTime
import play.api.libs.json._
import reactivemongo.bson._

import scala.reflect.runtime.universe._
import scala.util.Try

package object models {

  type Id = BSONObjectID

  def document(elements: Producer[BSONElement]*) = reactivemongo.bson.document(elements: _*)

  def array(values: reactivemongo.bson.Producer[reactivemongo.bson.BSONValue]*) =
    reactivemongo.bson.array(values: _*)

  def newId: Id = reactivemongo.bson.generateId

  implicit val bsonObjectIDFormat = Format[Id](
    Reads[Id] {
      case JsString(x) =>
        val maybeOID: Try[Id] = BSONObjectID.parse(x)
        if (maybeOID.isSuccess) JsSuccess(maybeOID.get)
        else {
          JsError("Expected BSONObjectID as JsString")
        }
      case _ => JsError("Expected BSONObjectID as JsString")
    },
    Writes[Id] { bson => JsString(bson.stringify) }
  )

  implicit def MapBSONReader(implicit reader: BSONReader[_ <: BSONValue, String]): BSONDocumentReader[Map[String, String]] =
    new BSONDocumentReader[Map[String, String]] {
      def read(doc: BSONDocument): Map[String, String] = {
        doc.elements.collect {
          case (key, value) => value.seeAsOpt[String](reader) map {
            ov => (key, ov)
          }
        }.flatten.toMap
      }
    }

  implicit def MapBSONWriter(implicit writer: BSONWriter[String, _ <: BSONValue]): BSONDocumentWriter[Map[String, String]] = new BSONDocumentWriter[Map[String, String]] {
    def write(doc: Map[String, String]): BSONDocument = {
      BSONDocument(doc.toTraversable map (t => (t._1, writer.write(t._2))))
    }
  }

  implicit val dateTimeHandler = new BSONHandler[BSONLong, DateTime] {
    override def write(t: DateTime): BSONLong = BSONLong(t.getMillis)

    override def read(bson: BSONLong): DateTime = new DateTime(bson.value)
  }

  //  def DBHandler[A: WeakTypeTag] = Macros.handler[A]
}
