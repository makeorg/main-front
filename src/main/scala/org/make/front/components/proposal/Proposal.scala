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
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalAuthorInfos.ProposalAuthorInfosProps
import org.make.front.components.proposal.ProposalContainer.ProposalAndThemeOrOperationModel
import org.make.front.components.proposal.ProposalSOperationInfos.ProposalSOperationInfosProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.components.share.ShareProposalPage.ShareProposalProps
import org.make.front.components.showcase.OperationShowcaseContainer.OperationShowcaseContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, RWDHideRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingLocation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object Proposal {

  final case class ProposalProps(futureProposal: Future[ProposalAndThemeOrOperationModel],
                                 language: String,
                                 country: String)

  final case class ProposalState(maybeProposal: Option[ProposalModel] = None,
                                 maybeTheme: Option[TranslatedThemeModel] = None,
                                 maybeOperation: Option[OperationModel] = None,
                                 maybeLocation: Option[LocationModel] = None,
                                 isProposalSharable: Boolean)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[ProposalProps, ProposalState](displayName = "Proposal", getInitialState = { _ =>
        ProposalState(isProposalSharable = false)
      }, componentWillReceiveProps = {
        (self, props) =>
          props.wrapped.futureProposal.onComplete {
            case Failure(_) => props.history.push("/404")
            case Success(proposal) if proposal.maybeProposal.isEmpty =>
              props.history.push("/404")
            case Success(proposal) =>
              self.setState(
                _.copy(
                  maybeProposal = proposal.maybeProposal,
                  maybeTheme = proposal.maybeTheme,
                  maybeOperation = proposal.maybeOperation,
                  maybeLocation = proposal.maybeProposal.map { proposal =>
                    LocationModel.ProposalPage(proposal.id)
                  }
                )
              )
          }
      }, render = {
        self =>
          <("proposal")()(
            <.div(
              ^.className := js
                .Array(TableLayoutStyles.fullHeightWrapper, ProposalStyles.wrapper(self.state.maybeProposal.isDefined))
            )(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := js.Array(TableLayoutStyles.cell, ProposalStyles.mainHeaderWrapper))(
                  <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
                  <.div(^.className := ProposalStyles.fixedMainHeaderWrapper)(
                    <.CookieAlertContainerComponent.empty,
                    <.MainHeaderContainer.empty
                  )
                )
              ),
              <.div(^.className := js.Array(TableLayoutStyles.row, ProposalStyles.fullHeight))(
                <.div(^.className := js.Array(TableLayoutStyles.cell, ProposalStyles.articleCell))(
                  if (self.state.maybeProposal.isDefined) {
                    <.div(^.className := js.Array(LayoutRulesStyles.centeredRow, ProposalStyles.fullHeight))(
                      <.article(^.className := js.Array(ProposalStyles.article))(
                        <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
                          <.div(
                            ^.className := js
                              .Array(TableLayoutStyles.cellVerticalAlignMiddle, ProposalStyles.articleInnerWrapper)
                          )(
                            <.div(^.className := LayoutRulesStyles.row)(
                              <.div(^.className := ProposalStyles.infosWrapper)(
                                <.p(^.className := js.Array(TextStyles.mediumText, ProposalStyles.infos))(
                                  self.state.maybeProposal.map { proposal =>
                                    <.ProposalAuthorInfos(
                                      ^.wrapped := ProposalAuthorInfosProps(
                                        proposal = proposal,
                                        country = Some(self.props.wrapped.country)
                                      )
                                    )()
                                  }
                                )
                              ),
                              <.div(^.className := ProposalStyles.contentWrapper)(
                                <.h1(^.className := js.Array(TextStyles.bigText, TextStyles.boldText))(
                                  self.state.maybeProposal.map(_.content)
                                ),
                                <.div(^.className := ProposalStyles.voteWrapper)(self.state.maybeProposal.map {
                                  proposal =>
                                    <.VoteContainerComponent(
                                      ^.wrapped := VoteContainerProps(
                                        proposal = proposal,
                                        index = 1,
                                        maybeTheme = self.state.maybeTheme,
                                        maybeOperation = self.state.maybeOperation,
                                        maybeSequenceId = None,
                                        maybeLocation = self.state.maybeLocation,
                                        trackingLocation = TrackingLocation.proposalPage,
                                        isProposalSharable = self.state.isProposalSharable
                                      )
                                    )()
                                })
                              ),
                              if (self.state.maybeOperation.isDefined) {
                                <.div(^.className := ProposalStyles.operationInfoWrapper)(
                                  <.ProposalSOperationInfosComponent(
                                    ^.wrapped := ProposalSOperationInfosProps(
                                      operation = self.state.maybeOperation.get,
                                      language = self.props.wrapped.language,
                                      country = self.props.wrapped.country
                                    )
                                  )()
                                )
                              } else {
                                self.state.maybeTheme.map { theme =>
                                  <.div(^.className := ProposalStyles.themeInfoWrapper)(
                                    <.p(^.className := js.Array(TextStyles.mediumText, ProposalStyles.themeInfo))(
                                      unescape(I18n.t("proposal.posted-in")),
                                      <.Link(
                                        ^.to := s"/${self.props.wrapped.country}/theme/${theme.slug}",
                                        ^.className := js.Array(TextStyles.title, ProposalStyles.themeName)
                                      )(theme.title)
                                    )
                                  )
                                }
                              }
                            )
                          )
                        )
                      ),
                      self.state.maybeOperation.map { operation =>
                        self.state.maybeProposal.map { proposal =>
                          <.ShareProposalPageComponent(
                            ^.wrapped := ShareProposalProps(
                              proposal = proposal,
                              operation = operation,
                              language = self.props.wrapped.language,
                              country = self.props.wrapped.country
                            )
                          )()
                        }
                      }.toSeq
                    )
                  } else {
                    <.SpinnerComponent.empty
                  }
                )
              )
            ),
            self.state.maybeOperation.map { operation =>
              <.OperationShowcaseContainerComponent(
                ^.wrapped := OperationShowcaseContainerProps(operation = operation)
              )()
            }.getOrElse(<.NavInThemesContainerComponent.empty),
            <.MainFooterComponent.empty,
            <.style()(ProposalStyles.render[String])
          )
      })
    )
}

object ProposalStyles extends StyleSheet.Inline {

  import dsl._

  val fullHeight: StyleA =
    style(height(100.%%))

  val wrapper: Boolean => StyleA = styleF.bool(
    isLoaded =>
      if (isLoaded) {
        styleS(
          height :=! s"calc(100% - ${ThemeStyles.SpacingValue.evenLarger.pxToEm().value})",
          backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
        )
      } else {
        styleS(backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))
    }
  )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val mainHeaderWrapper: StyleA =
    style(
      paddingBottom(50.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
    )

  val articleCell: StyleA =
    style(verticalAlign.middle, padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`))

  val shareArticleCell: StyleA =
    style(paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()))

  val article: StyleA =
    style(
      height(90.%%),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val articleInnerWrapper: StyleA =
    style(paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()), paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val infosWrapper: StyleA =
    style(
      textAlign.center,
      position.relative,
      paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()),
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
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()), overflow.hidden)

  val voteWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val operationInfoWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val themeInfoWrapper: StyleA =
    style(
      position.relative,
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      &.after(
        content := "''",
        position.absolute,
        bottom(100.%%),
        left(50.%%),
        transform := s"translateX(-50%)",
        marginTop(-0.5.px),
        height(1.px),
        width(90.pxToEm()),
        backgroundColor(ThemeStyles.BorderColor.lighter)
      )
    )

  val themeInfo: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.light))

  val themeName: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

}
