package org.make.core

import io.circe.{Decoder, Encoder, Json}
import io.circe.java8.time.TimeInstances
import org.make.front.models._
import org.make.services.proposal.ProposalResponses.RegisterProposalResponse

trait CirceClassFormatters extends TimeInstances {
  implicit lazy val userDecoder: Decoder[User] =
    Decoder.forProduct9(
      "userId",
      "email",
      "firstName",
      "lastName",
      "enabled",
      "verified",
      "lastConnection",
      "roles",
      "profile"
    )(User.apply)

  implicit lazy val registerProposalResponseDecoder: Decoder[RegisterProposalResponse] =
    Decoder.forProduct1("proposalId")(RegisterProposalResponse.apply)

  implicit lazy val roleEncoder: Encoder[Role] = (role: Role) => Json.fromString(role.shortName)
  implicit lazy val roleDecoder: Decoder[Role] =
    Decoder.decodeString.emap(
      maybeRole =>
        Role.matchRole(maybeRole) match {
          case Some(role) => Right(role)
          case _          => Left(s"$maybeRole is not a role")
      }
    )

  implicit lazy val tokenDecoder: Decoder[Token] =
    Decoder.forProduct4("token_type", "access_token", "expires_in", "refresh_token")(Token.apply)

  implicit lazy val profileDecoder: Decoder[Profile] =
    Decoder.forProduct13(
      "dateOfBirth",
      "avatarUrl",
      "profession",
      "phoneNumber",
      "twitterId",
      "facebookId",
      "googleId",
      "gender",
      "genderName",
      "departmentNumber",
      "karmaLevel",
      "locale",
      "optInNewsletter"
    )(Profile.apply)

  implicit lazy val genderEncoder: Encoder[Gender] = (gender: Gender) => Json.fromString(gender.shortName)
  implicit lazy val genderDecoder: Decoder[Gender] =
    Decoder.decodeString.emap(
      maybeGender =>
        Gender.matchGender(maybeGender) match {
          case Some(gender) => Right(gender)
          case _            => Left(s"$maybeGender is not a gender")
      }
    )

}
