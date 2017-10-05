package org.make.front.components.operation

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
import org.make.front.models.{Proposal => ProposalModel, Tag => TagModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.SearchResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.internal.mutable.StyleSheet

object ResultsInOperation {

  case class ResultsInOperationProps(
    onMoreResultsRequested: (Seq[ProposalModel], Seq[TagModel]) => Future[SearchResponse],
    onTagSelectionChange: (Seq[TagModel])                       => Future[SearchResponse],
    proposals: Future[SearchResponse],
    preselectedTags: Seq[TagModel]
  )

  case class ResultsInOperationState(listProposals: Seq[ProposalModel],
                                     selectedTags: Seq[TagModel],
                                     hasRequestedMore: Boolean,
                                     hasMore: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ResultsInOperationProps, ResultsInOperationState](
      displayName = "ResultsInOperation",
      getInitialState = { self =>
        self.props.wrapped.proposals.onComplete {
          case Success(searchResult) =>
            self.setState(
              _.copy(listProposals = searchResult.results, hasMore = searchResult.total > searchResult.results.size)
            )
          case Failure(_) => // TODO: handle error
        }
        ResultsInOperationState(
          selectedTags = self.props.wrapped.preselectedTags,
          listProposals = Seq(),
          hasRequestedMore = false,
          hasMore = false
        )
      },
      render = { (self) =>
        val onSeeMore: () => Unit =
          () => {
            self.setState(_.copy(hasRequestedMore = true))
            self.props.wrapped.onMoreResultsRequested(self.state.listProposals, self.state.selectedTags).onComplete {
              case Success(searchResult) =>
                self.setState(
                  _.copy(listProposals = searchResult.results, hasMore = searchResult.total > searchResult.results.size)
                )
              case Failure(_) => // TODO: handle error
            }
          }

        val noResults: ReactElement =
          <.div(^.className := Seq(ColRulesStyles.col, ResultsInOperationStyles.noResults))(
            <.p(^.className := ResultsInOperationStyles.noResultsSmiley)("😞"),
            <.p(
              ^.className := Seq(TextStyles.mediumText, ResultsInOperationStyles.noResultsMessage),
              ^.dangerouslySetInnerHTML := I18n.t("content.theme.matrix.noContent")
            )()
          )

        val onTagsChange: (Seq[TagModel]) => Unit = { tags =>
          self.setState(_.copy(selectedTags = tags))
          self.props.wrapped.onTagSelectionChange(tags).onComplete {
            case Success(searchResult) =>
              self.setState(
                _.copy(listProposals = searchResult.results, hasMore = searchResult.total > searchResult.results.size)
              )
            case Failure(_) => // TODO: handle error
          }
        }

        def proposals(proposals: Seq[ProposalModel]) =
          Seq(
            <.InfiniteScroll(
              ^.element := "ul",
              ^.className := ResultsInOperationStyles.itemsList,
              ^.hasMore := (self.state.hasMore && self.state.hasRequestedMore),
              ^.initialLoad := false,
              ^.loadMore := (_ => onSeeMore()),
              ^.loader := <.li(^.className := ResultsInOperationStyles.spinnerWrapper)(<.SpinnerComponent.empty)
            )(
              proposals.map(
                proposal =>
                  <.li(
                    ^.className := Seq(
                      ResultsInOperationStyles.item,
                      ColRulesStyles.col,
                      ColRulesStyles.colHalfBeyondMedium,
                      ColRulesStyles.colQuarterBeyondLarge
                    )
                  )(<.ProposalWithTagsComponent(^.wrapped := ProposalWithTagsProps(proposal = proposal))())
              )
            ),
            if (self.state.hasMore && !self.state.hasRequestedMore) {
              <.div(^.className := Seq(ResultsInOperationStyles.seeMoreButtonWrapper, ColRulesStyles.col))(
                <.button(^.onClick := onSeeMore, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                  unescape(I18n.t("content.theme.matrix.seeMoreProposals"))
                )
              )
            }
          )

        val proposalsToDisplay: Seq[ProposalModel] = self.state.listProposals

        <.section(^.className := Seq(RowRulesStyles.centeredRow, ResultsInOperationStyles.wrapper))(
          <.header(^.className := ColRulesStyles.col)(
            <.h2(^.className := TextStyles.bigTitle)(unescape(I18n.t("content.theme.matrix.title")))
          ),
          if (self.props.wrapped.preselectedTags.nonEmpty) {
            <.nav(^.className := ColRulesStyles.col)(
              <.FilterByTagsComponent(
                ^.wrapped := FilterByTagsProps(self.props.wrapped.preselectedTags, onTagsChange)
              )()
            )
          },
          if (proposalsToDisplay.nonEmpty) {
            proposals(proposalsToDisplay)
          } else {
            noResults
          },
          <.style()(ResultsInOperationStyles.render[String])
        )
      }
    )
}

object ResultsInOperationStyles extends StyleSheet.Inline {

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
      margin := s"${ThemeStyles.SpacingValue.small.pxToEm().value} ${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
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
