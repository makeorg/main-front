package org.make.front.helpers

object Validation {
  private val emailRegex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r

  def isValidEmail(value: String): Boolean = value match{
    case null                                                  => false
    case value if value.trim.isEmpty                           => false
    case value if emailRegex.findFirstMatchIn(value).isDefined => true
    case _                                                     => false
  }
}
