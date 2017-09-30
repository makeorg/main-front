package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.proposal.ProposalWithTags.ProposalWithTagsProps
import org.make.front.components.submitProposal.SubmitProposal.SubmitProposalProps
import org.make.front.components.theme.ResultsInThemeStyles
import org.make.front.facades.ReactInfiniteScroller.{
  ReactInfiniteScrollerVirtualDOMAttributes,
  ReactInfiniteScrollerVirtualDOMElements
}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{ProposalSearchResult, Proposal => ProposalModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._

object SearchResults {
  final case class SearchResultsProps(
    onMoreResultsRequested: (Seq[ProposalModel], Option[String]) => Future[ProposalSearchResult],
    searchValue: Option[String]
  )

  final case class SearchResultsState(listProposals: Seq[ProposalModel],
                                      initialLoad: Boolean,
                                      hasRequestedMore: Boolean,
                                      hasMore: Boolean,
                                      isProposalModalOpened: Boolean)

  object SearchResultsState {
    val empty = SearchResultsState(
      listProposals = Seq.empty,
      initialLoad = true,
      hasRequestedMore = false,
      hasMore = false,
      isProposalModalOpened = false
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
          val closeProposalModal: () => Unit = () => {
            self.setState(state => state.copy(isProposalModalOpened = false))
          }

          val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
          }

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
                        listProposals = searchResult.proposals,
                        hasMore = searchResult.hasMore,
                        initialLoad = false
                      )
                    )
                  case Failure(_) => // parent container will dispatch an error
                }
          }

          val noResults: ReactElement = {
            <.div(^.className := LayoutRulesStyles.centeredRow)(
              <.div(^.className := Seq(LayoutRulesStyles.col, SearchResultsStyles.noResults))(
                <.p(^.className := SearchResultsStyles.noResultsSmiley)("😞"),
                <.p(
                  ^.className := Seq(TextStyles.mediumText, SearchResultsStyles.noResultsMessage),
                  ^.dangerouslySetInnerHTML := I18n.t(s"content.search.matrix.noContent")
                )(),
                <.p(^.className := Seq(SearchResultsStyles.searchedExpression, TextStyles.mediumTitle))(
                  unescape("«&nbsp;" + self.props.wrapped.searchValue.getOrElse("") + "&nbsp;»")
                ),
                <.hr(^.className := SearchResultsStyles.noResultsMessageSeparator)(),
                <.p(^.className := Seq(TextStyles.mediumText))(I18n.t("content.search.proposeIntro")),
                <.button(
                  ^.className := Seq(
                    SearchResultsStyles.openProposalModalButton,
                    CTAStyles.basic,
                    CTAStyles.basicOnButton
                  ),
                  ^.onClick := openProposalModal
                )(
                  <.i(^.className := Seq(FontAwesomeStyles.pencil, FontAwesomeStyles.fa))(),
                  unescape("&nbsp;" + I18n.t("content.search.propose"))
                ),
                <.FullscreenModalComponent(
                  ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, closeProposalModal)
                )(<.SubmitProposalComponent(^.wrapped := SubmitProposalProps(onProposalProposed = () => {
                  self.setState(_.copy(isProposalModalOpened = false))
                }))())
              )
            )
          }

          def proposals(proposals: Seq[ProposalModel]) =
            Seq(
              <.InfiniteScroll(
                ^.className := LayoutRulesStyles.centeredRow,
                ^.element := "ul",
                ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
                ^.initialLoad := true,
                ^.pageStart := 1,
                ^.loadMore := onSeeMore,
                ^.loader := <.SpinnerComponent.empty
              )(if (proposals.nonEmpty) {
                proposals.map(
                  proposal =>
                    <.li(
                      ^.className := Seq(
                        LayoutRulesStyles.col,
                        LayoutRulesStyles.colHalfBeyondMedium,
                        LayoutRulesStyles.colQuarterBeyondLarge
                      )
                    )(<.ProposalWithTagsComponent(^.wrapped := ProposalWithTagsProps(proposal = proposal))())
                )
              } else {
                Seq(<.span.empty)
              }),
              if (self.state.hasMore && !self.state.hasRequestedMore) {
                <.div(^.className := Seq(ResultsInThemeStyles.seeMoreButtonWrapper, LayoutRulesStyles.col))(
                  <.button(^.onClick := { () =>
                    onSeeMore(1)
                  }, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                    unescape(I18n.t("content.theme.seeMoreProposals"))
                  )
                )
              }
            )

          val proposalsToDisplay: Seq[ProposalModel] = self.state.listProposals
          <("search-resulst")()(if (self.state.initialLoad || proposalsToDisplay.nonEmpty) {
            <.section(^.className := SearchResultsStyles.resultsWrapper)(if (!self.state.initialLoad) {
              <.h1(
                ^.dangerouslySetInnerHTML := unescape(
                  I18n
                    .t(s"content.search.title", Replacements(("results", self.state.listProposals.size.toString)))
                )
              )()
            }, proposals(proposalsToDisplay))
          } else {
            <.section(^.className := SearchResultsStyles.noResultsWrapper)(noResults)
          }, <.NavInThemesContainerComponent.empty, <.style()(SearchResultsStyles.render[String]))
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

  val noResultsWrapper: StyleA = style(
    minHeight(100.%%),
    paddingTop(50.pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
    ThemeStyles.MediaQueries.beyondSmall(paddingTop(80.pxToEm()))
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

  val noResults: StyleA = style(
    paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()),
    paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
    textAlign.center
  )

  val noResultsMessageSeparator: StyleA = style(
    display.block,
    maxWidth(235.pxToEm()),
    margin :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} auto",
    border.none,
    borderTop :=! s"1px solid ${ThemeStyles.BorderColor.veryLight.value}",
    ThemeStyles.MediaQueries
      .beyondSmall(maxWidth(470.pxToEm()), margin :=! s"${ThemeStyles.SpacingValue.large.pxToEm().value} auto")
  )

  val noResultsMessage: StyleA =
    style(unsafeChild("strong")(ThemeStyles.Font.circularStdBold))

  val searchedExpression: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm(20)),
      ThemeStyles.MediaQueries
        .beyondSmall(marginTop(ThemeStyles.SpacingValue.small.pxToEm(34)))
    )

  val openProposalModalButton: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))
}
