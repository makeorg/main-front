package org.make.core

import java.util.UUID

import io.circe._

import scala.util.{Failure, Success, Try}

trait CirceFormatters {

  implicit lazy val uuidEncoder: Encoder[UUID] = (a: UUID) => Json.fromString(a.toString)
  implicit lazy val uuidDecoder: Decoder[UUID] =
    Decoder.decodeString.emap { uuid =>
      Try(UUID.fromString(uuid)) match {
        case Success(parsed) => Right(parsed)
        case Failure(_)      => Left(s"$uuid is not a valid uuid")
      }
    }

}

object CirceFormatters extends CirceFormatters
