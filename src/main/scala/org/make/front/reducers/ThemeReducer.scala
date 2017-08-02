package org.make.front.reducers

import org.make.front.actions._
import org.make.front.models.Theme

object ThemeReducer {

  def reduce(maybeThemes: Option[Seq[Theme]], action: Any): Seq[Theme] = {
    val themes = maybeThemes.getOrElse(Seq.empty)
    action match {
      case action: SetThemes => action.themes
      case _                 => themes
    }
  }
}
