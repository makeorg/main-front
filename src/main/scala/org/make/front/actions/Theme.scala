package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action
import org.make.front.models.Theme

final case class SetThemes(themes: Seq[Theme]) extends Action
case object LoadThemes extends Action
