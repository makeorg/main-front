package org.make.front.facades

import org.scalajs.dom.DOMParser

object Unescape {
  def unescape(escaped: String): String = {
    new DOMParser().parseFromString(escaped, "text/html").documentElement.textContent
  }
}
