package org.make.front.models

import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.components.AppState

import scala.scalajs.js

@js.native
trait GradientColor extends js.Object {
  val from: String
  val to: String
}

object GradientColor {
  def apply(from: String, to: String): GradientColor = {
    js.Dynamic.literal(from = from, to = to).asInstanceOf[GradientColor]
  }
}

final case class Theme(id: ThemeId,
                       slug: String,
                       title: String,
                       actionsCount: Int,
                       proposalsCount: Int,
                       order: Int,
                       color: String,
                       gradient: Option[GradientColor] = None,
                       tags: Seq[Tag] = Seq.empty)

@js.native
trait ThemeId extends js.Object {
  val value: String
}

object ThemeId {
  def apply(value: String): ThemeId = {
    js.Dynamic.literal(value = value).asInstanceOf[ThemeId]
  }
}

object Theme {
  def getThemeById(id: String, store: Store[AppState]): Theme = {
    store.getState.themes.find(theme => theme.id.value == id).get
  }
}
