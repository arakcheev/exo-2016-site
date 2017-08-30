/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

import org.bson.codecs.configuration.{CodecProvider, CodecRegistry}
import org.bson.{BsonReader, BsonType, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.joda.time.DateTime
import org.mongodb.scala.bson.BsonObjectId
import play.api.libs.json._
//import reactivemongo.api.collections.bson.BSONCollection
//import reactivemongo.bson._

import scala.util.Try

package object models {

  type Id = BsonObjectId

  implicit class IdOps(id: Id) extends AnyRef{
    def stringify = id.getValue.toHexString
  }

  type Collection[T] = org.mongodb.scala.MongoCollection[T]

  def newId: Id = BsonObjectId()

  def parseId(id: String): Try[Id] = Try(BsonObjectId.apply(id))

  implicit val bsonObjectIDFormat = Format[Id](
    Reads[Id] {
      case JsString(x) =>
        val maybeOID: Try[Id] = parseId(x)
        if (maybeOID.isSuccess) JsSuccess(maybeOID.get)
        else {
          JsError("Expected BSONObjectID as JsString")
        }
      case _ => JsError("Expected BSONObjectID as JsString")
    },
    Writes[Id] { bson => JsString(bson.getValue.toHexString) }
  )

  implicit val dateTimeCodec = new Codec[DateTime] {
    override def decode(reader: BsonReader, decoderContext: DecoderContext): DateTime = {
      new DateTime(reader.readDateTime())
    }

    override def encode(writer: BsonWriter, value: DateTime, encoderContext: EncoderContext) = {
      writer.writeDateTime(value.getMillis)
    }

    override def getEncoderClass = classOf[DateTime]
  }
}
