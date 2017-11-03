package org.make.front.models

import scala.scalajs.js

@js.native
trait TokenResponse extends js.Object {
  val token_type: String
  val access_token: String
  val expires_in: Int
  val refresh_token: String
}

final case class Token(token_type: String, access_token: String, expires_in: Int, refresh_token: String)

object Token {
  def apply(tokenResponse: TokenResponse): Token = {
    Token(
      token_type = tokenResponse.token_type,
      access_token = tokenResponse.access_token,
      expires_in = tokenResponse.expires_in,
      refresh_token = tokenResponse.refresh_token
    )
  }
}
