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
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.{
  QualificateVoteButtonStyles,
  QualificateVoteStyles,
  VoteButtonStyles,
  VoteStyles
}
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.utils._

import scala.scalajs.js

object FakeProposalTile {

  val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[Unit, Unit](
          displayName = "ProposalTileWithTags",
          render = (self) => {
            <.article(^.className := js.Array(ProposalTileStyles.wrapper, FakeProposalTileStyles.wrapper))(
              <.div(^.className := js.Array(TableLayoutStyles.fullHeightWrapper, ProposalTileStyles.innerWrapper))(
                <.div(^.className := TableLayoutStyles.row)(
                  <.div(^.className := TableLayoutStyles.cell)(
                    <.div(
                      ^.className := js.Array(ProposalTileStyles.contentWrapper, FakeProposalTileStyles.contentWrapper)
                    )(
                      <.h3(
                        ^.className := js
                          .Array(TextStyles.mediumText, TextStyles.boldText, FakeProposalTileStyles.title)
                      )(unescape(I18n.t("user-profile.likeItproposal.fake-proposal"))),
                      <.ul(^.className := VoteStyles.voteButtonsList)(
                        <.li(^.className := VoteStyles.voteButtonItem(false))(
                          <.div(^.className := VoteButtonStyles.wrapper(true))(
                            <.div(^.className := VoteButtonStyles.buttonAndResultsOfCurrentVoteWrapper)(
                              <.button(
                                ^.className := js.Array(
                                  VoteButtonStyles.button,
                                  VoteButtonStyles.agree,
                                  VoteButtonStyles.agreeActivated
                                )
                              )()
                            ),
                            <.div(^.className := VoteButtonStyles.qualificateVoteAndResultsOfVoteWrapper)(
                              <.div(^.className := VoteButtonStyles.qualificateVoteAndResultsOfVoteInnerWrapper(true))(
                                <.div(^.className := QualificateVoteStyles.wrapper)(
                                  <.ul()(
                                    <.li(^.className := QualificateVoteStyles.buttonItem)(
                                      <.button(
                                        ^.className := js
                                          .Array(
                                            QualificateVoteButtonStyles.button,
                                            QualificateVoteButtonStyles.agree,
                                            QualificateVoteButtonStyles.agreeActivated
                                          )
                                      )(
                                        <.span(^.className := TableLayoutStyles.fullHeightWrapper)(
                                          <.span(
                                            ^.className := js.Array(
                                              TextStyles.smallerText,
                                              TextStyles.boldText,
                                              TableLayoutStyles.cellVerticalAlignMiddle,
                                              QualificateVoteButtonStyles.label,
                                            ),
                                            ^.dangerouslySetInnerHTML := I18n
                                              .t("proposal.vote.agree.qualifications.likeIt")
                                          )(),
                                          <.span(
                                            ^.className := js.Array(
                                              TableLayoutStyles.cellVerticalAlignMiddle,
                                              QualificateVoteButtonStyles.votesCounter,
                                              TextStyles.mediumText,
                                              TextStyles.boldText
                                            )
                                          )(
                                            <.span(
                                              ^.className := QualificateVoteButtonStyles.selectedQualificationVotesCounter
                                            )("25")
                                          )
                                        )
                                      )
                                    ),
                                    <.li(^.className := QualificateVoteStyles.buttonItem)(
                                      <.button(
                                        ^.className := js
                                          .Array(QualificateVoteButtonStyles.button, QualificateVoteButtonStyles.agree)
                                      )(
                                        <.span(^.className := TableLayoutStyles.fullHeightWrapper)(
                                          <.span(
                                            ^.className := js.Array(
                                              TextStyles.smallerText,
                                              TextStyles.boldText,
                                              TableLayoutStyles.cellVerticalAlignMiddle,
                                              QualificateVoteButtonStyles.label
                                            )
                                          )(unescape(I18n.t("proposal.vote.agree.qualifications.doable"))),
                                          <.span(
                                            ^.className := js.Array(
                                              TableLayoutStyles.cellVerticalAlignMiddle,
                                              QualificateVoteButtonStyles.votesCounter,
                                              TextStyles.mediumText,
                                              TextStyles.boldText
                                            )
                                          )("0")
                                        )
                                      )
                                    ),
                                    <.li(^.className := QualificateVoteStyles.buttonItem)(
                                      <.button(
                                        ^.className := js
                                          .Array(QualificateVoteButtonStyles.button, QualificateVoteButtonStyles.agree)
                                      )(
                                        <.span(^.className := TableLayoutStyles.fullHeightWrapper)(
                                          <.span(
                                            ^.className := js.Array(
                                              TextStyles.smallerText,
                                              TextStyles.boldText,
                                              TableLayoutStyles.cellVerticalAlignMiddle,
                                              QualificateVoteButtonStyles.label
                                            )
                                          )(unescape(I18n.t("proposal.vote.agree.qualifications.platitudeAgree"))),
                                          <.span(
                                            ^.className := js.Array(
                                              TableLayoutStyles.cellVerticalAlignMiddle,
                                              QualificateVoteButtonStyles.votesCounter,
                                              TextStyles.mediumText,
                                              TextStyles.boldText
                                            )
                                          )("0")
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
                    )
                  ),
                )
              ),
              <.style()(
                ProposalTileStyles.render[String],
                VoteButtonStyles.render[String],
                VoteStyles.render[String],
                QualificateVoteStyles.render[String],
                QualificateVoteButtonStyles.render[String],
                FakeProposalTileStyles.render[String]
              )
            )
          }
        )
    )
}

object FakeProposalTileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(marginTop(25.pxToEm()))

  val contentWrapper: StyleA =
    style(padding(25.pxToEm()))

  val title: StyleA =
    style(textAlign.center)

}
