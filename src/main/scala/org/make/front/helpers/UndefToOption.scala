package org.make.front.helpers

import scala.scalajs.js

object UndefToOption {
  def undefToOption[T](undef: js.UndefOr[T]): Option[T] = {
    Option(undef).flatMap(_.toOption)
  }
}
