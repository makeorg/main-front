package org.make.front.components.mainHeader

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{logoMake, I18n}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RWDHideRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

object MainHeaderWithStaticLinks {
  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "MainHeaderWithStaticLinks",
        render = (self) => {
          <.header(^.className := MainHeaderWithStaticLinksStyles.wrapper)(
            <.CookieAlertContainerComponent.empty,
            <.div(^.className := RowRulesStyles.centeredRow)(
              <.div(^.className := ColRulesStyles.col)(
                <.div(^.className := MainHeaderWithStaticLinksStyles.innerWrapper)(
                  <.p(^.className := MainHeaderWithStaticLinksStyles.logoWrapper)(
                    <.img(
                      ^.className := MainHeaderWithStaticLinksStyles.logo,
                      ^.src := logoMake.toString,
                      ^.title := I18n.t("about-make-main-header.title"),
                      ^("data-pin-no-hover") := "true"
                    )()
                  ),
                  <.div(^.className := MainHeaderWithStaticLinksStyles.menusWrapper)(
                    <.nav(
                      ^.className := Seq(
                        MainHeaderWithStaticLinksStyles.menuWrapper,
                        RWDHideRulesStyles.showInlineBlockBeyondMedium
                      )
                    )(
                      <.ul()(
                        Range(1, 6).map(
                          item =>
                            <.li(^.className := MainHeaderWithStaticLinksStyles.menuItem)(
                              <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                                <.a(
                                  ^.href := I18n.t(s"about-make-main-header.menu.item-$item.link"),
                                  ^.target := "_blank",
                                  ^.className := MainHeaderWithStaticLinksStyles.menuItemLink
                                )(unescape(I18n.t(s"about-make-main-header.menu.item-$item.label")))
                              )
                          )
                        )
                      )
                    )
                  )
                )
              )
            ),
            <.div(^.className := MainHeaderWithStaticLinksStyles.notificationsWrapper)(<.NotificationsComponent.empty),
            <.style()(MainHeaderWithStaticLinksStyles.render[String])
          )
        }
      )
}

object MainHeaderWithStaticLinksStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.white))

  val notificationsWrapper: StyleA =
    style(position.absolute, top(100.%%), left(`0`), width(100.%%))

  val innerWrapper: StyleA =
    style(
      display.table,
      width(100.%%),
      height(50.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(height(ThemeStyles.mainNavDefaultHeight))
    )

  val logoWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      width(80.pxToEm()),
      paddingRight(20.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(width(100.pxToEm()), paddingRight(40.pxToEm()))
    )
  val logo: StyleA = style(width(50.pxToEm()), maxWidth.none, ThemeStyles.MediaQueries.beyondSmall(width(90.pxToEm())))

  val menusWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val menuWrapper: StyleA =
    style(display.inlineBlock, margin :=! s"0 -${ThemeStyles.SpacingValue.small.pxToEm().value}")

  val menuItem: StyleA =
    style(display.inlineBlock, verticalAlign.baseline, margin :=! s"0 ${ThemeStyles.SpacingValue.small.pxToEm().value}")

  val menuItemLink: StyleA =
    style(color :=! inherit, transition := "color .2s ease-in-out", (&.hover)(color :=! ThemeStyles.ThemeColor.primary))

}
