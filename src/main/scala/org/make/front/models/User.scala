package org.make.front.models

import org.make.front.helpers.UndefToOption.undefToOption
import org.make.client.models.UserResponse

import scala.scalajs.js

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
                language: String)

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
      language = userResponse.language
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
