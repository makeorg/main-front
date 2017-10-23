package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalTileWithTags.ProposalTileWithTagsProps
import org.make.front.components.tags.FilterByTags.FilterByTagsProps
import org.make.front.facades.I18n
import org.make.front.facades.ReactInfiniteScroller.{ReactInfiniteScrollerVirtualDOMAttributes, ReactInfiniteScrollerVirtualDOMElements}
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal, Tag => TagModel, Theme => ThemeModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.internal.mutable.StyleSheet

object ResultsInTheme {

  case class ResultsInThemeProps(theme: ThemeModel,
                                 onMoreResultsRequested: (Seq[Proposal], Seq[TagModel]) => Future[SearchResult],
                                 onTagSelectionChange: (Seq[TagModel])                       => Future[SearchResult],
                                 proposals: Future[SearchResult],
                                 preselectedTags: Seq[TagModel])

  case class ResultsInThemeState(listProposals: Seq[Proposal],
                                 selectedTags: Seq[TagModel],
                                 initialLoad: Boolean,
                                 hasRequestedMore: Boolean,
                                 hasMore: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ResultsInThemeProps, ResultsInThemeState](
      displayName = "ResultsInTheme",
      getInitialState = { self =>
        ResultsInThemeState(
          selectedTags = self.props.wrapped.preselectedTags,
          listProposals = Seq(),
          initialLoad = true,
          hasRequestedMore = false,
          hasMore = false
        )
      },
      componentWillReceiveProps = { (self, nextProps) =>
        self.setState(
          ResultsInThemeState(
            selectedTags = nextProps.wrapped.preselectedTags,
            listProposals = Seq(),
            initialLoad = true,
            hasRequestedMore = false,
            hasMore = false
          )
        )
      },
      render = { (self) =>
        val onSeeMore: (Int) => Unit = {
          _ =>
            if (!self.state.initialLoad) {
              self.setState(_.copy(hasRequestedMore = true))
            }
            self.props.wrapped.onMoreResultsRequested(self.state.listProposals, self.state.selectedTags).onComplete {
              case Success(searchResult) =>
                self.setState(
                  _.copy(
                    listProposals = searchResult.results,
                    hasMore = searchResult.total > searchResult.results.size,
                    initialLoad = false
                  )
                )
              case Failure(_) => // Let parent handle logging error
            }
        }

        val noResults: ReactElement =
          <.div(^.className := Seq(ColRulesStyles.col, ResultsInThemeStyles.noResults))(
            <.p(^.className := ResultsInThemeStyles.noResultsSmiley)("😞"),
            <.p(
              ^.className := Seq(TextStyles.mediumText, ResultsInThemeStyles.noResultsMessage),
              ^.dangerouslySetInnerHTML := I18n.t("theme.results.no-results")
            )()
          )

        val onTagsChange: (Seq[TagModel]) => Unit = { tags =>
          self.setState(_.copy(selectedTags = tags))
          self.props.wrapped.onTagSelectionChange(tags).onComplete {
            case Success(searchResult) =>
              self.setState(
                _.copy(listProposals = searchResult.results, hasMore = searchResult.total > searchResult.results.size)
              )
            case Failure(_) => // Let parent handle logging error
          }
        }

        def proposals(proposals: Seq[Proposal]) =
          Seq(
            <.InfiniteScroll(
              ^.element := "ul",
              ^.className := ResultsInThemeStyles.itemsList,
              ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
              ^.initialLoad := true,
              ^.pageStart := 1,
              ^.loadMore := onSeeMore,
              ^.loader := <.li(^.className := ResultsInThemeStyles.spinnerWrapper)(<.SpinnerComponent.empty)
            )(if (proposals.nonEmpty) {
              val counter = new Counter()
              proposals.map(
                proposal =>
                  <.li(
                    ^.className := Seq(
                      ResultsInThemeStyles.item,
                      ColRulesStyles.col,
                      ColRulesStyles.colHalfBeyondMedium,
                      ColRulesStyles.colQuarterBeyondLarge
                    )
                  )(
                    <.ProposalTileWithTagsComponent(
                      ^.wrapped := ProposalTileWithTagsProps(proposal = proposal, index = counter.getAndIncrement())
                    )()
                )
              )
            } else { <.span.empty }),
            if (self.state.hasMore && !self.state.hasRequestedMore) {
              <.div(^.className := Seq(ResultsInThemeStyles.seeMoreButtonWrapper, ColRulesStyles.col))(
                <.button(
                  ^.onClick := (() => { onSeeMore(1) }),
                  ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton)
                )(unescape(I18n.t("theme.results.see-more")))
              )
            }
          )

        val proposalsToDisplay: Seq[Proposal] = self.state.listProposals

        <.section(^.className := Seq(RowRulesStyles.centeredRow, ResultsInThemeStyles.wrapper))(
          <.header(^.className := ColRulesStyles.col)(
            <.h2(^.className := TextStyles.bigTitle)(unescape(I18n.t("theme.results.title")))
          ),
          <.nav(^.className := ColRulesStyles.col)(
            <.FilterByTagsComponent(^.wrapped := FilterByTagsProps(self.props.wrapped.theme.tags, onTagsChange))()
          ),
          if (self.state.initialLoad || proposalsToDisplay.nonEmpty) {
            proposals(proposalsToDisplay)
          } else {
            noResults
          },
          <.style()(ResultsInThemeStyles.render[String])
        )
      }
    )
}

object ResultsInThemeStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()))
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
