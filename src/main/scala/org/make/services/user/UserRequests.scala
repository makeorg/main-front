package org.make.services.user

import java.time.LocalDate

import org.make.front.models.User

final case class RegisterUserRequest(email: String,
                                     password: String,
                                     dateOfBirth: Option[LocalDate] = None,
                                     firstName: Option[String] = None,
                                     lastName: Option[String] = None)
