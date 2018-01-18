package org.make.front.components.operation

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
import scala.util.{Failure, Success}

object ResultsInOperation {

  case class ResultsInOperationProps(
    operation: OperationModel,
    onMoreResultsRequested: (Seq[ProposalModel], Seq[TagModel], Option[Int]) => Future[SearchResult],
    onTagSelectionChange: (Seq[TagModel], Option[Int])                       => Future[SearchResult],
    preselectedTags: Seq[TagModel],
    maybeLocation: Option[LocationModel]
  )

  case class ResultsInOperationState(listProposals: Seq[ProposalModel],
                                     selectedTags: Seq[TagModel],
                                     initialLoad: Boolean,
                                     hasRequestedMore: Boolean,
                                     hasMore: Boolean,
                                     maybeSeed: Option[Int] = None)

  lazy val reactClass: ReactClass = {
    def onSuccessfulVote(proposalId: ProposalIdModel,
                         self: Self[ResultsInOperationProps, ResultsInOperationState]): (VoteModel) => Unit = {
      (voteModel) =>
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
      self: Self[ResultsInOperationProps, ResultsInOperationState]
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

    React.createClass[ResultsInOperationProps, ResultsInOperationState](
      displayName = "ResultsInOperation",
      getInitialState = { self =>
        ResultsInOperationState(
          selectedTags = self.props.wrapped.preselectedTags,
          listProposals = Seq(),
          initialLoad = true,
          hasRequestedMore = false,
          hasMore = false
        )
      },
      componentWillReceiveProps = { (self, nextProps) =>
        self.setState(
          ResultsInOperationState(
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
          <.div(^.className := Seq(ResultsInOperationStyles.noResults, LayoutRulesStyles.centeredRow))(
            <.p(^.className := ResultsInOperationStyles.noResultsSmiley)("😞"),
            <.p(
              ^.className := Seq(TextStyles.mediumText, ResultsInOperationStyles.noResultsMessage),
              ^.dangerouslySetInnerHTML := I18n.t("operation.results.no-results")
            )()
          )

        val onTagsChange: (Seq[TagModel]) => Unit = {
          tags =>
            val previousSelectedTags = self.state.selectedTags
            val changedTags = if (previousSelectedTags.size > tags.size) {
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
                "click-tag-action",
                TrackingContext(
                  TrackingLocation.operationPage,
                  operationSlug = Some(self.props.wrapped.operation.slug)
                ),
                Map("nature" -> action, "tag-name" -> tag.label)
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

        def proposals(proposals: Seq[ProposalModel]) =
          Seq(
            <.InfiniteScroll(
              ^.element := "ul",
              ^.className := Seq(ResultsInOperationStyles.itemsList, LayoutRulesStyles.centeredRowWithCols),
              ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
              ^.initialLoad := false,
              ^.loadMore := onSeeMore,
              ^.loader := <.li(^.className := ResultsInOperationStyles.spinnerWrapper)(<.SpinnerComponent.empty)
            )(if (proposals.nonEmpty) {
              val counter = new Counter()
              proposals.map(
                proposal =>
                  <.li(
                    ^.className := Seq(
                      ResultsInOperationStyles.item,
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
                        trackingLocation = TrackingLocation.operationPage,
                        maybeTheme = None,
                        maybeOperation = Some(self.props.wrapped.operation),
                        maybeSequenceId = None,
                        maybeLocation = self.props.wrapped.maybeLocation
                      )
                    )()
                )
              )
            } else { <.div.empty }),
            if (self.state.hasMore && !self.state.hasRequestedMore) {
              <.div(^.className := Seq(ResultsInOperationStyles.seeMoreButtonWrapper, LayoutRulesStyles.centeredRow))(
                <.button(^.onClick := (() => {
                  onSeeMore(1)
                  TrackingService.track(
                    "click-proposal-viewmore",
                    TrackingContext(
                      TrackingLocation.operationPage,
                      operationSlug = Some(self.props.wrapped.operation.slug)
                    )
                  )
                }), ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                  unescape(I18n.t("operation.results.see-more"))
                )
              )
            }
          )

        val proposalsToDisplay: Seq[ProposalModel] = self.state.listProposals

        <.section(^.className := ResultsInOperationStyles.wrapper)(
          <.header(^.className := LayoutRulesStyles.centeredRow)(
            <.h2(^.className := TextStyles.mediumTitle)(unescape(I18n.t("operation.results.title")))
          ),
          <.div(^.className := Seq(ResultsInOperationStyles.tagsNavWrapper, LayoutRulesStyles.centeredRow))(
            <.FilterByTagsComponent(^.wrapped := FilterByTagsProps(self.props.wrapped.operation.tags, onTagsChange))()
          ),
          if (self.state.initialLoad || proposalsToDisplay.nonEmpty) {
            proposals(proposalsToDisplay)
          } else {
            noResults
          },
          <.style()(ResultsInOperationStyles.render[String])
        )
      }
    )
  }
}

object ResultsInOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(ThemeStyles.SpacingValue.large.pxToEm())),
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
