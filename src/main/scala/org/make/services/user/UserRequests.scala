package org.make.services.user

import java.time.LocalDate

import org.make.front.models.User

final case class RegisterUserRequest(email: String,
                                     password: String,
                                     dateOfBirth: Option[LocalDate],
                                     firstName: Option[String],
                                     lastName: Option[String])
object RegisterUserRequest {
  def apply(user: User): RegisterUserRequest = RegisterUserRequest(
    email = user.email,
    password = user.hashedPassword.get,
    dateOfBirth = user.profile.flatMap(_.dateOfBirth),
    firstName = user.firstName,
    lastName = user.lastName
  )
}
