package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.front.facades.imageLogoMake

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object HeaderComponent {
  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (self) =>
      <.div(^.className := HeaderStyles.container.htmlClass)(
        <.div(^.className := HeaderStyles.header_container.htmlClass)(
          <.a(^.className := HeaderStyles.header_logo_container.htmlClass, ^.href := "/")(
            <.img(^.src := imageLogoMake.toString)()
          ),
          <(SearchInputComponent()).empty,
          <(UserHeaderComponent()).empty
        ),
        <.style()(HeaderStyles.render[String])
    )
  )
}

object HeaderStyles extends StyleSheet.Inline {
  import dsl._

  val container: StyleA = style(
    position.relative,
    zIndex(500),
    height(80.px),
    padding(12.px, 50.px, 0.px, 50.px),
    backgroundColor(c"#fff"),
    boxShadow := "1px 1px 4px 0 rgba(0, 0, 0, 0.5)"
  )

  val header_container: StyleA =
    style(position.relative, width(100.%%), height(100.%%), maxWidth(1140.px), marginRight(auto), marginLeft(auto))

  val header_logo_container: StyleA = style(float.left)
}
