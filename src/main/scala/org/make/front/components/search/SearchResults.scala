package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalWithTags.ProposalWithTagsProps
import org.make.front.components.search.NoResultToSearch.NoResultToSearchProps
import org.make.front.facades.ReactInfiniteScroller.{
  ReactInfiniteScrollerVirtualDOMAttributes,
  ReactInfiniteScrollerVirtualDOMElements
}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.SearchResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
object SearchResults {
  final case class SearchResultsProps(
    onMoreResultsRequested: (Seq[ProposalModel], Option[String]) => Future[SearchResponse],
    searchValue: Option[String]
  )

  final case class SearchResultsState(listProposals: Seq[ProposalModel],
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

          def proposals(proposals: Seq[ProposalModel]) =
            Seq(
              <.InfiniteScroll(
                ^.element := "ul",
                ^.className := SearchResultsStyles.listItems,
                ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
                ^.initialLoad := true,
                ^.pageStart := 1,
                ^.loadMore := onSeeMore,
                ^.loader := <.li(^.className := SearchResultsStyles.spinnerWrapper)(<.SpinnerComponent.empty)
              )(if (proposals.nonEmpty) {
                proposals.map(
                  proposal =>
                    <.li(
                      ^.className := Seq(
                        SearchResultsStyles.item,
                        ColRulesStyles.col,
                        ColRulesStyles.colHalfBeyondMedium,
                        ColRulesStyles.colQuarterBeyondLarge
                      )
                    )(<.ProposalWithTagsComponent(^.wrapped := ProposalWithTagsProps(proposal = proposal))())
                )
              }),
              if (self.state.hasMore && !self.state.hasRequestedMore) {
                <.div(^.className := Seq(SearchResultsStyles.seeMoreButtonWrapper, ColRulesStyles.col))(
                  <.button(^.onClick := { () =>
                    onSeeMore(1)
                  }, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                    unescape(I18n.t("content.theme.matrix.seeMoreProposals"))
                  )
                )
              }
            )

          val proposalsToDisplay: Seq[ProposalModel] = self.state.listProposals
          <("search-results")()(if (self.state.initialLoad || proposalsToDisplay.nonEmpty) {
            <.section(^.className := Seq(SearchResultsStyles.resultsWrapper))(
              <.div(^.className := Seq(SearchResultsStyles.resultsInnerWrapper, RowRulesStyles.centeredRow))(
                if (!self.state.initialLoad) {
                  <.header(^.className := Seq(SearchResultsStyles.resultsHeader, ColRulesStyles.col))(
                    <.h2(
                      ^.className := Seq(TextStyles.mediumText, SearchResultsStyles.searchedExpressionIntro),
                      ^.dangerouslySetInnerHTML := I18n
                        .t(s"content.search.title", Replacements(("results", self.state.resultsCount.toString)))
                    )(),
                    <.h1(^.className := Seq(SearchResultsStyles.searchedExpression, TextStyles.mediumTitle))(
                      unescape("«&nbsp;" + self.props.wrapped.searchValue.getOrElse("") + "&nbsp;»")
                    )
                  )
                },
                proposals(proposalsToDisplay)
              ),
              <.NavInThemesContainerComponent.empty
            )
          } else {
            <.section(^.className := SearchResultsStyles.noResultsWrapper)(
              <.NoResultToSearchComponent(
                ^.wrapped := NoResultToSearchProps(searchValue = self.props.wrapped.searchValue)
              )(),
              <.NavInThemesContainerComponent.empty
            )
          }, <.style()(SearchResultsStyles.render[String]))
      })
    )
}

object SearchResultsStyles extends StyleSheet.Inline {

  import dsl._

  val resultsWrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      paddingTop(50.pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(80.pxToEm()))
    )

  val resultsInnerWrapper: StyleA =
    style(paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()), paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()))

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

  val listItems: StyleA = style(display.flex, flexWrap.wrap)

  val item: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val spinnerWrapper: StyleA =
    style(width(100.%%), margin(ThemeStyles.SpacingValue.small.pxToEm()))

  val seeMoreButtonWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
    textAlign.center
  )

  val noResultsWrapper: StyleA = style(
    minHeight(100.%%),
    paddingTop(50.pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
    ThemeStyles.MediaQueries.beyondSmall(paddingTop(80.pxToEm()))
  )
}
