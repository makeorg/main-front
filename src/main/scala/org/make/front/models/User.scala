package org.make.front.models

import java.time.ZonedDateTime

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
                lastConnection: ZonedDateTime,
                roles: Seq[Role],
                profile: Option[Profile])


object User {
  def apply(userResponse: UserResponse): User = {
    User(
      userId = UserId(userResponse.userId),
      email = userResponse.email,
      firstName = userResponse.firstName.toOption,
      lastName = userResponse.lastName.toOption,
      enabled = userResponse.enabled,
      verified = userResponse.verified,
      lastConnection = ZonedDateTime.parse(userResponse.lastConnection),
      roles = userResponse.roles,
      profile = userResponse.profile.toOption)
  }
}
trait UserId extends js.Object {
  val value: String
}

object UserId {
  def apply(value: String): UserId = {
    js.Dynamic.literal(value = value).asInstanceOf[UserId]
  }
}
