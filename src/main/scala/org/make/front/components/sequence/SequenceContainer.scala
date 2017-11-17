package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.sequence.Sequence.ExtraSlide
import org.make.front.models.{Operation => OperationModel, Proposal => ProposalModel, Sequence => SequenceModel}
import org.make.services.proposal.{ContextRequest, ProposalService, SearchResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SequenceContainer {

  final case class SequenceContainerProps(maybeFirstProposalSlug: Option[String],
                                          sequence: SequenceModel,
                                          progressBarColor: Option[String],
                                          extraSlides: Seq[ExtraSlide])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Sequence.reactClass)

  def sortProposals(proposals: Seq[ProposalModel]): Seq[ProposalModel] = {
    scala.util.Random.shuffle(proposals)
  }

  def selectorFactory: (Dispatch) => (AppState, Props[SequenceContainerProps]) => Sequence.SequenceProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[SequenceContainerProps]) =>
      {
        val proposals: () => Future[Seq[ProposalModel]] = () => {

          val vffOperation: OperationModel = state.operations.filter(_.operationId.value == "vff").head
          val proposalsResponse: Future[SearchResult] =
            ProposalService.searchProposals(
              context = Some(ContextRequest(operation = Some(vffOperation.label))),
              limit = Some(12)
            )

          proposalsResponse.recover {
            case e => dispatch(NotifyError(e.getMessage))
          }

          proposalsResponse.map(_.results).flatMap { proposals =>
            val voted = proposals.filter(_.votes.exists(_.hasVoted))
            val notVoted = proposals.filter(_.votes.forall(!_.hasVoted))
            val sortedUnvotedProposals = sortProposals(notVoted)

            props.wrapped.maybeFirstProposalSlug.map { slug =>
              if (proposals.exists(_.slug == slug)) {
                val reOrderedUnvoted = if (sortedUnvotedProposals.exists(_.slug == slug)) {
                  sortedUnvotedProposals.filter(_.slug == slug) ++ sortedUnvotedProposals.filter(_.slug != slug)
                } else {
                  sortedUnvotedProposals
                }
                Future.successful(voted ++ reOrderedUnvoted)
              } else {
                ProposalService.searchProposals(slug = Some(slug)).map { searchResult =>
                  voted ++ searchResult.results.take(1) ++ sortedUnvotedProposals
                }
              }

            }.getOrElse(Future.successful(voted ++ sortedUnvotedProposals))

          }
        }

        val shouldReload: Boolean = state.connectedUser.nonEmpty

        Sequence.SequenceProps(
          sequence = props.wrapped.sequence,
          progressBarColor = props.wrapped.progressBarColor,
          proposals = proposals,
          extraSlides = props.wrapped.extraSlides,
          shouldReload = shouldReload
        )
      }
    }
}
