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
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object SearchResults {
  final case class SearchResultsProps(onMoreResultsRequested: (Seq[Proposal], Option[String]) => Future[SearchResult],
                                      searchValue: Option[String],
                                      maybeSequence: Option[SequenceId],
                                      maybeOperation: Option[OperationModel],
                                      maybeLocation: Option[Location],
                                      isConnected: Boolean)

  final case class SearchResultsState(listProposals: Seq[Proposal],
                                      initialLoad: Boolean,
                                      hasRequestedMore: Boolean,
                                      hasMore: Boolean,
                                      resultsCount: Int)

  object SearchResultsState {
    val empty =
      SearchResultsState(
        listProposals = Seq.empty,
        initialLoad = true,
        hasRequestedMore = false,
        hasMore = false,
        resultsCount = 0
      )
  }

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[SearchResultsProps, SearchResultsState](displayName = "SearchResults", getInitialState = { _ =>
        SearchResultsState.empty
      }, shouldComponentUpdate = { (self, props, state) =>
        self.props.wrapped.isConnected != props.wrapped.isConnected ||
        self.state.listProposals.size != state.listProposals.size
      }, componentWillReceiveProps = (self, props) => {
        if (self.props.wrapped.searchValue != props.wrapped.searchValue) {
          self.setState(SearchResultsState.empty)
        }
      }, render = {
        self =>
          val onSeeMore: (Int) => Unit = {
            _ =>
              self.props.wrapped
                .onMoreResultsRequested(self.state.listProposals, self.props.wrapped.searchValue)
                .onComplete {
                  case Success(searchResult) =>
                    if (self.state.initialLoad) {
                      TrackingService.track(
                        "display-search-results-page",
                        TrackingContext(
                          TrackingLocation.searchResultsPage,
                          self.props.wrapped.maybeOperation.map(_.slug)
                        ),
                        Map("results-count" -> searchResult.total.toString)
                      )
                    }
                    self.setState(
                      s =>
                        s.copy(
                          listProposals = searchResult.results,
                          hasMore = searchResult.total > searchResult.results.size,
                          initialLoad = false,
                          resultsCount = searchResult.total,
                          hasRequestedMore = !s.initialLoad
                      )
                    )
                  case Failure(_) => // parent container will dispatch an error
                }
          }

          def proposals(proposals: Seq[Proposal]) =
            Seq(
              <.InfiniteScroll(
                ^.element := "ul",
                ^.className := Seq(SearchResultsStyles.itemsList, LayoutRulesStyles.centeredRowWithCols),
                ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
                ^.initialLoad := true,
                ^.pageStart := 1,
                ^.loadMore := onSeeMore,
                ^.loader := <.li(^.className := SearchResultsStyles.spinnerWrapper)(<.SpinnerComponent.empty)
              )(if (proposals.nonEmpty) {
                val counter = new Counter()
                proposals.map(
                  proposal =>
                    <.li(
                      ^.className := Seq(
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
                            maybeOperation = self.props.wrapped.maybeOperation,
                            maybeLocation = self.props.wrapped.maybeLocation,
                            trackingLocation = TrackingLocation.searchResultsPage
                          )
                      )()
                  )
                )
              } else { <.div.empty }),
              if (self.state.hasMore && !self.state.hasRequestedMore) {
                <.div(^.className := Seq(SearchResultsStyles.seeMoreButtonWrapper, LayoutRulesStyles.centeredRow))(
                  <.button(^.onClick := { () =>
                    onSeeMore(1)
                    TrackingService
                      .track(
                        "click-proposal-viewmore",
                        TrackingContext(
                          TrackingLocation.searchResultsPage,
                          self.props.wrapped.maybeOperation.map(_.slug)
                        )
                      )
                  }, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                    unescape(I18n.t("search.results.see-more"))
                  )
                )
              }
            )

          val proposalsToDisplay: Seq[Proposal] = self.state.listProposals

          <.section(^.className := SearchResultsStyles.wrapper)(
            <.div(
              ^.className := Seq(
                TableLayoutStyles.fullHeightWrapper,
                SearchResultsStyles.background(proposalsToDisplay.nonEmpty)
              )
            )(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := TableLayoutStyles.cell)(
                  <.div(^.className := SearchResultsStyles.mainHeaderWrapper)(<.MainHeaderComponent.empty)
                )
              ),
              <.div(^.className := TableLayoutStyles.fullHeightRow)(
                <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                  if (self.state.initialLoad || proposalsToDisplay.nonEmpty) {
                    Seq(<.div(^.className := Seq(SearchResultsStyles.resultsWrapper))(if (proposalsToDisplay.nonEmpty) {
                      <.header(^.className := Seq(SearchResultsStyles.resultsHeader, LayoutRulesStyles.centeredRow))(
                        <.h1(
                          ^.className := Seq(TextStyles.mediumText, SearchResultsStyles.searchedExpressionIntro),
                          ^.dangerouslySetInnerHTML := I18n
                            .t("search.results.intro", Replacements(("total", self.state.resultsCount.toString)))
                        )(),
                        <.h2(^.className := Seq(SearchResultsStyles.searchedExpression, TextStyles.mediumTitle))(
                          unescape("«&nbsp;" + self.props.wrapped.searchValue.getOrElse("") + "&nbsp;»")
                        )
                      )
                    }, proposals(proposalsToDisplay)))
                  },
                  if (!self.state.initialLoad && proposalsToDisplay.isEmpty) {
                    <.NoResultToSearchComponent(
                      ^.wrapped := NoResultToSearchProps(
                        searchValue = self.props.wrapped.searchValue,
                        maybeLocation = self.props.wrapped.maybeLocation
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
      })
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
    style(visibility.hidden)

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
