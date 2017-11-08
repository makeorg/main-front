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
import org.make.front.models.Proposal
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object SearchResults {
  final case class SearchResultsProps(onMoreResultsRequested: (Seq[Proposal], Option[String]) => Future[SearchResult],
                                      searchValue: Option[String])

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
      }, componentWillReceiveProps = (self, _) => {
        self.setState(SearchResultsState.empty)
      }, render = {
        self =>
          val onSeeMore: (Int) => Unit = {
            _ =>
              if (!self.state.initialLoad) {
                self.setState(_.copy(hasRequestedMore = true))
              }
              self.props.wrapped
                .onMoreResultsRequested(self.state.listProposals, self.props.wrapped.searchValue)
                .onComplete {
                  case Success(searchResult) =>
                    self.setState(
                      _.copy(
                        listProposals = searchResult.results,
                        hasMore = searchResult.total > searchResult.results.size,
                        initialLoad = false,
                        resultsCount = searchResult.total
                      )
                    )
                  case Failure(_) => // parent container will dispatch an error
                }
          }

          def proposals(proposals: Seq[Proposal]) =
            Seq(
              <.InfiniteScroll(
                ^.element := "ul",
                ^.className := SearchResultsStyles.itemsList,
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
                          ProposalTileWithThemeContainerProps(proposal = proposal, index = counter.getAndIncrement())
                      )()
                  )
                )
              } else { <.span.empty }),
              if (self.state.hasMore && !self.state.hasRequestedMore) {
                <.div(^.className := Seq(SearchResultsStyles.seeMoreButtonWrapper, ColRulesStyles.col))(
                  <.button(^.onClick := { () =>
                    onSeeMore(1)
                  }, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                    unescape(I18n.t("search.results.see-more"))
                  )
                )
              }
            )

          val proposalsToDisplay: Seq[Proposal] = self.state.listProposals

          val hasResults: Boolean = self.state.initialLoad || proposalsToDisplay.nonEmpty
          <("search-results")()(
            <.div(^.className := SearchResultsStyles.wrapper(hasResults))(
              <.div(^.className := SearchResultsStyles.mainHeaderWrapper)(<.MainHeaderComponent.empty),
              if (hasResults) {
                Seq(
                  <.div(^.className := Seq(SearchResultsStyles.resultsWrapper, RowRulesStyles.centeredRow))(
                    if (!self.state.initialLoad) {
                      <.header(^.className := Seq(SearchResultsStyles.resultsHeader, ColRulesStyles.col))(
                        <.h1(
                          ^.className := Seq(TextStyles.mediumText, SearchResultsStyles.searchedExpressionIntro),
                          ^.dangerouslySetInnerHTML := I18n
                            .t("search.results.intro", Replacements(("total", self.state.resultsCount.toString)))
                        )(),
                        <.h2(^.className := Seq(SearchResultsStyles.searchedExpression, TextStyles.mediumTitle))(
                          unescape("«&nbsp;" + self.props.wrapped.searchValue.getOrElse("") + "&nbsp;»")
                        )
                      )
                    },
                    proposals(proposalsToDisplay)
                  )
                )

              } else {
                <.NoResultToSearchComponent(
                  ^.wrapped := NoResultToSearchProps(searchValue = self.props.wrapped.searchValue)
                )()
              }
            ),
            <.div(^.className := SearchResultsStyles.navInThemesWrapper(hasResults))(
              <.NavInThemesContainerComponent.empty
            ),
            <.style()(SearchResultsStyles.render[String])
          )
      })
    )
}

object SearchResultsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: (Boolean) => StyleA = styleF.bool(
    hasResults =>
      if (hasResults) {
        styleS(minHeight(100.%%), backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))
      } else {
        styleS(minHeight(100.%%))
    }
  )

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)

  val navInThemesWrapper: (Boolean) => StyleA = styleF.bool(
    hasResults =>
      if (hasResults) {
        styleS(backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))
      } else {
        styleS()
    }
  )

  val resultsWrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

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
      margin :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} ${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val seeMoreButtonWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm())
    ),
    textAlign.center
  )

}
