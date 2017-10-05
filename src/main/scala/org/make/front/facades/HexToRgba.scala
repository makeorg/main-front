package org.make.front.facades

import scala.scalajs.js.annotation.JSImport
import scalajs.js

@js.native
@JSImport("hex-to-rgba", JSImport.Default)
object HexToRgba extends js.Any {
  def apply(rgb: String): String = js.native
  def apply(rgb: String, opacity: Float): String = js.native
}
