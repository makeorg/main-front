package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalWithTags.ProposalWithTagsProps
import org.make.front.components.tags.FilterByTags.FilterByTagsProps
import org.make.front.facades.I18n
import org.make.front.facades.ReactInfiniteScroller.{
  ReactInfiniteScrollerVirtualDOMAttributes,
  ReactInfiniteScrollerVirtualDOMElements
}
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{ProposalSearchResult, Proposal => ProposalModel, Tag => TagModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.internal.mutable.StyleSheet

object ResultsInTheme {

  case class ResultsInThemeProps(
    onMoreResultsRequested: (Seq[ProposalModel], Seq[TagModel]) => Future[ProposalSearchResult],
    onTagSelectionChange: (Seq[TagModel])                       => Future[ProposalSearchResult],
    proposals: Future[ProposalSearchResult],
    preselectedTags: Seq[TagModel]
  )

  case class ResultsInThemeState(listProposals: Seq[ProposalModel],
                                 selectedTags: Seq[TagModel],
                                 initialLoad: Boolean,
                                 hasRequestedMore: Boolean,
                                 hasMore: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ResultsInThemeProps, ResultsInThemeState](
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
                  _.copy(listProposals = searchResult.proposals, hasMore = searchResult.hasMore, initialLoad = false)
                )
              case Failure(_) => // Let parent handle logging error
            }
        }

        val noResults: ReactElement =
          <.div(^.className := Seq(LayoutRulesStyles.col, ResultsInThemeStyles.noResults))(
            <.p(^.className := ResultsInThemeStyles.noResultsSmiley)("😞"),
            <.p(
              ^.className := Seq(TextStyles.mediumText, ResultsInThemeStyles.noResultsMessage),
              ^.dangerouslySetInnerHTML := I18n.t("content.theme.matrix.noContent")
            )()
          )

        val onTagsChange: (Seq[TagModel]) => Unit = { tags =>
          self.setState(_.copy(selectedTags = tags))
          self.props.wrapped.onTagSelectionChange(tags).onComplete {
            case Success(searchResult) =>
              self.setState(_.copy(listProposals = searchResult.proposals, hasMore = searchResult.hasMore))
            case Failure(_) => // Let parent handle logging error
          }
        }

        def proposals(proposals: Seq[ProposalModel]) =
          Seq(
            <.InfiniteScroll(
              ^.element := "ul",
              ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
              ^.initialLoad := true,
              ^.pageStart := 1,
              ^.loadMore := onSeeMore,
              ^.loader := <.SpinnerComponent.empty
            )(if (proposals.isEmpty) {
              <.p.empty
            } else {
              proposals.map(
                proposal =>
                  <.li(
                    ^.className := Seq(
                      ResultsInThemeStyles.item,
                      LayoutRulesStyles.col,
                      LayoutRulesStyles.colHalfBeyondMedium,
                      LayoutRulesStyles.colQuarterBeyondLarge
                    )
                  )(<.ProposalWithTagsComponent(^.wrapped := ProposalWithTagsProps(proposal = proposal))())
              )
            }),
            if (self.state.hasMore && !self.state.hasRequestedMore) {
              <.div(^.className := Seq(ResultsInThemeStyles.seeMoreButtonWrapper, LayoutRulesStyles.col))(
                <.button(
                  ^.onClick := (() => { onSeeMore(1) }),
                  ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton)
                )(unescape(I18n.t("content.theme.seeMoreProposals")))
              )
            }
          )

        val proposalsToDisplay: Seq[ProposalModel] = self.state.listProposals

        <.section(^.className := Seq(LayoutRulesStyles.centeredRow, ResultsInThemeStyles.wrapper))(
          <.header(^.className := LayoutRulesStyles.col)(
            <.h2(^.className := TextStyles.bigTitle)(unescape(I18n.t("content.theme.matrix.title")))
          ),
          if (self.props.wrapped.preselectedTags.nonEmpty) {
            <.nav(^.className := LayoutRulesStyles.col)(
              <.FilterByTagsComponent(
                ^.wrapped := FilterByTagsProps(self.props.wrapped.preselectedTags, onTagsChange)
              )()
            )
          },
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
    style(paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()), paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val item: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val seeMoreButtonWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
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
