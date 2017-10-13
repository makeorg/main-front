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
              /*TODO: h1 if homepage else p*/
              <.h1(^.className := AboutMakeMainHeaderStyles.logoWrapper)(
                <.a(^.href := "/")(
                  <.img(
                    ^.className := AboutMakeMainHeaderStyles.logo,
                    ^.src := logoMake.toString,
                    ^.title := "Make.org",
                    ^("data-pin-no-hover") := "true"
                  )()
                )
              ),
              <.div(^.className := AboutMakeMainHeaderStyles.menusWrapper)(
                <.nav(
                  ^.className := Seq(
                    AboutMakeMainHeaderStyles.menuWrapper,
                    RWDHideRulesStyles.showInlineBlockBeyondMedium
                  )
                )(
                  <.ul()(
                    <.li(^.className := AboutMakeMainHeaderStyles.menuItem)(
                      <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                        <.a(
                          ^.href := "https://about.make.org/qui-sommes-nous",
                          ^.target := "_blank",
                          ^.className := AboutMakeMainHeaderStyles.menuItemLink
                        )(unescape(I18n.t("content.header.presentation")))
                      )
                    ),
                    <.li(^.className := AboutMakeMainHeaderStyles.menuItem)(
                      <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                        <.a(
                          ^.href := "https://about.make.org",
                          ^.target := "_blank",
                          ^.className := AboutMakeMainHeaderStyles.menuItemLink
                        )(unescape("Notre actu"))
                      )
                    ),
                    <.li(^.className := AboutMakeMainHeaderStyles.menuItem)(
                      <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                        <.a(
                          ^.href := "https://about.make.org/category/videos",
                          ^.target := "_blank",
                          ^.className := AboutMakeMainHeaderStyles.menuItemLink
                        )(unescape("Notre équipe"))
                      )
                    ),
                    <.li(^.className := AboutMakeMainHeaderStyles.menuItem)(
                      <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                        <.a(
                          ^.href := "https://about.make.org/category/presse",
                          ^.target := "_blank",
                          ^.className := AboutMakeMainHeaderStyles.menuItemLink
                        )(unescape("Presse"))
                      )
                    ),
                    <.li(^.className := AboutMakeMainHeaderStyles.menuItem)(
                      <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                        <.a(
                          ^.href := "https://about.make.org/jobs",
                          ^.target := "_blank",
                          ^.className := AboutMakeMainHeaderStyles.menuItemLink
                        )(unescape("Nous rejoindre"))
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
    style()

}
