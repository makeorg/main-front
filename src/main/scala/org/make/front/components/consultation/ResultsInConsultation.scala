package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.core.Counter
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
import org.make.services.proposal.SearchResult
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object ResultsInConsultation {

  case class ResultsInConsultationProps(
    operation: OperationModel,
    onMoreResultsRequested: (js.Array[ProposalModel], js.Array[TagModel], Option[Int]) => Future[SearchResult],
    onTagSelectionChange: (js.Array[TagModel], Option[Int])                            => Future[SearchResult],
    preselectedTags: js.Array[TagModel],
    maybeLocation: Option[LocationModel],
    country: String
  )

  case class ResultsInConsultationState(listProposals: js.Array[ProposalModel],
                                        selectedTags: js.Array[TagModel],
                                        initialLoad: Boolean,
                                        hasRequestedMore: Boolean,
                                        hasMore: Boolean,
                                        maybeSeed: Option[Int] = None)

  lazy val reactClass: ReactClass = {
    def onSuccessfulVote(proposalId: ProposalIdModel,
                         self: Self[ResultsInConsultationProps, ResultsInConsultationState]): VoteModel => Unit = {
      voteModel =>
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
      self: Self[ResultsInConsultationProps, ResultsInConsultationState]
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

    React.createClass[ResultsInConsultationProps, ResultsInConsultationState](
      displayName = "ResultsInConsultation",
      getInitialState = { self =>
        ResultsInConsultationState(
          selectedTags = self.props.wrapped.preselectedTags,
          listProposals = js.Array(),
          initialLoad = true,
          hasRequestedMore = false,
          hasMore = false
        )
      },
      componentWillReceiveProps = { (self, nextProps) =>
        self.setState(
          ResultsInConsultationState(
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
      render = { self =>
        val onSeeMore: Int => Unit = {
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
          <.div()(<.p()("😞"), <.p(^.dangerouslySetInnerHTML := I18n.t("operation.results.no-results"))())

        val onTagsChange: js.Array[TagModel] => Unit = {
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

        def proposals(proposals: js.Array[ProposalModel], country: String) =
          js.Array(
              <.InfiniteScroll(
                ^.element := "ul",
                ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
                ^.initialLoad := false,
                ^.loadMore := onSeeMore,
                ^.loader := <.li()(<.SpinnerComponent.empty)
              )(if (proposals.nonEmpty) {
                val counter = new Counter()
                proposals
                  .map(
                    proposal =>
                      <.li()(
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
                            maybeLocation = self.props.wrapped.maybeLocation,
                            country = country
                          )
                        )()
                    )
                  )
                  .toSeq
              } else { <.div.empty }),
              if (self.state.hasMore && !self.state.hasRequestedMore) {
                <.div()(<.button(^.onClick := (() => {
                  onSeeMore(1)
                  TrackingService.track(
                    "click-proposal-viewmore",
                    TrackingContext(
                      TrackingLocation.operationPage,
                      operationSlug = Some(self.props.wrapped.operation.slug)
                    )
                  )
                }))(unescape(I18n.t("operation.results.see-more"))))
              }
            )
            .toSeq

        val proposalsToDisplay: js.Array[ProposalModel] = self.state.listProposals

        <.section()(
          <.header()(<.h2()(unescape(I18n.t("operation.results.title")))),
          <.div()(
            <.FilterByTagsComponent(^.wrapped := FilterByTagsProps(self.props.wrapped.operation.tagIds, onTagsChange))()
          ),
          if (self.state.initialLoad || proposalsToDisplay.nonEmpty) {
            proposals(proposalsToDisplay, self.props.wrapped.country)
          } else {
            noResults
          }
        )
      }
    )
  }
}
