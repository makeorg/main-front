package org.make.front.models

import scalajs.js

import org.make.front.helpers.UndefToOption.undefToOption
import org.make.client.models.ProfileResponse

sealed trait Gender {
  def shortName: String
}

object Gender {
  val genders: Map[String, Gender] = Map(Male.shortName -> Male, Female.shortName -> Female, Other.shortName -> Other)

  def matchGender(gender: String): Option[Gender] = {
    val maybeGender = genders.get(gender)
    maybeGender
  }

  case object Male extends Gender {
    override val shortName: String = "M"
  }

  case object Female extends Gender {
    override val shortName: String = "F"
  }

  case object Other extends Gender {
    override val shortName: String = "O"
  }
}

case class Profile(dateOfBirth: Option[js.Date],
                   avatarUrl: Option[String],
                   profession: Option[String],
                   phoneNumber: Option[String],
                   twitterId: Option[String],
                   facebookId: Option[String],
                   googleId: Option[String],
                   gender: Option[Gender],
                   genderName: Option[String],
                   departmentNumber: Option[String],
                   karmaLevel: Option[Int],
                   locale: Option[String],
                   optInNewsletter: Boolean = false)

object Profile {
  def apply(profileResponse: ProfileResponse): Profile = {
    Profile(
      dateOfBirth = undefToOption(profileResponse.dateOfBirth).flatMap { dateOfBirth =>
        dateOfBirth match {
          case _ if Option(dateOfBirth).forall(_.isEmpty) => None
          case _                                          => Some(new js.Date(dateOfBirth))
        }
      },
      avatarUrl = undefToOption(profileResponse.avatarUrl),
      profession = undefToOption(profileResponse.profession),
      phoneNumber = undefToOption(profileResponse.phoneNumber),
      twitterId = undefToOption(profileResponse.twitterId),
      facebookId = undefToOption(profileResponse.facebookId),
      googleId = undefToOption(profileResponse.googleId),
      gender = undefToOption(profileResponse.genderName).flatMap { gender =>
        gender match {
          case _ if Option(gender).forall(_.isEmpty) => None
          case _                                     => Gender.matchGender(gender)
        }
      },
      genderName = undefToOption(profileResponse.genderName),
      departmentNumber = undefToOption(profileResponse.departmentNumber),
      karmaLevel = undefToOption(profileResponse.karmaLevel),
      locale = undefToOption(profileResponse.locale),
      optInNewsletter = profileResponse.optInNewsletter
    )
  }

  def isEmpty(profile: Profile): Boolean = profile match {
    case Profile(None, None, None, None, None, None, None, None, None, None, None, None, false) => true
    case _                                                                                      => false
  }

  def parseProfile(dateOfBirth: Option[js.Date] = None,
                   avatarUrl: Option[String] = None,
                   profession: Option[String] = None,
                   phoneNumber: Option[String] = None,
                   twitterId: Option[String] = None,
                   facebookId: Option[String] = None,
                   googleId: Option[String] = None,
                   gender: Option[Gender] = None,
                   genderName: Option[String] = None,
                   departmentNumber: Option[String] = None,
                   karmaLevel: Option[Int] = None,
                   locale: Option[String] = None,
                   optInNewsletter: Boolean = false): Option[Profile] = {

    val profile = Profile(
      dateOfBirth = dateOfBirth,
      avatarUrl = avatarUrl,
      profession = profession,
      phoneNumber = phoneNumber,
      twitterId = twitterId,
      facebookId = facebookId,
      googleId = googleId,
      gender = gender,
      genderName = genderName,
      departmentNumber = departmentNumber,
      karmaLevel = karmaLevel,
      locale = locale,
      optInNewsletter = optInNewsletter
    )
    if (isEmpty(profile)) {
      None
    } else {
      Some(profile)
    }
  }
}
