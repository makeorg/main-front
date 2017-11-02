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

object CoreMainHeader {
  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "CoreMainHeader",
        render = (self) => {
          <.header(^.className := CoreMainHeaderStyles.wrapper)(
            <.CookieAlertContainerComponent.empty,
            <.div(^.className := RowRulesStyles.centeredRow)(
              <.div(^.className := ColRulesStyles.col)(
                <.div(^.className := CoreMainHeaderStyles.innerWrapper)(
                  <.p(^.className := CoreMainHeaderStyles.logoWrapper)(
                    <.Link(^.to := "/")(
                      <.img(
                        ^.className := CoreMainHeaderStyles.logo,
                        ^.src := logoMake.toString,
                        ^.title := I18n.t("main-header.title"),
                        ^.alt := I18n.t("main-header.title"),
                        ^("data-pin-no-hover") := "true"
                      )()
                    )
                  ),
                  <.div(^.className := CoreMainHeaderStyles.searchWrapper)(<.SearchFormComponent.empty),
                  <.div(^.className := CoreMainHeaderStyles.menusWrapper)(
                    <.div(^.className := CoreMainHeaderStyles.menusInnerWrapper)(
                      <.nav(
                        ^.className := Seq(
                          CoreMainHeaderStyles.menuWrapper,
                          RWDHideRulesStyles.showInlineBlockBeyondMedium
                        )
                      )(
                        <.ul(^.className := CoreMainHeaderStyles.menu)(
                          <.li(^.className := CoreMainHeaderStyles.menuItem)(
                            <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                              <.a(
                                ^.href := I18n.t("main-header.menu.item-1.link"),
                                ^.target := "_blank",
                                ^.className := CoreMainHeaderStyles.menuItemLink
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
            <.div(^.className := CoreMainHeaderStyles.notificationsWrapper)(<.NotificationsComponent.empty),
            <.style()(CoreMainHeaderStyles.render[String])
          )
        }
      )
}

object CoreMainHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, backgroundColor(ThemeStyles.BackgroundColor.white))

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
