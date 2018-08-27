/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.client.models

import scala.scalajs.js

@js.native
trait UserResponse extends js.Object {
  val userId: String
  val email: String
  val firstName: js.UndefOr[String]
  val lastName: js.UndefOr[String]
  val organisationName: js.UndefOr[String]
  val enabled: Boolean
  val emailVerified: Boolean
  val isOrganisation: Boolean
  val lastConnection: String
  val roles: js.Array[String]
  val profile: js.UndefOr[ProfileResponse]
  val country: String
  val language: String
  val hasPassword: Boolean
}

@js.native
trait ProfileResponse extends js.Object {
  val dateOfBirth: js.UndefOr[String]
  val avatarUrl: js.UndefOr[String]
  val profession: js.UndefOr[String]
  val phoneNumber: js.UndefOr[String]
  val description: js.UndefOr[String]
  val twitterId: js.UndefOr[String]
  val facebookId: js.UndefOr[String]
  val googleId: js.UndefOr[String]
  val genderName: js.UndefOr[String]
  val departmentNumber: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
  val karmaLevel: js.UndefOr[Int]
  val locale: js.UndefOr[String]
  val optInNewsletter: Boolean
}

@js.native
trait AuthorResponse extends js.Object {
  val firstName: js.UndefOr[String]
  val organisationName: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
  val age: js.UndefOr[Int]
  val avatarUrl: js.UndefOr[String]
}

@js.native
trait OrganisationInfoResponse extends js.Object {
  val organisationId: String
  val organisationName: js.UndefOr[String]
}
