package org.make.front.components.mainHeader

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{logoMake, I18n}
import org.make.front.styles._
import org.make.front.styles.base._
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object MainHeader {
  final case class MainHeaderProps(country: String)

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[MainHeaderProps, Unit](
          displayName = "MainHeader",
          shouldComponentUpdate = (_, _, _) => true,
          render = { self =>
            val trackLogoClick: () => Unit = () => {
              TrackingService.track("click-navbar-logo", TrackingContext(TrackingLocation.navBar))
              self.props.history.push(s"/${self.props.wrapped.country}")
            }

            val trackAboutUsClick: () => Unit = () => {
              TrackingService.track("click-navbar-whoarewe", TrackingContext(TrackingLocation.navBar))
              scalajs.js.Dynamic.global.window.open(I18n.t("main-header.menu.item-1.link"), "_blank")
            }

            <.header(^.className := MainHeaderStyles.wrapper)(
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.div(^.className := js.Array(TableLayoutStyles.wrapper, MainHeaderStyles.innerWrapper))(
                  <.div(
                    ^.className := js.Array(TableLayoutStyles.cellVerticalAlignMiddle, MainHeaderStyles.logoWrapper)
                  )(
                    <.button(^.onClick := trackLogoClick)(
                      <.img(
                        ^.className := MainHeaderStyles.logo,
                        ^.src := logoMake.toString,
                        ^.title := I18n.t("main-header.title"),
                        ^.alt := I18n.t("main-header.title"),
                        ^("data-pin-no-hover") := "true"
                      )()
                    )
                  ),
                  <.div(
                    ^.className := js.Array(TableLayoutStyles.cellVerticalAlignMiddle, MainHeaderStyles.searchWrapper)
                  )(<.SearchFormContainer.empty),
                  <.div(
                    ^.className := js.Array(TableLayoutStyles.cellVerticalAlignMiddle, MainHeaderStyles.menusWrapper)
                  )(
                    <.div(^.className := MainHeaderStyles.menusInnerWrapper)(
                      <.nav(^.className := js.Array(RWDRulesMediumStyles.showInlineBlockBeyondMedium))(
                        <.ul()(
                          <.li(^.className := MainHeaderStyles.menuItem)(
                            <.button(
                              ^.onClick := trackAboutUsClick,
                              ^.className := js
                                .Array(MainHeaderStyles.menuItemLink, TextStyles.title, TextStyles.smallText)
                            )(unescape(I18n.t("main-header.menu.item-1.label")))
                          )
                        )
                      ),
                      <.UserNavContainerComponent.empty
                    )
                  )
                )
              ),
              <.div(^.className := MainHeaderStyles.notificationsWrapper)(<.NotificationsComponent.empty),
              <.style()(MainHeaderStyles.render[String])
            )
          }
        )
    )
}

object MainHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, backgroundColor(ThemeStyles.BackgroundColor.white))

  val notificationsWrapper: StyleA =
    style(position.absolute, top(100.%%), left(`0`), width(100.%%))

  val innerWrapper: StyleA =
    style(height(50.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(height(ThemeStyles.mainNavDefaultHeight)))

  val logoWrapper: StyleA =
    style(
      width(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      paddingRight(20.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(width(100.pxToEm()), paddingRight(40.pxToEm()))
    )

  val searchWrapper: StyleA =
    style(width(500.pxToEm()))

  val menusWrapper: StyleA =
    style(textAlign.right, paddingLeft(20.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(paddingLeft(40.pxToEm())))

  val menusInnerWrapper: StyleA =
    style(margin(`0`, (ThemeStyles.SpacingValue.small * -1).pxToEm()), whiteSpace.nowrap)

  val logo: StyleA = style(width(50.pxToEm()), maxWidth.none, ThemeStyles.MediaQueries.beyondSmall(width(90.pxToEm())))

  val menuItem: StyleA =
    style(display.inlineBlock, verticalAlign.baseline, margin(`0`, ThemeStyles.SpacingValue.small.pxToEm()))

  val menuItemLink: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

}
