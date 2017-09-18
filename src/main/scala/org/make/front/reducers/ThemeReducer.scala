package org.make.front.reducers

import org.make.front.actions._
import org.make.front.models.{Theme => ThemeModel}

object ThemeReducer {

  def reduce(maybeThemes: Option[Seq[ThemeModel]], action: Any): Seq[ThemeModel] = {
    val themes = maybeThemes.getOrElse(Seq.empty)
    action match {
      case action: SetThemes => action.themes
      case _                 => themes
    }
  }
}
