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
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.share.ShareProposal.ShareProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  GradientColor     => GradientColorModel,
  OperationExpanded => OperationModel,
  OperationWording  => OperationWordingModel,
  SequenceId        => SequenceIdModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles, _}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object PromptingToGoBackToOperation {

  final case class PromptingToGoBackToOperationProps(operation: OperationModel,
                                                     sequenceId: SequenceIdModel,
                                                     clickOnButtonHandler: () => Unit,
                                                     language: String,
                                                     country: String)

  final case class PromptingToGoBackToOperationState()

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[PromptingToGoBackToOperationProps, PromptingToGoBackToOperationState](
          displayName = "PromptingToGoBackToOperation",
          getInitialState = { _ =>
            PromptingToGoBackToOperationState()
          },
          render = { self =>
            val gradientValues: GradientColorModel =
              self.props.wrapped.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

            object DynamicPromptingToGoBackToOperationStyles extends StyleSheet.Inline {

              import dsl._

              val gradient =
                style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
            }
            val wording: OperationWordingModel =
              self.props.wrapped.operation.getWordingByLanguageOrError(self.props.wrapped.language)

            val onClick: () => Unit = () => {
              TrackingService.track(
                eventName = "click-finale-card-learnmore",
                trackingContext =
                  TrackingContext(TrackingLocation.sequencePage, Some(self.props.wrapped.operation.slug)),
                parameters = Map.empty,
                internalOnlyParameters = Map("sequenceId" -> self.props.wrapped.sequenceId.value)
              )
              self.props.history
                .push(s"/${self.props.wrapped.country}/consultation/${self.props.wrapped.operation.slug}")
            }

            <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := TableLayoutStyles.cell)(
                  <.div(
                    ^.className := js.Array(LayoutRulesStyles.row, PromptingToGoBackToOperationStyles.introWrapper)
                  )(
                    <.p(
                      ^.className := js
                        .Array(PromptingToGoBackToOperationStyles.intro, TextStyles.bigText, TextStyles.boldText)
                    )(unescape(I18n.t("sequence.prompting-to-continue.intro")))
                  )
                )
              ),
              <.div(^.className := TableLayoutStyles.fullHeightRow)(
                <.div(^.className := js.Array(TableLayoutStyles.cell, LayoutRulesStyles.rowWithCols))(
                  <.div(
                    ^.className := js.Array(
                      PromptingToGoBackToOperationStyles.contentWrapper,
                      TableLayoutBeyondMediumStyles.fullHeightWrapper
                    )
                  )(
                    <.div(
                      ^.className := js.Array(
                        TableLayoutBeyondMediumStyles.cell,
                        ColRulesStyles.col,
                        ColRulesStyles.colHalfBeyondMedium
                      )
                    )(
                      <.div(
                        ^.className := js.Array(
                          TableLayoutBeyondMediumStyles.fullHeightWrapper,
                          PromptingToGoBackToOperationStyles.learnMoreAccessWrapper
                        )
                      )(
                        <.div(
                          ^.className := js
                            .Array(TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle, LayoutRulesStyles.row)
                        )(
                          <.div(^.className := PromptingToGoBackToOperationStyles.learnMoreAccessContent)(
                            <.p(
                              ^.className := js
                                .Array(PromptingToGoBackToOperationStyles.learnMoreAccessIntro, TextStyles.mediumText)
                            )(unescape(I18n.t("sequence.prompting-to-continue.learn-more.intro"))),
                            <.p(^.className := PromptingToGoBackToOperationStyles.learnMoreAccessLogoWrapper)(
                              <.img(^.src := self.props.wrapped.operation.logoUrl, ^.alt := wording.title)()
                            ),
                            <.p(
                              ^.className := js
                                .Array(PromptingToGoBackToOperationStyles.learnMoreAccessIntro, TextStyles.mediumText)
                            )(unescape(I18n.t("sequence.prompting-to-continue.learn-more.following-intro"))),
                            <.p(^.className := PromptingToGoBackToOperationStyles.ctaWrapper)(
                              <.button(
                                ^.onClick := onClick,
                                ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton)
                              )(unescape(I18n.t("sequence.prompting-to-continue.learn-more.cta")))
                            )
                          )
                        )
                      )
                    ),
                    if (self.props.wrapped.operation.featureSettings.share) {
                      <.div(
                        ^.className := js
                          .Array(
                            TableLayoutBeyondMediumStyles.cell,
                            ColRulesStyles.col,
                            ColRulesStyles.colHalfBeyondMedium
                          )
                      )(
                        <.div(
                          ^.className := js
                            .Array(
                              TableLayoutBeyondMediumStyles.fullHeightWrapper,
                              PromptingToGoBackToOperationStyles.sharingWrapper
                            )
                        )(
                          <.div(
                            ^.className := js
                              .Array(TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle, LayoutRulesStyles.row)
                          )(
                            <.div(^.className := PromptingToGoBackToOperationStyles.sharingIntroWrapper)(
                              <.p(
                                ^.className := js
                                  .Array(PromptingToGoBackToOperationStyles.sharingIntro, TextStyles.mediumText)
                              )(unescape(I18n.t("sequence.prompting-to-continue.share.intro")))
                            ),
                            <.div(^.className := PromptingToGoBackToOperationStyles.alignshare)(
                              <.ShareComponent(^.wrapped := ShareProps(operation = self.props.wrapped.operation))()
                            )
                          )
                        )
                      )
                    }
                  )
                ),
                <.style()(
                  PromptingToGoBackToOperationStyles.render[String],
                  DynamicPromptingToGoBackToOperationStyles.render[String]
                )
              )
            )
          }
        )
    )
}

object PromptingToGoBackToOperationStyles extends StyleSheet.Inline {

  import dsl._

  val intro: StyleA =
    style(textAlign.center)

  val introWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val contentWrapper: StyleA = style(maxWidth(1030.pxToEm()), margin(`0`, auto))

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val learnMoreAccessWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`)),
      textAlign.center,
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val learnMoreAccessContent: StyleA = style(display.inlineBlock)

  val learnMoreAccessIntro: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val learnMoreAccessLogoWrapper: StyleA =
    style(margin(ThemeStyles.SpacingValue.smaller.pxToEm(), auto), maxWidth(240.pxToEm()))

  val sharingWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries.belowMedium(marginTop(ThemeStyles.SpacingValue.medium.pxToEm())),
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      textAlign.center,
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val sharingIntroWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val sharingIntro: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val alignshare: StyleA =
    style(textAlign.center)
}
