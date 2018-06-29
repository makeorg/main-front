/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.error

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object Error {

  case class ErrorProps(redirectToRandomTheme: () => Unit)

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ErrorProps, Unit](
          displayName = "Error",
          render = { self =>
            <("error")()(
              <.div(^.className := js.Array(TableLayoutStyles.fullHeightWrapper))(
                <.div(^.className := TableLayoutStyles.row)(
                  <.div(^.className := js.Array(TableLayoutStyles.cell, ErrorStyles.mainHeaderWrapper))(
                    <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
                    <.div(^.className := ErrorStyles.fixedMainHeaderWrapper)(
                      <.CookieAlertContainerComponent.empty,
                      <.MainHeaderContainer.empty
                    )
                  )
                ),
                <.div(^.className := js.Array(TableLayoutStyles.row, ErrorStyles.fullHeight))(
                  <.div(^.className := js.Array(TableLayoutStyles.cell, ErrorStyles.articleCell))(
                    <.div(^.className := js.Array(LayoutRulesStyles.centeredRow, ErrorStyles.fullHeight))(
                      <.article(^.className := ErrorStyles.article)(
                        <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
                          <.div(
                            ^.className := js
                              .Array(TableLayoutStyles.cellVerticalAlignMiddle, ErrorStyles.articleInnerWrapper)
                          )(
                            <.div(^.className := LayoutRulesStyles.row)(
                              <.p(^.className := js.Array(ErrorStyles.intro, TextStyles.mediumIntro))(
                                unescape(I18n.t("error.intro"))
                              ),
                              <.div(^.className := ErrorStyles.titleWrapper)(
                                <.h1(^.className := TextStyles.veryBigTitle)(unescape(I18n.t("error.title")))
                              ),
                              <.div(^.className := ErrorStyles.textWrapper)(
                                <.p(^.className := js.Array(ErrorStyles.text, TextStyles.mediumText))(
                                  unescape(I18n.t("error.recherche-intro"))
                                )
                              ),
                              <.div(^.className := ErrorStyles.searchFormWrapper)(<.SearchFormContainer.empty),
                              <.div(
                                ^.className := js.Array(
                                  RWDRulesMediumStyles.showBlockBeyondMedium,
                                  LayoutRulesStyles.evenNarrowerCenteredRow
                                )
                              )(<.hr(^.className := ErrorStyles.separatorLine)()),
                              <.div(^.className := ErrorStyles.textWrapper)(
                                <.p(^.className := js.Array(ErrorStyles.text, TextStyles.mediumText))(
                                  unescape(I18n.t("error.redirection-intro"))
                                )
                              ),
                              <.ul(^.className := ErrorStyles.ctasWrapper)(
                                <.li(^.className := ErrorStyles.ctaWrapper)(
                                  <.Link(^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA), ^.to := s"/")(
                                    <.i(^.className := FontAwesomeStyles.home)(),
                                    unescape("&nbsp;" + I18n.t("error.redirect-to-home"))
                                  )
                                ),
                                <.li(^.className := ErrorStyles.ctaWrapper)(
                                  <.button(
                                    ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton),
                                    ^.onClick := self.props.wrapped.redirectToRandomTheme
                                  )(
                                    <.i(^.className := FontAwesomeStyles.random)(),
                                    unescape("&nbsp;" + I18n.t("error.redirect-to-random-theme"))
                                  )
                                )
                              )
                            )
                          )
                        )
                      )
                    )
                  )
                )
              ),
              <.NavInThemesContainerComponent.empty,
              <.style()(ErrorStyles.render[String])
            )
          }
        )
    )
}

object ErrorStyles extends StyleSheet.Inline {

  import dsl._

  val fullHeight: StyleA =
    style(height(100.%%))

  val mainHeaderWrapper: StyleA =
    style(
      paddingBottom(50.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
    )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val articleCell: StyleA =
    style(verticalAlign.middle, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`))

  val article: StyleA =
    style(height(100.%%), backgroundColor(ThemeStyles.BackgroundColor.lightBlueGrey), textAlign.center)

  val articleInnerWrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(
        paddingTop(ThemeStyles.SpacingValue.large.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.large.pxToEm())
      )
    )

  val intro: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val titleWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(ThemeStyles.SpacingValue.large.pxToEm()))
    )

  val textWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(
        marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
        marginBottom(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val text: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val searchFormWrapper: StyleA =
    style(maxWidth(940.pxToEm()), margin(ThemeStyles.SpacingValue.medium.pxToEm(), auto))

  val ctasWrapper: StyleA = style(margin((-1 * ThemeStyles.SpacingValue.smaller).pxToEm()))

  val ctaWrapper: StyleA =
    style(display.inlineBlock, verticalAlign.middle, padding(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val separatorLine: StyleA =
    style(
      height(1.px),
      width(100.%%),
      margin(ThemeStyles.SpacingValue.largerMedium.pxToEm(), `0`),
      border.none,
      backgroundColor(ThemeStyles.BorderColor.veryLight)
    )
}
