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
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.QualificateVote.QualificateVoteProps
import org.make.front.components.proposal.vote.ResultsOfVote.ResultsOfVoteProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.NumberFormat._
import org.make.front.models.{OperationExpanded, Proposal, ProposalId, Qualification, Vote => VoteModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.TooltipStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object VoteButton {

  case class VoteButtonProps(proposal: Proposal,
                             maybeOperation: Option[OperationExpanded],
                             index: Int,
                             updateState: Boolean,
                             proposalId: ProposalId,
                             votes: Map[String, Int],
                             vote: VoteModel,
                             handleVote: (String)                      => Future[VoteModel],
                             handleUnvote: (String)                    => Future[VoteModel],
                             qualifyVote: (String, String)             => Future[Qualification],
                             removeVoteQualification: (String, String) => Future[Qualification],
                             guideToVote: Option[String] = None,
                             guideToQualification: Option[String] = None,
                             isProposalSharable: Boolean)

  case class VoteButtonState(isActivated: Boolean,
                             isHovered: Boolean,
                             resultsOfVoteAreDisplayed: Boolean,
                             votes: Map[String, Int],
                             qualifications: js.Array[Qualification])

  lazy val reactClass: ReactClass =
    React.createClass[VoteButtonProps, VoteButtonState](displayName = "VoteButton", getInitialState = { self =>
      VoteButtonState(
        isActivated = self.props.wrapped.vote.hasVoted,
        isHovered = false,
        resultsOfVoteAreDisplayed = false,
        votes = self.props.wrapped.votes,
        qualifications = self.props.wrapped.vote.qualifications
      )
    }, componentWillReceiveProps = { (self, props) =>
      self.setState(
        VoteButtonState(
          isActivated = props.wrapped.vote.hasVoted,
          isHovered = false,
          resultsOfVoteAreDisplayed = false,
          votes = props.wrapped.votes,
          qualifications = props.wrapped.vote.qualifications
        )
      )
    }, render = { (self) =>
      def voteOrUnvote() = (e: SyntheticEvent) => {
        e.preventDefault()
        if (self.state.isActivated) {
          self.props.wrapped.handleUnvote(self.props.wrapped.vote.key)
        } else {
          self.props.wrapped.handleVote(self.props.wrapped.vote.key)
        }
      }

      def handleMouseOver() = (e: SyntheticEvent) => {
        e.preventDefault()
        self.setState(_.copy(isHovered = true))
      }

      def handleMouseOut() = (e: SyntheticEvent) => {
        e.preventDefault()
        self.setState(_.copy(isHovered = false))
      }

      def qualify(key: String): Future[Qualification] = {
        val future = self.props.wrapped.qualifyVote(self.props.wrapped.vote.key, key)
        if (self.props.wrapped.updateState) {
          future.onComplete {
            case Failure(_) =>
            case Success(qualifications) =>
              self.setState(
                state =>
                  state.copy(qualifications = state.qualifications.map { qualification =>
                    if (qualifications.key == qualification.key) {
                      Qualification(
                        key = qualifications.key,
                        count = qualifications.count,
                        hasQualified = qualifications.hasQualified,
                        activateTooltip = true
                      )
                    } else {
                      qualification
                    }
                  })
              )
          }
        }

        future

      }
      def removeQualification(key: String): Future[Qualification] = {
        val future = self.props.wrapped.removeVoteQualification(self.props.wrapped.vote.key, key)

        if (self.props.wrapped.updateState) {
          future.onComplete {
            case Failure(_) =>
            case Success(qualifications) =>
              self.setState(
                state =>
                  state.copy(qualifications = state.qualifications.map { qualification =>
                    if (qualifications.key == qualification.key) {
                      Qualification(
                        key = qualifications.key,
                        count = qualifications.count,
                        hasQualified = qualifications.hasQualified,
                        activateTooltip = false
                      )
                    } else {
                      qualification
                    }
                  })
              )
          }
        }

        future
      }

      def toggleResultsOfVote() = (e: SyntheticEvent) => {
        e.preventDefault()
        self.setState(_.copy(resultsOfVoteAreDisplayed = !self.state.resultsOfVoteAreDisplayed))
      }

      val buttonClasses =
        js.Array(VoteButtonStyles.button.htmlClass, self.props.wrapped.vote.key match {
            case "agree" =>
              VoteButtonStyles.agree.htmlClass + " " + (if (self.state.isActivated)
                                                          VoteButtonStyles.agreeActivated.htmlClass
                                                        else "")
            case "disagree" =>
              VoteButtonStyles.disagree.htmlClass + " " + (if (self.state.isActivated)
                                                             VoteButtonStyles.disagreeActivated.htmlClass
                                                           else "")
            case "neutral" =>
              VoteButtonStyles.neutral.htmlClass + " " + (if (self.state.isActivated)
                                                            VoteButtonStyles.neutralActivated.htmlClass
                                                          else "")
            case _ =>
              VoteButtonStyles.neutral.htmlClass + " " + (if (self.state.isActivated)
                                                            VoteButtonStyles.neutralActivated.htmlClass
                                                          else "")
          })
          .mkString(" ")

      val totalOfVotesClasses =
        js.Array(VoteButtonStyles.totalOfVotes.htmlClass, self.props.wrapped.vote.key match {
            case "agree" =>
              VoteButtonStyles.totalOfAgreeVotes.htmlClass
            case "disagree" =>
              VoteButtonStyles.totalOfDisagreeVotes.htmlClass
            case "neutral" =>
              VoteButtonStyles.totalOfNeutralVotes.htmlClass
            case _ =>
              VoteButtonStyles.totalOfNeutralVotes.htmlClass
          })
          .mkString(" ")

      <.div(^.className := VoteButtonStyles.wrapper(self.state.isActivated))(
        <.div(^.className := VoteButtonStyles.buttonAndResultsOfCurrentVoteWrapper)(
          <.button(
            ^.className := buttonClasses,
            ^.onClick := voteOrUnvote(),
            ^.onMouseOver := handleMouseOver,
            ^.onMouseOut := handleMouseOut
          )(
            <.span(
              ^.className := js.Array(
                VoteButtonStyles.label,
                VoteButtonStyles
                  .labelDisplay(self.props.wrapped.guideToVote.getOrElse("") == "" && !self.state.isActivated),
                VoteButtonStyles.labelVisibility(
                  self.props.wrapped.guideToVote.getOrElse("") == "" && !self.state.isActivated && self.state.isHovered
                )
              )
            )(
              <.span(^.className := TextStyles.smallerText)(
                unescape(I18n.t(s"proposal.vote.${self.props.wrapped.vote.key}.label"))
              )
            ),
            if (self.props.wrapped.guideToVote.getOrElse("") != "") {
              <.span(
                ^.className := js.Array(VoteButtonStyles.label, VoteButtonStyles.labelDisplay(!self.state.isActivated))
              )(
                <.span(
                  ^.className := TextStyles.smallerText,
                  ^.dangerouslySetInnerHTML := self.props.wrapped.guideToVote.getOrElse("")
                )()
              )
            }
          ),
          if (self.state.isActivated) {
            val totalOfVotes: Int = self.state.votes.values.sum
            val currentVotes: Int = self.state.votes.getOrElse(self.props.wrapped.vote.key, 0)

            js.Array(
                <.p(^.className := totalOfVotesClasses)(formatToKilo(currentVotes)),
                <.p(^.className := js.Array(VoteButtonStyles.partOfVotes))(
                  unescape(
                    I18n.t(
                      "proposal.vote.partOfVotes",
                      Replacements(("value", formatToPercent(currentVotes, totalOfVotes).toString))
                    )
                  )
                ),
                <.button(
                  ^.className := js.Array(VoteButtonStyles.resultsOfVoteAccessButton, FontAwesomeStyles.barChart),
                  ^.onClick := toggleResultsOfVote()
                )()
              )
              .toSeq
          }
        ),
        <.div(^.className := VoteButtonStyles.qualificateVoteAndResultsOfVoteWrapper)(
          <.div(^.className := VoteButtonStyles.qualificateVoteAndResultsOfVoteInnerWrapper(self.state.isActivated))(
            if (self.state.isActivated) {
              if (self.state.resultsOfVoteAreDisplayed) {
                <.div(^.className := VoteButtonStyles.resultsOfVoteWrapper)(
                  <.ResultsOfVoteComponent(
                    ^.wrapped := ResultsOfVoteProps(votes = self.state.votes, index = self.props.wrapped.index)
                  )()
                )
              } else {
                <.QualificateVoteComponent(
                  ^.wrapped := QualificateVoteProps(
                    proposal = self.props.wrapped.proposal,
                    maybeOperation = self.props.wrapped.maybeOperation,
                    updateState = self.props.wrapped.updateState,
                    qualifications = self.state.qualifications,
                    voteKey = self.props.wrapped.vote.key,
                    qualify = qualify,
                    removeQualification = removeQualification,
                    guide = self.props.wrapped.guideToQualification,
                    isProposalSharable = self.props.wrapped.isProposalSharable
                  )
                )()
              }
            }
          )
        ),
        <.style()(VoteButtonStyles.render[String])
      )
    })
}

object VoteButtonStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: (Boolean) => StyleA = styleF.bool(
    voted =>
      if (voted) {
        styleS(position.relative, display.table, height(100.%%), width(100.%%))
      } else {
        styleS(position.relative, display.table, height(100.%%))
    }
  )

  val buttonAndResultsOfCurrentVoteWrapper: StyleA =
    style(display.tableCell, height(100.%%), verticalAlign.top, width(50.pxToEm()))

  val qualificateVoteAndResultsOfVoteWrapper: StyleA = style(display.tableCell, height(100.%%))

  val qualificateVoteAndResultsOfVoteInnerWrapper: (Boolean) => StyleA = styleF.bool(
    voted =>
      if (voted) {
        styleS(
          position.relative,
          display.table,
          height(100.%%),
          width(100.%%),
          paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm())
        )
      } else {
        styleS(width(0.pxToEm()))
    }
  )

  val resultsOfVoteWrapper: StyleA =
    style(
      position.relative,
      display.tableCell,
      verticalAlign.middle,
      width(100.%%),
      height(100.%%),
      padding(ThemeStyles.SpacingValue.smaller.pxToEm(), ThemeStyles.SpacingValue.small.pxToEm()),
      borderRadius(
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        `0`
      ),
      backgroundColor(rgb(74, 74, 74)),
      &.after(
        content := "''",
        position.absolute,
        right(100.%%),
        bottom(0.%%),
        borderRight(5.pxToEm(), solid, rgb(74, 74, 74)),
        borderBottom(5.pxToEm(), solid, rgb(74, 74, 74)),
        borderLeft(5.pxToEm(), solid, transparent),
        borderTop(5.pxToEm(), solid, transparent)
      )
    )

  val button: StyleA = style(
    display.block,
    position.relative,
    width(50.pxToEm()),
    height(50.pxToEm()),
    boxSizing.borderBox,
    border(1.px, solid, ThemeStyles.TextColor.base),
    borderRadius(50.%%),
    textAlign.center,
    backgroundColor.transparent,
    boxShadow := "0 0 0 0 rgba(0, 0, 0, 0)",
    transition := "color .2s ease-in-out, background-color .2s ease-in-out, box-shadow .2s ease-in-out",
    &.before(
      content := "'\\f087'",
      position.absolute,
      top(`0`),
      left(`0`),
      height(100.%%),
      width(100.%%),
      lineHeight(48.px),
      fontSize(24.px),
      ThemeStyles.Font.fontAwesome,
      textAlign.center
    ),
    &.hover(
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.TextColor.base),
      boxShadow := s"0 ${2.pxToEm().value} ${5.pxToEm().value} 0 rgba(0, 0, 0, .5)"
    )
  )

  val label: StyleA = style(
    TooltipStyles.bottomPositioned,
    paddingTop(3.pxToEm()),
    paddingBottom(3.pxToEm()),
    width.auto,
    whiteSpace.nowrap
  )

  val labelVisibility: (Boolean) => StyleA = styleF.bool(
    visible =>
      if (visible) {
        styleS(opacity(1), transition := "opacity .2s ease-in-out")
      } else {
        styleS(opacity(0), transition := "opacity .2s ease-in-out")
    }
  )

  val labelDisplay: (Boolean) => StyleA = styleF.bool(
    displayed =>
      if (displayed) {
        styleS()
      } else {
        styleS(display.none)
    }
  )

  val agree: StyleA = style(
    color(ThemeStyles.ThemeColor.positive),
    borderColor(ThemeStyles.ThemeColor.positive),
    &.hover(backgroundColor(ThemeStyles.ThemeColor.positive), color(ThemeStyles.TextColor.white)),
    &.before(content := s"'\\F087'")
  )

  val agreeActivated: StyleA = style(
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.ThemeColor.positive),
    boxShadow := "0 0 0 0 rgba(0, 0, 0, 0)"
  )

  val disagree: StyleA = style(
    color(ThemeStyles.ThemeColor.negative),
    borderColor(ThemeStyles.ThemeColor.negative),
    &.hover(backgroundColor(ThemeStyles.ThemeColor.negative), color(ThemeStyles.TextColor.white)),
    &.before(content := s"'\\F088'")
  )

  val disagreeActivated: StyleA = style(
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.ThemeColor.negative),
    boxShadow := "0 0 0 0 rgba(0, 0, 0, 0)"
  )

  val neutral: StyleA = style(
    color(ThemeStyles.TextColor.grey),
    borderColor(ThemeStyles.TextColor.grey),
    &.hover(backgroundColor(ThemeStyles.TextColor.grey), color(ThemeStyles.TextColor.white)),
    &.before(content := s"'\\F087'", transform := s"rotate(-90deg)")
  )

  val neutralActivated: StyleA = style(
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.TextColor.grey),
    boxShadow := "0 0 0 0 rgba(0, 0, 0, 0)"
  )

  val totalOfVotes: StyleA =
    style(
      marginTop(5.pxToEm(13)),
      ThemeStyles.Font.circularStdBold,
      fontSize(13.pxToEm()),
      lineHeight(1),
      textAlign.center
    )

  val totalOfAgreeVotes: StyleA =
    style(color(ThemeStyles.ThemeColor.positive))

  val totalOfDisagreeVotes: StyleA =
    style(color(ThemeStyles.ThemeColor.negative))

  val totalOfNeutralVotes: StyleA =
    style(color(ThemeStyles.TextColor.grey))

  val partOfVotes: StyleA =
    style(
      ThemeStyles.Font.circularStdBook,
      fontSize(11.pxToEm()),
      lineHeight(1),
      textAlign.center,
      color(ThemeStyles.TextColor.grey)
    )

  val resultsOfVoteAccessButton: StyleA =
    style(
      height(16.pxToEm(10)),
      width(50.pxToEm(10)),
      marginTop(5.pxToEm(10)),
      boxSizing.borderBox,
      borderRadius(8.pxToEm(10)),
      padding(`0`, 8.pxToEm(10)),
      fontSize(10.pxToEm()),
      textAlign.center,
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )
}
