package org.make.core

import io.circe.java8.time.TimeInstances
import io.circe.{Decoder, Encoder, Json}
import org.make.front.models.{
  Gender        => GenderModel,
  Profile       => ProfileModel,
  Role          => RoleModel,
  Token         => TokenModel,
  User          => UserModel,
  Vote          => VoteModel,
  Qualification => QualificationModel
}
import org.make.services.proposal.ProposalResponses.RegisterProposalResponse

trait CirceClassFormatters extends TimeInstances {
  implicit lazy val userDecoder: Decoder[UserModel] =
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
    )(UserModel.apply)

  implicit lazy val registerProposalResponseDecoder: Decoder[RegisterProposalResponse] =
    Decoder.forProduct1("proposalId")(RegisterProposalResponse.apply)

  implicit lazy val roleEncoder: Encoder[RoleModel] = (role: RoleModel) => Json.fromString(role.shortName)
  implicit lazy val roleDecoder: Decoder[RoleModel] =
    Decoder.decodeString.emap(
      maybeRole =>
        RoleModel.matchRole(maybeRole) match {
          case Some(role) => Right(role)
          case _          => Left(s"$maybeRole is not a role")
      }
    )

  implicit lazy val tokenDecoder: Decoder[TokenModel] =
    Decoder.forProduct4("token_type", "access_token", "expires_in", "refresh_token")(TokenModel.apply)

  implicit lazy val profileDecoder: Decoder[ProfileModel] =
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
    )(ProfileModel.apply)

  implicit lazy val genderEncoder: Encoder[GenderModel] = (gender: GenderModel) => Json.fromString(gender.shortName)
  implicit lazy val genderDecoder: Decoder[GenderModel] =
    Decoder.decodeString.emap(
      maybeGender =>
        GenderModel.matchGender(maybeGender) match {
          case Some(gender) => Right(gender)
          case _            => Left(s"$maybeGender is not a gender")
      }
    )

  implicit lazy val vote: Decoder[VoteModel] =
    Decoder.forProduct4("key", "count", "qualifications", "selected")(VoteModel.apply)

  implicit lazy val qualification: Decoder[QualificationModel] =
    Decoder.forProduct3("key", "count", "selected")(QualificationModel.apply)
}
