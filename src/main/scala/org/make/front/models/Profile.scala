package org.make.front.models

import java.time.LocalDate

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

case class Profile(dateOfBirth: Option[LocalDate],
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
      dateOfBirth = profileResponse.dateOfBirth.toOption.flatMap { dateOfBirth =>
        dateOfBirth match {
          case _ if Option(dateOfBirth).forall(_.isEmpty) => None
          case _ => Some(LocalDate.parse(dateOfBirth))
        }
      },
      avatarUrl = profileResponse.avatarUrl.toOption,
      profession = profileResponse.profession.toOption,
      phoneNumber = profileResponse.phoneNumber.toOption,
      twitterId = profileResponse.twitterId.toOption,
      facebookId = profileResponse.facebookId.toOption,
      googleId = profileResponse.googleId.toOption,
      gender = profileResponse.genderName.toOption.flatMap { gender =>
        gender match {
          case _ if Option(gender).forall(_.isEmpty) => None
          case _ => Gender.matchGender(gender)
        }
      },
      genderName = profileResponse.genderName.toOption,
      departmentNumber = profileResponse.departmentNumber.toOption,
      karmaLevel = profileResponse.karmaLevel.toOption,
      locale = profileResponse.locale.toOption,
      optInNewsletter = profileResponse.optInNewsletter
    )
  }

  def isEmpty(profile: Profile): Boolean = profile match {
    case Profile(None, None, None, None, None, None, None, None, None, None, None, None, false) => true
    case _                                                                                      => false
  }

  def parseProfile(dateOfBirth: Option[LocalDate] = None,
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
