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

package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.core.Counter
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalTileWithOrganisationsVotes.ProposalTileWithOrganisationsVotesProps
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
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.proposal.{ProposalService, SearchResult}
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.front.Main.CssSettings._
import org.make.front.components.proposal.ProposalTileWithVideo.ProposalTileWithVideoProps
import org.make.front.styles.ThemeStyles
import org.make.front.styles.ui.{AnimationsStyles, CTAStyles}
import org.make.front.styles.utils._
import org.make.front.facades.ReactCSSTransition.{
  ReactCSSTransitionDOMAttributes,
  ReactCSSTransitionVirtualDOMElements,
  TransitionClasses
}
import org.make.front.facades.ReactTransition.ReactTransitionDOMAttributes

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
                                        maybeSeed: Option[Int] = None,
                                        proposalWithVideo: Option[ProposalModel])

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
          hasMore = false,
          proposalWithVideo = None
        )
      },
      componentWillReceiveProps = { (self, nextProps) =>
        self.setState(
          _.copy(
            selectedTags = nextProps.wrapped.preselectedTags,
            listProposals = js.Array(),
            initialLoad = true,
            hasRequestedMore = false,
            hasMore = false
          )
        )
        ProposalService
          .searchProposals(
            slug = Some(
              "il-faut-soutenir-les-festivals-pour-leur-permettre-d-etre-itinerants-afin-de-se-deployer-sur-le-territoire"
            )
          )
          .onComplete {
            case Success(SearchResult(_, proposals, _)) =>
              self.setState(_.copy(proposalWithVideo = proposals.toSeq.headOption))
            case Failure(_) =>
          }
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
          <.div(^.className := js.Array(ResultsInConsultationStyles.noResults, LayoutRulesStyles.centeredRow))(
            <.p(^.className := ResultsInConsultationStyles.noResultsSmiley)("😞"),
            <.p(
              ^.className := js.Array(TextStyles.mediumText, ResultsInConsultationStyles.noResultsMessage),
              ^.dangerouslySetInnerHTML := I18n.t("operation.results.no-results")
            )()
          )

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
                eventName = "click-tag-action",
                trackingContext = TrackingContext(
                  TrackingLocation.operationPage,
                  operationSlug = Some(self.props.wrapped.operation.slug)
                ),
                parameters = Map("nature" -> action, "tag-name" -> tag.label)
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

        def proposals(proposals: js.Array[ProposalModel], country: String) = {
          val counter = new Counter()
          js.Array(
              <.InfiniteScroll(
                ^.element := "ul",
                ^.hasMore := (self.state.initialLoad || self.state.hasMore && self.state.hasRequestedMore),
                ^.initialLoad := false,
                ^.loadMore := onSeeMore,
                ^.loader := <.li()(<.SpinnerComponent.empty)
              )(self.state.proposalWithVideo.map {
                proposal =>
                  <.li()(
                    <.ProposalTileWithVideoComponent(
                      ^.wrapped := ProposalTileWithVideoProps(
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
              }, if (proposals.nonEmpty) {
                proposals
                  .map(
                    proposal =>
                      <.li()(
                        <.ProposalTileWithOrganisationsVotesComponent(
                          ^.wrapped := ProposalTileWithOrganisationsVotesProps(
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
              } else {
                <.div.empty
              }),
              if (self.state.hasMore && !self.state.hasRequestedMore) {
                <.button(
                  ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA, ResultsInConsultationStyles.moreResults),
                  ^.onClick := (() => {
                    onSeeMore(1)
                    TrackingService.track(
                      eventName = "click-proposal-viewmore",
                      trackingContext = TrackingContext(
                        TrackingLocation.operationPage,
                        operationSlug = Some(self.props.wrapped.operation.slug)
                      )
                    )
                  })
                )(unescape(I18n.t("operation.results.see-more")))
              }
            )
            .toSeq
        }

        val proposalsToDisplay: js.Array[ProposalModel] = self.state.listProposals

        <.div()(
          <.header(^.className := ResultsInConsultationStyles.wrapper)(
            <.h2(^.className := ResultsInConsultationStyles.title)(
              <.i(^.className := js.Array(FontAwesomeStyles.thumbsUp, ResultsInConsultationStyles.icon))(),
              unescape(I18n.t("operation.results.title"))
            ),
            <.div(
              ^.className := js.Array(ResultsInConsultationStyles.wrapper, ResultsInConsultationStyles.tagsContainer)
            )(
              <.FilterByTagsComponent(
                ^.wrapped := FilterByTagsProps(
                  tags = self.props.wrapped.operation.tags,
                  selectedTags = self.state.selectedTags,
                  onTagSelectionChange = onTagsChange
                )
              )()
            )
          ),
          if (self.state.initialLoad || proposalsToDisplay.nonEmpty) {
            <.CSSTransition(
              ^.timeout := 250,
              ^.in := proposalsToDisplay.nonEmpty,
              ^.classNamesMap := TransitionClasses(
                enter = AnimationsStyles.fadeOn250.htmlClass,
                enterDone = AnimationsStyles.showChildren.htmlClass
              )
            )(<.div()(proposals(proposalsToDisplay, self.props.wrapped.country)))
          } else {
            noResults
          },
          <.style()(ResultsInConsultationStyles.render[String])
        )
      }
    )
  }
}

object ResultsInConsultationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      marginTop(40.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingRight(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(paddingLeft(`0`), paddingRight(`0`))
    )

  val tagsContainer: StyleA =
    style(
      paddingBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      borderBottom(2.pxToEm(), solid, ThemeStyles.BorderColor.lighter)
    )

  val title: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()))
    )

  val icon: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val moreResults: StyleA =
    style(display.block, margin(`0`, auto, ThemeStyles.SpacingValue.small.pxToEm()))

  val noResults: StyleA = style(
    paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
    paddingBottom((ThemeStyles.SpacingValue.small).pxToEm()),
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
