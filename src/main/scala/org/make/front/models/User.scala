package org.make.front.models

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
    RoleCitizen.shortName -> RoleCitizen
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
}

case class User(userId: UserId,
                email: String,
                firstName: Option[String],
                lastName: Option[String],
                enabled: Boolean,
                verified: Boolean,
                lastConnection: js.Date,
                roles: Seq[Role],
                profile: Option[Profile])

object User {
  def apply(userResponse: UserResponse): User = {
    val seqRoles: Seq[String] = userResponse.roles

    User(
      userId = UserId(userResponse.userId),
      email = userResponse.email,
      firstName = userResponse.firstName.toOption,
      lastName = userResponse.lastName.toOption,
      enabled = userResponse.enabled,
      verified = userResponse.verified,
      lastConnection = new js.Date(userResponse.lastConnection),
      roles = seqRoles.map(Role.apply),
      profile = if (userResponse.profile == null) None else userResponse.profile.toOption.map(Profile.apply)
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
