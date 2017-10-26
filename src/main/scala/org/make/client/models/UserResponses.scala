package org.make.client.models

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
  val roles: js.Array[String]
  val profile: js.UndefOr[ProfileResponse]
}

@js.native
trait ProfileResponse extends js.Object {
  val dateOfBirth: js.UndefOr[String]
  val avatarUrl: js.UndefOr[String]
  val profession: js.UndefOr[String]
  val phoneNumber: js.UndefOr[String]
  val twitterId: js.UndefOr[String]
  val facebookId: js.UndefOr[String]
  val googleId: js.UndefOr[String]
  val genderName: js.UndefOr[String]
  val departmentNumber: js.UndefOr[String]
  val karmaLevel: js.UndefOr[Int]
  val locale: js.UndefOr[String]
  val optInNewsletter: Boolean
}

@js.native
trait AuthorResponse extends js.Object {
  val firstName: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
  val age: js.UndefOr[Int]
}
