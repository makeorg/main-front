package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.front.facades.imageLogoMake
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.styles.{BulmaStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object HeaderComponent {
  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    render = (self) =>
      <.nav(^.className := Seq(BulmaStyles.Components.Navbar.navbar, HeaderStyles.headerContainer))(
        <.div(^.className := BulmaStyles.Components.Navbar.navbarBrand)(
          <.a(^.className := BulmaStyles.Components.Navbar.navbarItem, ^.href := "/")(
            <.img(
              ^.style := Map("maxHeight" -> "100%"),
              ^.src := imageLogoMake.toString,
              ^.height := 56,
              ^.width := 105
            )()
          ),
          <.div(^.className := BulmaStyles.Components.Navbar.navbarBurger)(<.span()(), <.span()(), <.span()())
        ),
        <.div(^.className := BulmaStyles.Components.Navbar.navbarMenu)(
          <.div(^.className := BulmaStyles.Components.Navbar.navbarMenu)(
            <.div(^.className := BulmaStyles.Components.Navbar.navbarStart)(
              <.SearchInputComponent(^.className := BulmaStyles.Components.Navbar.navbarItem)()
            ),
            <.div(^.className := BulmaStyles.Components.Navbar.navbarEnd)(<.UserHeaderContainerComponent.empty)
          )
        ),
        <.style()(HeaderStyles.render[String])
    )
  )
}

object HeaderStyles extends StyleSheet.Inline {
  import dsl._

  val headerRemHeight: Float = 8

  val imageBrand: StyleA = style(maxHeight(100.%%))
  val headerContainer: StyleA = style(
    height(headerRemHeight.rem),
    fontSize(1.6.rem),
    MakeStyles.modeMobile(height(10.rem)),
    MakeStyles.modeDesktop(padding(0.rem, 15.rem)),
    position.sticky,
    zIndex(1000),
    top(0.rem),
    boxShadow := "0 4px 8px 0 rgba(0, 0, 0, 0.5)"
  )
}
