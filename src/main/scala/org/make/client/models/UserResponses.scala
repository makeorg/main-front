package org.make.client.models


import org.make.front.models.{Profile, Role, UserId}

import scala.scalajs.js

@js.native
trait UserResponse extends js.Object {
  val userId: String
  val email: String
  val firstName: js.UndefOr[String]
  val lastName: js.UndefOr[String]
  val enabled: Boolean
  val verified: Boolean
  val lastConnection: String
  val roles: js.Array[Role]
  val profile: js.UndefOr[Profile]
}

@js.native
trait AuthorResponse extends js.Object {
  val firstName: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
  val age: js.UndefOr[Int]
}
