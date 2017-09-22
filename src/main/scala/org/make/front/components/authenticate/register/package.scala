package org.make.front.components.authenticate

import scala.concurrent.Future
import org.make.front.models.{User => UserModel}

package object register {
  case class RegisterState(fields: Map[String, String], errors: Map[String, String]) {
    def hasError(field: String): Boolean = {
      val maybeErrorMessage = errors.get(field)
      maybeErrorMessage.isEmpty || maybeErrorMessage.contains("")
    }
  }

  object RegisterState {
    val empty = RegisterState(Map(), Map())
  }

  case class RegisterProps(note: String, register: (RegisterState) => Future[UserModel])

}
