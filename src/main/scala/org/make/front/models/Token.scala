package org.make.front.models

final case class Token(token_type: String, access_token: String, expires_in: Long, refresh_token: String)
