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

package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalTileWithThemeContainer.ProposalTileWithThemeContainerProps
import org.make.front.components.search.NoResultToSearch.NoResultToSearchProps
import org.make.front.facades.ReactInfiniteScroller.{
  ReactInfiniteScrollerVirtualDOMAttributes,
  ReactInfiniteScrollerVirtualDOMElements
}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Location, Proposal, SequenceId, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object SearchResults {
  final case class SearchResultsProps(
    onMoreResultsRequested: (js.Array[Proposal], Option[String]) => Future[SearchResult],
    searchValue: Option[String],
    maybeSequence: Option[SequenceId],
    maybeOperation: Option[OperationModel],
    maybeLocation: Option[Location],
    isConnected: Boolean,
    language: String
  )

  final case class SearchResultsState(listProposals: js.Array[Proposal],
                                      initialLoad: Boolean,
                                      hasRequestedMore: Boolean,
                                      hasMore: Boolean,
                                      resultsCount: Int,
                                      maybeOperation: Option[OperationModel])

  object SearchResultsState {
    val empty =
      SearchResultsState(
        listProposals = js.Array(),
        initialLoad = true,
        hasRequestedMore = false,
        hasMore = false,
        resultsCount = 0,
        maybeOperation = None
      )
  }

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[SearchResultsProps, SearchResultsState](
        displayName = "SearchResults",
        getInitialState = { self =>
          SearchResultsState(
            listProposals = js.Array(),
            initialLoad = true,
            hasRequestedMore = false,
            hasMore = false,
            resultsCount = 0,
            maybeOperation = self.props.wrapped.maybeOperation
          )
        },
        shouldComponentUpdate = { (self, props, state) =>
          self.props.wrapped.isConnected != props.wrapped.isConnected ||
          self.state.listProposals.lengthCompare(state.listProposals.size) != 0 ||
          (!state.initialLoad && state.listProposals.isEmpty)
        },
        componentWillReceiveProps = (self, props) => {
          if (self.props.wrapped.searchValue != props.wrapped.searchValue) {
            self.setState(SearchResultsState.empty)
          }
        },
        render = { self =>
          val onSeeMore: (Int) => Unit = {
            _ =>
              self.props.wrapped
                .onMoreResultsRequested(self.state.listProposals, self.props.wrapped.searchValue)
                .onComplete {
                  case Success(searchResult) =>
                    if (self.state.initialLoad) {
                      TrackingService.track(
                        eventName = "display-search-results-page",
                        trackingContext =
                          TrackingContext(TrackingLocation.searchResultsPage, self.state.maybeOperation.map(_.slug)),
                        parameters = Map("results-count" -> searchResult.total.toString)
                      )
                    }
                    self.setState(
                      s =>
                        s.copy(
                          listProposals = s.listProposals ++ searchResult.results,
                          hasMore = searchResult.total > searchResult.results.size,
                          initialLoad = false,
                          resultsCount = searchResult.total,
                          hasRequestedMore = !s.initialLoad
                      )
                    )
                  case Failure(_) => // parent container will dispatch an error
                }
          }

          def proposals(proposals: js.Array[Proposal]) =
            js.Array(
                <.InfiniteScroll(
                  ^.element := "ul",
                  ^.className := js.Array(SearchResultsStyles.itemsList, LayoutRulesStyles.centeredRowWithCols),
                  ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
                  ^.initialLoad := true,
                  ^.pageStart := 1,
                  ^.loadMore := onSeeMore,
                  ^.loader := <.li(^.className := SearchResultsStyles.spinnerWrapper)(<.SpinnerComponent.empty)
                )(if (proposals.nonEmpty) {
                  val counter = new Counter()
                  proposals
                    .map(
                      proposal =>
                        <.li(
                          ^.className := js.Array(
                            SearchResultsStyles.item,
                            ColRulesStyles.col,
                            ColRulesStyles.colHalfBeyondMedium,
                            ColRulesStyles.colQuarterBeyondLarge
                          )
                        )(
                          <.ProposalTileWithThemeContainerComponent(
                            ^.wrapped :=
                              ProposalTileWithThemeContainerProps(
                                proposal = proposal,
                                index = counter.getAndIncrement(),
                                maybeSequenceId = self.props.wrapped.maybeSequence,
                                maybeOperation = self.state.maybeOperation,
                                maybeLocation = self.props.wrapped.maybeLocation,
                                trackingLocation = TrackingLocation.searchResultsPage
                              )
                          )()
                      )
                    )
                    .toSeq
                } else { <.div.empty }),
                if (self.state.hasMore && !self.state.hasRequestedMore) {
                  <.div(
                    ^.className := js.Array(SearchResultsStyles.seeMoreButtonWrapper, LayoutRulesStyles.centeredRow)
                  )(<.button(^.onClick := { () =>
                    onSeeMore(1)
                    TrackingService
                      .track(
                        eventName = "click-proposal-viewmore",
                        trackingContext =
                          TrackingContext(TrackingLocation.searchResultsPage, self.state.maybeOperation.map(_.slug))
                      )
                  }, ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton))(unescape(I18n.t("search.results.see-more"))))
                }
              )
              .toSeq

          val proposalsToDisplay: js.Array[Proposal] = self.state.listProposals

          <.section(^.className := SearchResultsStyles.wrapper)(
            <.div(
              ^.className := js
                .Array(TableLayoutStyles.fullHeightWrapper, SearchResultsStyles.background(proposalsToDisplay.nonEmpty))
            )(
              <.div(^.className := TableLayoutStyles.fullHeightRow)(
                <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                  if (self.state.initialLoad || proposalsToDisplay.nonEmpty) {
                    js.Array(
                        <.div(^.className := js.Array(SearchResultsStyles.resultsWrapper))(
                          if (proposalsToDisplay.nonEmpty) {
                            <.header(
                              ^.className := js.Array(SearchResultsStyles.resultsHeader, LayoutRulesStyles.centeredRow)
                            )(
                              <.h1(
                                ^.className := js
                                  .Array(TextStyles.mediumText, SearchResultsStyles.searchedExpressionIntro),
                                ^.dangerouslySetInnerHTML := I18n
                                  .t("search.results.intro", Replacements(("total", self.state.resultsCount.toString)))
                              )(),
                              <.h2(
                                ^.className := js.Array(SearchResultsStyles.searchedExpression, TextStyles.mediumTitle)
                              )(unescape("«&nbsp;" + self.props.wrapped.searchValue.getOrElse("") + "&nbsp;»"))
                            )
                          },
                          proposals(proposalsToDisplay)
                        )
                      )
                      .toSeq
                  },
                  if (!self.state.initialLoad && proposalsToDisplay.isEmpty) {
                    <.NoResultToSearchComponent(
                      ^.wrapped := NoResultToSearchProps(
                        searchValue = self.props.wrapped.searchValue,
                        maybeLocation = self.props.wrapped.maybeLocation,
                        maybeOperation = self.state.maybeOperation,
                        language = self.props.wrapped.language
                      )
                    )()
                  }
                )
              )
            ),
            <.div(
              ^.className :=
                SearchResultsStyles.background(proposalsToDisplay.nonEmpty)
            )(<.NavInThemesContainerComponent.empty),
            <.style()(SearchResultsStyles.render[String])
          )
        }
      )
    )
}

object SearchResultsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(display.inherit)

  val background: (Boolean) => StyleA = styleF.bool(
    hasResults =>
      if (hasResults) {
        styleS(backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))
      } else {
        styleS(backgroundColor(ThemeStyles.BackgroundColor.white))
    }
  )

  val mainHeaderWrapper: StyleA =
    style(
      paddingBottom(50.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
    )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val resultsWrapper: StyleA =
    style(paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()), paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val resultsHeader: StyleA =
    style(textAlign.center)

  val searchedExpressionIntro: StyleA =
    style(unsafeChild("strong")(ThemeStyles.Font.circularStdBold))

  val searchedExpression: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm(20)),
      ThemeStyles.MediaQueries
        .beyondSmall(marginTop(ThemeStyles.SpacingValue.small.pxToEm(34)))
    )

  val itemsList: StyleA = style(display.flex, flexWrap.wrap)

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
}
