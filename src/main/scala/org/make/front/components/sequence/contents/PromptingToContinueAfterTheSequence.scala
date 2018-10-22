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

package org.make.front.components.sequence.contents
import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.share.ShareProposal.ShareProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  GradientColor     => GradientColorModel,
  OperationExpanded => OperationModel,
  OperationWording  => OperationWordingModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TextStyles, _}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object PromptingToContinueAfterTheSequence {

  final case class PromptingToContinueAfterTheSequenceProps(operation: OperationModel,
                                                            clickOnButtonHandler: () => Unit,
                                                            language: String)

  final case class PromptingToContinueAfterTheSequenceState()

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToContinueAfterTheSequenceProps, PromptingToContinueAfterTheSequenceState](
        displayName = "PromptingToContinueAfterTheSequence",
        getInitialState = { _ =>
          PromptingToContinueAfterTheSequenceState()
        },
        render = { self =>
          val gradientValues: GradientColorModel =
            self.props.wrapped.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicPromptingToContinueAfterTheSequenceStyles extends StyleSheet.Inline {
            import dsl._

            val gradient = style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }
          val wording: OperationWordingModel =
            self.props.wrapped.operation.getWordingByLanguageOrError(self.props.wrapped.language)

          <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := js.Array(TableLayoutStyles.cell))(
                <.div(
                  ^.className := js.Array(LayoutRulesStyles.row, PromptingToContinueAfterTheSequenceStyles.introWrapper)
                )(
                  <.p(
                    ^.className := js
                      .Array(PromptingToContinueAfterTheSequenceStyles.intro, TextStyles.bigText, TextStyles.boldText)
                  )(unescape(I18n.t("sequence.prompting-to-continue.intro")))
                )
              )
            ),
            <.div(^.className := TableLayoutStyles.fullHeightRow)(
              <.div(^.className := js.Array(TableLayoutStyles.cell, LayoutRulesStyles.rowWithCols))(
                <.div(
                  ^.className := js.Array(
                    PromptingToContinueAfterTheSequenceStyles.contentWrapper,
                    TableLayoutBeyondMediumStyles.fullHeightWrapper
                  )
                )(
                  <.div(
                    ^.className := js.Array(
                      TableLayoutBeyondMediumStyles.cell,
                      ColRulesStyles.col,
                      ColRulesStyles.colTwoThirdsBeyondMedium
                    )
                  )(
                    <.div(
                      ^.className := js.Array(
                        PromptingToContinueAfterTheSequenceStyles.nextSequenceAccessWrapper,
                        TableLayoutBeyondMediumStyles.fullHeightWrapper,
                        DynamicPromptingToContinueAfterTheSequenceStyles.gradient
                      )
                    )(
                      <.div(
                        ^.className := js
                          .Array(TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle, LayoutRulesStyles.row)
                      )(
                        <.p(
                          ^.className := js.Array(
                            PromptingToContinueAfterTheSequenceStyles.nextSequenceAccessIntro,
                            TextStyles.mediumText
                          )
                        )(unescape(I18n.t("sequence.prompting-to-continue.continue.intro"))),
                        <.div(^.className := PromptingToContinueAfterTheSequenceStyles.nextSequenceAccessTitleWrapper)(
                          <.p(
                            ^.className := js.Array(
                              PromptingToContinueAfterTheSequenceStyles.nextSequenceAccessTitle,
                              TextStyles.biggerMediumText,
                              TextStyles.boldText
                            )
                          )(unescape(wording.question))
                        ),
                        <.div(^.className := PromptingToContinueAfterTheSequenceStyles.ctaWrapper)(
                          <.button(
                            ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton),
                            ^.onClick := self.props.wrapped.clickOnButtonHandler
                          )(
                            <.i(^.className := js.Array(FontAwesomeStyles.play))(),
                            unescape("&nbsp;" + I18n.t("sequence.prompting-to-continue.continue.cta"))
                          )
                        )
                      )
                    )
                  ),
                  <.div(
                    ^.className := js.Array(
                      TableLayoutBeyondMediumStyles.cell,
                      ColRulesStyles.col,
                      ColRulesStyles.colThirdBeyondMedium
                    )
                  )(
                    <.div(
                      ^.className := js.Array(
                        TableLayoutBeyondMediumStyles.fullHeightWrapper,
                        PromptingToContinueAfterTheSequenceStyles.learnMoreAccessWrapper
                      )
                    )(
                      <.div(
                        ^.className := js
                          .Array(TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle, LayoutRulesStyles.row)
                      )(
                        <.p(
                          ^.className := js.Array(
                            PromptingToContinueAfterTheSequenceStyles.learnMoreAccessIntro,
                            TextStyles.mediumText
                          )
                        )(unescape(I18n.t("sequence.prompting-to-continue.learn-more.intro"))),
                        <.p(^.className := PromptingToContinueAfterTheSequenceStyles.learnMoreAccessLogoWrapper)(
                          <.img(^.src := self.props.wrapped.operation.logoUrl, ^.alt := wording.title)()
                        ),
                        <.p(^.className := PromptingToContinueAfterTheSequenceStyles.ctaWrapper)(
                          <.Link(
                            ^.to := s"${self.props.wrapped.operation.country}/consultation/${self.props.wrapped.operation.slug}",
                            ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA)
                          )(unescape(I18n.t("sequence.prompting-to-continue.learn-more.cta")))
                        )
                      )
                    )
                  )
                )
              )
            ),
            if (self.props.wrapped.operation.featureSettings.share) {
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := js.Array(TableLayoutStyles.cell))(
                  <.div(
                    ^.className := js
                      .Array(LayoutRulesStyles.row, PromptingToContinueAfterTheSequenceStyles.shareWrapper)
                  )(
                    <.ShareComponent(
                      ^.wrapped := ShareProps(
                        operation = self.props.wrapped.operation,
                        intro = Some(unescape(I18n.t("sequence.prompting-to-continue.share.intro")))
                      )
                    )()
                  )
                )
              )
            },
            <.style()(
              PromptingToContinueAfterTheSequenceStyles.render[String],
              DynamicPromptingToContinueAfterTheSequenceStyles.render[String]
            )
          )
        }
      )
}

object PromptingToContinueAfterTheSequenceStyles extends StyleSheet.Inline {
  import dsl._

  val intro: StyleA =
    style(textAlign.center)

  val introWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val shareWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()), textAlign.center)

  val contentWrapper: StyleA = style(maxWidth(1030.pxToEm()), margin(`0`, auto))

  val nextSequenceAccessWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`)),
      ThemeStyles.MediaQueries.beyondMedium(textAlign.center),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val nextSequenceAccessIntro: StyleA =
    style(color(ThemeStyles.TextColor.white))

  val nextSequenceAccessTitle: StyleA =
    style(color(ThemeStyles.TextColor.white))

  val nextSequenceAccessTitleWrapper: StyleA = style(margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`))

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val learnMoreAccessWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`)),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val learnMoreAccessIntro: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val learnMoreAccessLogoWrapper: StyleA =
    style(margin(ThemeStyles.SpacingValue.smaller.pxToEm(), `0`), maxWidth(240.pxToEm()))
}
