package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.sequence.Sequence.ExtraSlide
import org.make.front.facades.I18n
import org.make.front.models.{
  ProposalId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  Sequence          => SequenceModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.services.proposal.ProposalService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure

object SequenceContainer {

  final case class SequenceContainerProps(maybeFirstProposalSlug: Option[String],
                                          sequence: (Seq[ProposalId]) => Future[SequenceModel],
                                          progressBarColor: Option[String],
                                          extraSlides: Seq[ExtraSlide],
                                          maybeTheme: Option[TranslatedThemeModel],
                                          maybeOperation: Option[OperationModel],
                                          maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Sequence.reactClass)

  def sortProposals(proposals: Seq[ProposalModel]): Seq[ProposalModel] = {
    scala.util.Random.shuffle(proposals)
  }

  def selectorFactory: (Dispatch) => (AppState, Props[SequenceContainerProps]) => Sequence.SequenceProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[SequenceContainerProps]) =>
      {
        val shouldReload: Boolean = state.connectedUser.nonEmpty

        val loadSequence: (Seq[ProposalId]) => Future[SequenceModel] = { forcedProposals =>
          props.wrapped.maybeFirstProposalSlug.map { slug =>
            ProposalService.searchProposals(slug = Some(slug)).map { result =>
              result.results
            }
          }.getOrElse {
            Future.successful(Seq.empty[ProposalModel])
          }.flatMap { pushedProposal =>
            val result = props.wrapped.sequence(forcedProposals ++ pushedProposal.map(_.id)).map { sequence =>
              val proposals = sequence.proposals
              val pushedProposalId = pushedProposal.map(_.id)
              val otherProposals = proposals.filter(p => !pushedProposalId.contains(p.id))
              val voted = otherProposals.filter(_.votes.exists(_.hasVoted))
              val notVoted = otherProposals.filter(_.votes.forall(!_.hasVoted))

              sequence.copy(proposals = voted ++ pushedProposal ++ sortProposals(notVoted))
            }

            result.onComplete {
              case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
              case _          =>
            }
            result
          }
        }

        Sequence.SequenceProps(
          sequence = loadSequence,
          progressBarColor = props.wrapped.progressBarColor,
          extraSlides = props.wrapped.extraSlides,
          shouldReload = shouldReload,
          maybeTheme = props.wrapped.maybeTheme,
          maybeOperation = props.wrapped.maybeOperation,
          maybeLocation = props.wrapped.maybeLocation
        )
      }
    }
}
