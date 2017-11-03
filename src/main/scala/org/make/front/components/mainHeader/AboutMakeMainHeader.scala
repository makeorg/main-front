package org.make.front.components.mainHeader

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{logoMake, I18n}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RWDHideRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object AboutMakeMainHeader {
  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    displayName = "MainHeader",
    render = (self) =>
      <.header(^.className := AboutMakeMainHeaderStyles.wrapper)(
        <.div(^.className := RowRulesStyles.centeredRow)(
          <.div(^.className := ColRulesStyles.col)(
            <.div(^.className := AboutMakeMainHeaderStyles.innerWrapper)(
              <.p(^.className := AboutMakeMainHeaderStyles.logoWrapper)(
                <.img(
                  ^.className := AboutMakeMainHeaderStyles.logo,
                  ^.src := logoMake.toString,
                  ^.title := I18n.t("about-make-main-header.title"),
                  ^("data-pin-no-hover") := "true"
                )()
              ),
              <.div(^.className := AboutMakeMainHeaderStyles.menusWrapper)(
                <.nav(
                  ^.className := Seq(
                    AboutMakeMainHeaderStyles.menuWrapper,
                    RWDHideRulesStyles.showInlineBlockBeyondMedium
                  )
                )(
                  <.ul()(
                    Range(1, 6).map(
                      item =>
                        <.li(^.className := AboutMakeMainHeaderStyles.menuItem)(
                          <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                            <.a(
                              ^.href := I18n.t(s"about-make-main-header.menu.item-$item.link"),
                              ^.target := "_blank",
                              ^.className := AboutMakeMainHeaderStyles.menuItemLink
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
        <.style()(AboutMakeMainHeaderStyles.render[String])
    )
  )
}

object AboutMakeMainHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      position.fixed,
      top(0.em),
      left(0.em),
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
