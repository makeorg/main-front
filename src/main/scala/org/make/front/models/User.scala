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

package org.make.front.models

import org.make.front.helpers.UndefToOption.undefToOption

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
  val followedUsers: js.Array[String]
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
  val organisationSlug: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
  val age: js.UndefOr[Int]
  val avatarUrl: js.UndefOr[String]
}

@js.native
trait OrganisationInfoResponse extends js.Object {
  val organisationId: String
  val organisationName: js.UndefOr[String]
  val organisationSlug: js.UndefOr[String]
}

sealed trait Role {
  def shortName: String
}

object Role {
  val roles: Map[String, Role] = Map(
    RoleAdmin.shortName -> RoleAdmin,
    RoleModerator.shortName -> RoleModerator,
    RolePolitical.shortName -> RolePolitical,
    RoleCitizen.shortName -> RoleCitizen,
    RoleActor.shortName -> RoleActor
  )

  def apply(roleName: String): Role = {
    roles(roleName)
  }

  def matchRole(role: String): Option[Role] = {
    val maybeRole = roles.get(role)
    maybeRole
  }

  case object RoleAdmin extends Role {
    val shortName: String = "ROLE_ADMIN"
  }

  case object RoleModerator extends Role {
    val shortName: String = "ROLE_MODERATOR"
  }

  case object RolePolitical extends Role {
    val shortName: String = "ROLE_POLITICAL"
  }

  case object RoleCitizen extends Role {
    val shortName: String = "ROLE_CITIZEN"
  }

  case object RoleActor extends Role {
    val shortName: String = "ROLE_ACTOR"
  }
}

case class User(userId: UserId,
                email: String,
                firstName: Option[String],
                lastName: Option[String],
                organisationName: Option[String],
                enabled: Boolean,
                emailVerified: Boolean,
                isOrganisation: Boolean,
                lastConnection: js.Date,
                roles: js.Array[Role],
                profile: Option[Profile],
                country: String,
                language: String,
                hasPassword: Boolean,
                followedUsers: Seq[UserId])

object User {
  def apply(userResponse: UserResponse): User = {
    val seqRoles: js.Array[String] = userResponse.roles

    User(
      userId = UserId(userResponse.userId),
      email = userResponse.email,
      firstName = undefToOption(userResponse.firstName),
      lastName = undefToOption(userResponse.lastName),
      organisationName = undefToOption(userResponse.organisationName),
      enabled = userResponse.enabled,
      emailVerified = userResponse.emailVerified,
      isOrganisation = userResponse.isOrganisation,
      lastConnection = new js.Date(userResponse.lastConnection),
      roles = seqRoles.map(Role.apply),
      profile = undefToOption(userResponse.profile).map(Profile.apply),
      country = userResponse.country,
      language = userResponse.language,
      hasPassword = userResponse.hasPassword,
      followedUsers = userResponse.followedUsers.map(UserId.apply)
    )
  }
}

@js.native
trait UserId extends js.Object {
  val value: String
}

object UserId {
  def apply(value: String): UserId = {
    js.Dynamic.literal(value = value).asInstanceOf[UserId]
  }
}
