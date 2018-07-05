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

package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.QualificateVoteButton.QualificateVoteButtonProps
import org.make.front.models.{OperationExpanded, Proposal, Qualification}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.TooltipStyles
import org.make.front.styles.utils._

import scala.concurrent.Future
import org.make.front.Main.CssSettings._
import org.make.front.components.share.ShareLikeItProposal.ShareLikeItProposalProps

import scala.scalajs.js

object QualificateVote {

  final case class QualificateVoteProps(proposal: Proposal,
                                        maybeOperation: Option[OperationExpanded],
                                        updateState: Boolean,
                                        voteKey: String,
                                        qualifications: js.Array[Qualification],
                                        qualify: String             => Future[Qualification],
                                        removeQualification: String => Future[Qualification],
                                        guide: Option[String] = None,
                                        isProposalSharable : Boolean)

  final case class QualificateVoteState(qualifications: Map[String, Qualification], activateTooltip: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[QualificateVoteProps, QualificateVoteState](
        displayName = "QualificateVote",
        getInitialState = { self =>
          QualificateVoteState(
            qualifications = self.props.wrapped.qualifications.map { qualification => qualification.key -> qualification }.toMap,
            activateTooltip = false
          )
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(QualificateVoteState(
            qualifications = props.wrapped.qualifications.map { qualification => qualification.key -> qualification }.toMap,
            activateTooltip = props.wrapped.qualifications.map { qualification => qualification.activateTooltip }.head)
          )
        },
        render = { self =>

          def tooltipOn() = () => {
            self.setState(_.copy(activateTooltip = true))
          }

          def tooltipOff() = () => {
            self.setState(_.copy(activateTooltip = false))
          }

          <.div(^.className := QualificateVoteStyles.wrapper, ^.onMouseOver := tooltipOn, ^.onMouseOut := tooltipOff)(
            <.ul()(
              self.props.wrapped.qualifications.map {
            qualification =>
              <.li(^.className := QualificateVoteStyles.buttonItem)(
                <.QualificateVoteButtonComponent(
                  ^.wrapped := QualificateVoteButtonProps(
                    updateState = self.props.wrapped.updateState,
                    voteKey = self.props.wrapped.voteKey,
                    qualification = qualification,
                    qualifyVote = self.props.wrapped.qualify,
                    removeVoteQualification = self.props.wrapped.removeQualification
                  )
                )(),
                <.style()(QualificateVoteStyles.render[String])
              )
          }.toSeq), if (self.props.wrapped.guide.getOrElse("") != "") {
            <.p(^.className := QualificateVoteStyles.guide)(
              <.span(
                ^.className := TextStyles.smallerText,
                ^.dangerouslySetInnerHTML := self.props.wrapped.guide.getOrElse("")
              )()
            )
          },
            <.div (^.className := QualificateVoteStyles.tooltipTrigged(self.state.activateTooltip))(
              self.props.wrapped.qualifications.map {
                qualification =>
                if(qualification.key == "likeIt" && qualification.hasQualified){
                  self.props.wrapped.maybeOperation.map {
                    operation =>
                      <.div (^.className := QualificateVoteStyles.isProposalSharable(self.props.wrapped.isProposalSharable))(
                        <.ShareLikeItProposalComponent(
                          ^.wrapped := ShareLikeItProposalProps(
                            proposal = self.props.wrapped.proposal,
                            operation = operation,
                            country = operation.country
                          )
                        )()
                      )
                  }
                }
              }.toSeq
            )
          )
        }
      )
}

object QualificateVoteStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative)

  val buttonItem: StyleA =
    style(marginTop((ThemeStyles.SpacingValue.smaller / 2).pxToEm()), &.firstChild(marginTop(`0`)))

  val guide: StyleA =
    style(
      TooltipStyles.topPositioned,
      ThemeStyles.MediaQueries.beyondMedium(
        top(50.%%),
        transform := "translateY(-50%)",
        left(100.%%),
        marginBottom(`0`),
        marginLeft(ThemeStyles.SpacingValue.smaller.pxToEm()),
        bottom.inherit,
        right.auto,
        &.after(
          top(50.%%),
          transform := "translateY(-50%)",
          right(100.%%),
          bottom.auto,
          borderTop(5.pxToEm(), solid, transparent),
          borderBottom(5.pxToEm(), solid, transparent),
          borderRight(5.pxToEm(), solid, ThemeStyles.BackgroundColor.darkGrey)
        )
      )
    )

  val tooltipTrigged: (Boolean) => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(
          opacity(1),
          visibility.visible,
          transition := s"opacity .25s ease-in"
        )
      } else {
        styleS(
          opacity(0),
          visibility.hidden,
          transition := s"visibility 0s linear 1.25s, opacity .25s ease-in 1s"
        )
      }
  )

  val isProposalSharable : (Boolean) => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(
          TooltipStyles.bottomPositioned,
          width(275.pxToEm()),
          height(75.pxToEm()),
          padding(ThemeStyles.SpacingValue.smaller.pxToEm()),
          ThemeStyles.MediaQueries.beyondMedium(
            TooltipStyles.rightPositioned,
            width(220.pxToEm()),
            height(100.pxToEm())
          )
        )
      } else {
        styleS(
          display.none
        )
      }
  )


}
