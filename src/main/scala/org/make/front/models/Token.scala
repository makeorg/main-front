package org.make.front.models

import scala.scalajs.js

@js.native
trait Token extends js.Object {
  val token_type: String
  val access_token: String
  val expires_in: Long
  val refresh_token: String
}

object Token {
  def apply(token_type: String, access_token: String, expires_in: Long, refresh_token: String): Token = {
    js.Dynamic.literal(
      token_type = token_type,
      access_token = access_token,
      expires_in = expires_in,
      refresh_token = refresh_token
    ).asInstanceOf[Token]
  }
}
