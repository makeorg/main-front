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

package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalTileWithTags.ProposalTileWithTagsProps
import org.make.front.components.tags.FilterByTags.FilterByTagsProps
import org.make.front.facades.I18n
import org.make.front.facades.ReactInfiniteScroller.{
  ReactInfiniteScrollerVirtualDOMAttributes,
  ReactInfiniteScrollerVirtualDOMElements
}
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  ProposalId        => ProposalIdModel,
  Qualification     => QualificationModel,
  Tag               => TagModel,
  TranslatedTheme   => TranslatedThemeModel,
  Vote              => VoteModel
}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object ResultsInTheme {

  case class ResultsInThemeProps(
    theme: TranslatedThemeModel,
    onMoreResultsRequested: (js.Array[ProposalModel], js.Array[TagModel], Option[Int]) => Future[SearchResult],
    onTagSelectionChange: (js.Array[TagModel], Option[Int])                            => Future[SearchResult],
    preselectedTags: js.Array[TagModel],
    maybeOperation: Option[OperationModel],
    maybeLocation: Option[LocationModel],
    country: String
  )

  case class ResultsInThemeState(listProposals: js.Array[ProposalModel],
                                 selectedTags: js.Array[TagModel],
                                 initialLoad: Boolean,
                                 hasRequestedMore: Boolean,
                                 hasMore: Boolean,
                                 maybeSeed: Option[Int] = None)

  lazy val reactClass: ReactClass = {
    def onSuccessfulVote(proposalId: ProposalIdModel,
                         self: Self[ResultsInThemeProps, ResultsInThemeState]): (VoteModel) => Unit = { (voteModel) =>
      def mapProposal(proposal: ProposalModel): ProposalModel = {
        if (proposal.id == proposalId) {
          proposal.copy(votes = proposal.votes.map { vote =>
            if (vote.key == voteModel.key) {
              voteModel
            } else {
              vote
            }
          })
        } else {
          proposal
        }
      }

      val updatedProposals = self.state.listProposals.map(mapProposal)
      self.setState(_.copy(listProposals = updatedProposals))
    }

    def onSuccessfulQualification(
      proposalId: ProposalIdModel,
      self: Self[ResultsInThemeProps, ResultsInThemeState]
    ): (String, QualificationModel) => Unit = { (voteKey, qualificationModel) =>
      def mapProposal(proposal: ProposalModel): ProposalModel = {
        if (proposal.id == proposalId) {
          proposal.copy(votes = proposal.votes.map { vote =>
            if (vote.key == voteKey) {
              vote.copy(qualifications = vote.qualifications.map { qualification =>
                if (qualification.key == qualificationModel.key) {
                  qualificationModel
                } else {
                  qualification
                }
              })
            } else {
              vote
            }
          })
        } else {
          proposal
        }
      }

      val updatedProposals = self.state.listProposals.map(mapProposal)
      self.setState(_.copy(listProposals = updatedProposals))
    }

    React.createClass[ResultsInThemeProps, ResultsInThemeState](
      displayName = "ResultsInTheme",
      getInitialState = { self =>
        ResultsInThemeState(
          selectedTags = self.props.wrapped.preselectedTags,
          listProposals = js.Array(),
          initialLoad = true,
          hasRequestedMore = false,
          hasMore = false
        )
      },
      componentWillReceiveProps = { (self, nextProps) =>
        self.setState(
          ResultsInThemeState(
            selectedTags = nextProps.wrapped.preselectedTags,
            listProposals = js.Array(),
            initialLoad = true,
            hasRequestedMore = false,
            hasMore = false
          )
        )
      },
      shouldComponentUpdate = { (self, _, state) =>
        self.state.listProposals.map(_.id).toSet != state.listProposals.map(_.id).toSet
      },
      render = { (self) =>
        val onSeeMore: (Int) => Unit = {
          _ =>
            self.props.wrapped
              .onMoreResultsRequested(self.state.listProposals, self.state.selectedTags, self.state.maybeSeed)
              .onComplete {
                case Success(searchResult) =>
                  self.setState(
                    s =>
                      s.copy(
                        listProposals = searchResult.results,
                        hasMore = searchResult.total > searchResult.results.size,
                        initialLoad = false,
                        maybeSeed = searchResult.seed,
                        hasRequestedMore = !s.initialLoad
                    )
                  )
                case Failure(_) => // Let parent handle logging error
              }
        }

        val noResults: ReactElement =
          <.div(^.className := js.Array(ResultsInThemeStyles.noResults, LayoutRulesStyles.centeredRow))(
            <.p(^.className := ResultsInThemeStyles.noResultsSmiley)("😞"),
            <.p(
              ^.className := js.Array(TextStyles.mediumText, ResultsInThemeStyles.noResultsMessage),
              ^.dangerouslySetInnerHTML := I18n.t("theme.results.no-results")
            )()
          )

        val onTagsChange: (js.Array[TagModel]) => Unit = {
          tags =>
            val previousSelectedTags = self.state.selectedTags
            val changedTags = if (previousSelectedTags.lengthCompare(tags.size) > 0) {
              val tagsAsStrings = tags.map(_.tagId.value)
              previousSelectedTags.filter(tag => !tagsAsStrings.contains(tag.tagId.value))
            } else {
              val tagsAsStrings = previousSelectedTags.map(_.tagId.value)
              tags.filter(tag => !tagsAsStrings.contains(tag.tagId.value))
            }

            val action = if (previousSelectedTags.lengthCompare(tags.size) > 0) {
              "deselect"
            } else {
              "select"
            }
            changedTags.foreach { tag =>
              TrackingService.track(
                eventName = "click-tag-action",
                trackingContext = TrackingContext(TrackingLocation.themePage),
                parameters = Map.empty,
                internalOnlyParameters = Map("themeId" -> self.props.wrapped.theme.id.value, "nature" -> action, "tag-name" -> tag.label)
              )
            }

            self.setState(_.copy(selectedTags = tags))
            self.props.wrapped.onTagSelectionChange(tags, self.state.maybeSeed).onComplete {
              case Success(searchResult) =>
                self.setState(
                  _.copy(
                    listProposals = searchResult.results,
                    hasMore = searchResult.total > searchResult.results.size,
                    maybeSeed = searchResult.seed
                  )
                )
              case Failure(_) => // Let parent handle logging error
            }
        }

        def proposals(proposals: js.Array[ProposalModel], country: String) =
          js.Array(
              <.InfiniteScroll(
                ^.element := "ul",
                ^.className := js.Array(ResultsInThemeStyles.itemsList, LayoutRulesStyles.centeredRowWithCols),
                ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
                ^.initialLoad := true,
                ^.pageStart := 1,
                ^.loadMore := onSeeMore,
                ^.loader := <.li(^.className := ResultsInThemeStyles.spinnerWrapper)(<.SpinnerComponent.empty)
              )(if (proposals.nonEmpty) {
                val counter = new Counter()
                proposals
                  .map(
                    proposal =>
                      <.li(
                        ^.className := js.Array(
                          ResultsInThemeStyles.item,
                          ColRulesStyles.col,
                          ColRulesStyles.colHalfBeyondMedium,
                          ColRulesStyles.colQuarterBeyondLarge
                        )
                      )(
                        <.ProposalTileWithTagsComponent(
                          ^.wrapped := ProposalTileWithTagsProps(
                            proposal = proposal,
                            handleSuccessfulVote = onSuccessfulVote(proposal.id, self),
                            handleSuccessfulQualification = onSuccessfulQualification(proposal.id, self),
                            index = counter.getAndIncrement(),
                            maybeTheme = Some(self.props.wrapped.theme),
                            maybeOperation = self.props.wrapped.maybeOperation,
                            maybeSequenceId = None,
                            maybeLocation = self.props.wrapped.maybeLocation,
                            trackingLocation = TrackingLocation.themePage,
                            country = country
                          )
                        )()
                    )
                  )
                  .toSeq
              } else { <.div.empty }),
              if (self.state.hasMore && !self.state.hasRequestedMore) {
                <.div(
                  ^.className := js.Array(ResultsInThemeStyles.seeMoreButtonWrapper, LayoutRulesStyles.centeredRow)
                )(<.button(^.onClick := (() => {
                  onSeeMore(-1)
                  TrackingService.track(
                    eventName = "click-proposal-viewmore",
                    trackingContext = TrackingContext(TrackingLocation.themePage),
                    parameters = Map.empty,
                    internalOnlyParameters = Map("themeId" -> self.props.wrapped.theme.id.value)
                  )
                }), ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton))(unescape(I18n.t("theme.results.see-more"))))
              }
            )
            .toSeq

        val proposalsToDisplay: js.Array[ProposalModel] = self.state.listProposals

        <.section(^.className := ResultsInThemeStyles.wrapper)(
          <.header(^.className := LayoutRulesStyles.centeredRow)(
            <.h2(^.className := TextStyles.mediumTitle)(unescape(I18n.t("theme.results.title")))
          ),
          <.div(^.className := js.Array(LayoutRulesStyles.centeredRow, ResultsInThemeStyles.tagsNavWrapper))(
            <.FilterByTagsComponent(^.wrapped := FilterByTagsProps(self.props.wrapped.theme.tags, onTagsChange))()
          ),
          if (self.state.initialLoad || proposalsToDisplay.nonEmpty) {
            proposals(proposalsToDisplay, self.props.wrapped.country)
          } else {
            noResults
          },
          <.style()(ResultsInThemeStyles.render[String])
        )
      }
    )
  }
}

object ResultsInThemeStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(ThemeStyles.SpacingValue.medium.pxToEm())),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm())
    )

  val tagsNavWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val itemsList: StyleA = style(display.flex, flexWrap.wrap, width(100.%%))

  val item: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val spinnerWrapper: StyleA =
    style(
      width(100.%%),
      lineHeight(0),
      margin(
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        `0`,
        ThemeStyles.SpacingValue.small.pxToEm()
      )
    )

  val seeMoreButtonWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(marginTop(ThemeStyles.SpacingValue.small.pxToEm())),
    textAlign.center
  )

  val noResults: StyleA = style(
    paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()),
    paddingBottom((ThemeStyles.SpacingValue.larger - ThemeStyles.SpacingValue.small).pxToEm()),
    textAlign.center
  )

  val noResultsSmiley: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(34)),
      fontSize(34.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries
        .beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm(48)), fontSize(48.pxToEm()))
    )

  val noResultsMessage: StyleA =
    style(unsafeChild("strong")(ThemeStyles.Font.circularStdBold))

}
