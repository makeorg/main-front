package org.make.services.user

import java.time.LocalDate

import org.make.front.models.User

final case class RegisterUserRequest(email: String,
                                     password: String,
                                     dateOfBirth: LocalDate = LocalDate.now,
                                     firstName: String = "",
                                     lastName: String = "")
