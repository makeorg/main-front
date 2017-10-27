package org.make.front.components.mainHeader

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM.{
  RouterDOMVirtualDOMElements,
  RouterVirtualDOMAttributes
}
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{logoMake, I18n}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RWDHideRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object MainHeader {
  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    displayName = "MainHeader",
    render = (self) =>
      <.header(^.className := MainHeaderStyles.wrapper)(
        <.CookieAlertContainerComponent.empty,
        <.div(^.className := RowRulesStyles.centeredRow)(
          <.div(^.className := ColRulesStyles.col)(
            <.div(^.className := MainHeaderStyles.innerWrapper)(
              /*TODO: h1 if homepage else p*/
              <.h1(^.className := MainHeaderStyles.logoWrapper)(
                <.Link(^.to := "/")(
                  <.img(
                    ^.className := MainHeaderStyles.logo,
                    ^.src := logoMake.toString,
                    ^.title := I18n.t("main-header.title"),
                    ^("data-pin-no-hover") := "true"
                  )()
                )
              ),
              <.div(^.className := MainHeaderStyles.searchWrapper)(<.SearchFormComponent.empty),
              <.div(^.className := MainHeaderStyles.menusWrapper)(
                <.div(^.className := MainHeaderStyles.menusInnerWrapper)(
                  <.nav(
                    ^.className := Seq(MainHeaderStyles.menuWrapper, RWDHideRulesStyles.showInlineBlockBeyondMedium)
                  )(
                    <.ul(^.className := MainHeaderStyles.menu)(
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
          )
        ),
        <.style()(MainHeaderStyles.render[String])
    )
  )
}

object MainHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      position.fixed,
      top(`0`),
      left(`0`),
      width(100.%%),
      zIndex(10),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)"
    )

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

  val searchWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle, width(500.pxToEm()))

  val menusWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      textAlign.right,
      paddingLeft(20.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingLeft(40.pxToEm()))
    )

  val menusInnerWrapper: StyleA =
    style(margin :=! s"0 -${ThemeStyles.SpacingValue.small.pxToEm().value}", whiteSpace.nowrap)

  val menuWrapper: StyleA =
    style(display.inlineBlock)

  val logo: StyleA = style(width(50.pxToEm()), maxWidth.none, ThemeStyles.MediaQueries.beyondSmall(width(90.pxToEm())))

  val menu: StyleA =
    style()

  val menuItem: StyleA =
    style(display.inlineBlock, verticalAlign.baseline, margin :=! s"0 ${ThemeStyles.SpacingValue.small.pxToEm().value}")

  val menuItemLink: StyleA =
    style(color :=! ThemeStyles.ThemeColor.primary)

}
