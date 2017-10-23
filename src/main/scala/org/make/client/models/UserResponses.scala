package org.make.client.models

import java.time.ZonedDateTime

import org.make.front.models.{Profile, Role, UserId}

import scala.scalajs.js

@js.native
trait UserResponse extends js.Object {
  val userId: UserId
  val email: String
  val firstName: Option[String]
  val lastName: Option[String]
  val enabled: Boolean
  val verified: Boolean
  val lastConnection: ZonedDateTime
  val roles: Seq[Role]
  val profile: Option[Profile]
}

@js.native
trait AuthorResponse extends js.Object {
  val firstName: Option[String]
  val postalCode: Option[String]
  val age: Option[Int]
}
