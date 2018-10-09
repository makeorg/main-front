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
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalAuthorInfos.ProposalAuthorInfosProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  Qualification     => QualificationModel,
  TranslatedTheme   => TranslatedThemeModel,
  Vote              => VoteModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingLocation

import scala.scalajs.js

object ProposalInsideSequence {

  final case class ProposalInsideSequenceProps(proposal: ProposalModel,
                                               handleSuccessfulVote: (VoteModel)                           => Unit = (_) => {},
                                               handleSuccessfulQualification: (String, QualificationModel) => Unit =
                                                 (_, _) => {},
                                               handleClickOnCta: () => Unit,
                                               hasBeenVoted: Boolean,
                                               guideToVote: Option[String] = None,
                                               guideToQualification: Option[String] = None,
                                               index: Int,
                                               maybeTheme: Option[TranslatedThemeModel],
                                               maybeOperation: Option[OperationModel],
                                               sequenceId: SequenceId,
                                               maybeLocation: Option[LocationModel])

  final case class ProposalInsideSequenceState(hasBeenVoted: Boolean, isProposalSharable: Boolean)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[ProposalInsideSequenceProps, ProposalInsideSequenceState](
        displayName = "ProposalInsideSequence",
        getInitialState = { self =>
          ProposalInsideSequenceState(hasBeenVoted = false, isProposalSharable = false)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(
            ProposalInsideSequenceState(hasBeenVoted = props.wrapped.hasBeenVoted, isProposalSharable = false)
          )
        },
        render = { self =>
          <.div(^.className := js.Array(LayoutRulesStyles.row))(
            <.div(^.className := ProposalInsideSequenceStyles.infosWrapper)(
              <.p(^.className := js.Array(TextStyles.mediumText, ProposalInsideSequenceStyles.infos))(
                <.ProposalAuthorInfos(
                  ^.wrapped := ProposalAuthorInfosProps(
                    proposal = self.props.wrapped.proposal,
                    country = None,
                    withCreationDate = false
                  )
                )()
              )
            ),
            <.div(^.className := ProposalInsideSequenceStyles.contentWrapper)(
              <.h3(^.className := js.Array(TextStyles.bigText, TextStyles.boldText))(
                self.props.wrapped.proposal.content
              ),
              <.div(^.className := ProposalInsideSequenceStyles.voteWrapper)(
                <.VoteContainerComponent(
                  ^.wrapped := VoteContainerProps(
                    proposal = self.props.wrapped.proposal,
                    onSuccessfulVote = self.props.wrapped.handleSuccessfulVote,
                    onSuccessfulQualification = self.props.wrapped.handleSuccessfulQualification,
                    guideToVote = self.props.wrapped.guideToVote,
                    guideToQualification = self.props.wrapped.guideToQualification,
                    index = self.props.wrapped.index,
                    trackingLocation = TrackingLocation.sequencePage,
                    maybeTheme = self.props.wrapped.maybeTheme,
                    maybeOperation = self.props.wrapped.maybeOperation,
                    maybeSequenceId = Some(self.props.wrapped.sequenceId),
                    maybeLocation = self.props.wrapped.maybeLocation,
                    isProposalSharable = false
                  )
                )(),
                <.div(^.className := ProposalInsideSequenceStyles.ctaWrapper)(
                  <.button(
                    ^.className := js.Array(
                      CTAStyles.basic,
                      CTAStyles.basicOnButton,
                      ProposalInsideSequenceStyles.ctaVisibility(self.props.wrapped.hasBeenVoted)
                    ),
                    ^.disabled := !self.props.wrapped.hasBeenVoted,
                    ^.onClick := self.props.wrapped.handleClickOnCta
                  )(
                    unescape(I18n.t("sequence.proposal.next-cta") + "&nbsp;"),
                    <.i(^.className := FontAwesomeStyles.angleRight)()
                  )
                )
              )
            ),
            <.style()(ProposalInsideSequenceStyles.render[String])
          )
        }
      )
    )
}

object ProposalInsideSequenceStyles extends StyleSheet.Inline {
  import dsl._

  val infosWrapper: StyleA =
    style(
      textAlign.center,
      position.relative,
      paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      &.after(
        content := "''",
        position.absolute,
        top(100.%%),
        left(50.%%),
        transform := s"translateX(-50%)",
        marginTop(-0.5.px),
        height(1.px),
        width(90.pxToEm()),
        backgroundColor(ThemeStyles.BorderColor.lighter)
      )
    )

  val infos: StyleA =
    style(color(ThemeStyles.TextColor.light))

  val contentWrapper: StyleA =
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val voteWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val ctaWrapper: StyleA =
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val ctaVisibility: (Boolean) => StyleA = styleF.bool(
    visible =>
      if (visible) {
        styleS(visibility.visible)
      } else {
        styleS(visibility.hidden)
    }
  )
}
