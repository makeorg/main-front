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

package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationWording, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{RWDRulesMediumStyles, TableLayoutBeyondMediumStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._

import scala.scalajs.js

object ProposalSOperationInfos {

  final case class ProposalSOperationInfosProps(operation: OperationModel, language: String, country: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ProposalSOperationInfosProps, Unit](
        displayName = "ProposalInRelationToOperationFooter",
        render = (self) => {
          object DynamicProposalSOperationInfosStyles extends StyleSheet.Inline {

            import dsl._

            val operationName =
              style(color :=! self.props.wrapped.operation.color)
          }
          val wording: OperationWording =
            self.props.wrapped.operation.getWordingByLanguageOrError(self.props.wrapped.language)

          <.div(^.className := ProposalSOperationInfosStyles.wrapper)(
            <.div(^.className := TableLayoutBeyondMediumStyles.wrapper)(
              <.p(
                ^.className := js.Array(
                  ProposalSOperationInfosStyles.infosWrapper,
                  TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle
                )
              )(
                <.span(^.className := js.Array(ProposalSOperationInfosStyles.intro, TextStyles.smallText))(
                  unescape(I18n.t("proposal.proposal-s-operation-infos.intro"))
                ),
                <.span(^.className := RWDRulesMediumStyles.hideBeyondMedium)(" "),
                <.span(
                  ^.className := js.Array(
                    TextStyles.verySmallTitle,
                    ProposalSOperationInfosStyles.operationName,
                    DynamicProposalSOperationInfosStyles.operationName
                  )
                )(unescape(wording.title))
              ),
              <.div(
                ^.className := js.Array(
                  ProposalSOperationInfosStyles.CTAsWrapper,
                  TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle
                )
              )(
                <.p(^.className := ProposalSOperationInfosStyles.CTA)(
                  if (!self.props.wrapped.operation.landingSequenceId.value.isEmpty) {
                    <.Link(
                      ^.to := s"/${self.props.wrapped.country}/consultation/${self.props.wrapped.operation.slug}/selection",
                      ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA)
                    )(unescape(I18n.t("proposal.proposal-s-operation-infos.participate")))
                  } else {
                    <.Link(
                      ^.to := s"/${self.props.wrapped.country}/consultation/${self.props.wrapped.operation.slug}",
                      ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA)
                    )(unescape(I18n.t("proposal.proposal-s-operation-infos.participate")))
                  }
                ),
                wording.learnMoreUrl.map { url =>
                  <.p(^.className := ProposalSOperationInfosStyles.CTA)(
                    <.a(
                      ^.href := url,
                      ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA),
                      ^.target := "_blank"
                    )(unescape(I18n.t("proposal.proposal-s-operation-infos.see-more")))
                  )
                }
              )
            ),
            <.style()(ProposalSOperationInfosStyles.render[String], DynamicProposalSOperationInfosStyles.render[String])
          )
        }
      )
}

object ProposalSOperationInfosStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      borderTop(1.px, solid, ThemeStyles.BorderColor.veryLight),
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm())
    )

  val infosWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        borderRight(1.px, solid, ThemeStyles.BorderColor.veryLight),
        textAlign.right
      )
    )

  val intro: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val operationName: StyleA =
    style(
      ThemeStyles.MediaQueries
        .beyondMedium(display.block, marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()))
    )

  val CTAsWrapper: StyleA =
    style(
      marginTop((ThemeStyles.SpacingValue.small - (ThemeStyles.SpacingValue.smaller / 2)).pxToEm()),
      marginBottom((-1 * (ThemeStyles.SpacingValue.smaller / 2)).pxToEm()),
      ThemeStyles.MediaQueries
        .beyondMedium(margin(`0`), paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm()), whiteSpace.nowrap)
    )

  val CTA: StyleA =
    style(
      display.inlineBlock,
      margin(
        (ThemeStyles.SpacingValue.smaller / 2).pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        (ThemeStyles.SpacingValue.smaller / 2).pxToEm(),
        `0`
      )
    )

}
