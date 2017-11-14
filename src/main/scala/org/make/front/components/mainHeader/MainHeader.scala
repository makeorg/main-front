package org.make.front.components.mainHeader

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM.{
  RouterDOMVirtualDOMElements,
  RouterVirtualDOMAttributes
}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{logoMake, I18n}
import org.make.front.styles._
import org.make.front.styles.base._
import org.make.front.styles.utils._

object MainHeader {
  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "MainHeader",
        render = (self) => {
          <.header(^.className := MainHeaderStyles.wrapper)(
            <.CookieAlertContainerComponent.empty,
            <.div(^.className := LayoutRulesStyles.centeredRow)(
              <.div(^.className := Seq(TableLayoutStyles.wrapper, MainHeaderStyles.innerWrapper))(
                <.p(^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, MainHeaderStyles.logoWrapper))(
                  <.Link(^.to := "/")(
                    <.img(
                      ^.className := MainHeaderStyles.logo,
                      ^.src := logoMake.toString,
                      ^.title := I18n.t("main-header.title"),
                      ^.alt := I18n.t("main-header.title"),
                      ^("data-pin-no-hover") := "true"
                    )()
                  )
                ),
                <.div(^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, MainHeaderStyles.searchWrapper))(
                  <.SearchFormComponent.empty
                ),
                <.div(^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, MainHeaderStyles.menusWrapper))(
                  <.div(^.className := MainHeaderStyles.menusInnerWrapper)(
                    <.nav(^.className := Seq(RWDHideRulesStyles.showInlineBlockBeyondMedium))(
                      <.ul()(
                        <.li(^.className := MainHeaderStyles.menuItem)(
                          <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                            <.a(
                              ^.href := I18n.t("main-header.menu.item-1.link"),
                              ^.target := "_blank",
                              ^.className := MainHeaderStyles.menuItemLink
                            )(unescape(I18n.t("main-header.menu.item-1.label")))
                          )
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
      width(80.pxToEm()),
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
