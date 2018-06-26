/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
