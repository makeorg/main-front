package org.make.front.facades

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("configuration")
object Configuration extends js.Object {
  val apiUrl: String = js.native
  val googleAppId: String = js.native
  val googleAdWordsId: String = js.native
  val facebookAppId: String = js.native
  val detectedCountry: String = js.native
  val forcedCountry: String = js.native
}
