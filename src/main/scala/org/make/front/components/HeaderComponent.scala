package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object HeaderComponent {
  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div(^.className := HeaderStyles.common.htmlClass)(
        "The header of the page",
        <.style()(
          HeaderStyles.render[String]
        )
      )

  )

}

object HeaderStyles extends StyleSheet.Inline {
  import dsl._

  val common = style(
    backgroundColor.green,
    media.maxWidth(320 px)(
      backgroundColor.red
    )
  )

}