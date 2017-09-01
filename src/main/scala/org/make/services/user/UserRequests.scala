package org.make.services.user

import java.time.LocalDate

final case class RegisterUserRequest(email: String,
                                     password: String,
                                     firstName: String,
                                     lastName: Option[String] = None,
                                     dateOfBirth: Option[LocalDate] = None,
                                     profession: Option[String] = None,
                                     postalCode: Option[String] = None)
