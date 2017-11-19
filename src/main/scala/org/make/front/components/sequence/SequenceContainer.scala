package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.components.sequence.Sequence.ExtraSlide
import org.make.front.models.{Proposal => ProposalModel, Sequence => SequenceModel}

import scala.concurrent.Future

object SequenceContainer {

  final case class SequenceContainerProps(maybeFirstProposalSlug: Option[String],
                                          sequence: Future[SequenceModel],
                                          progressBarColor: Option[String],
                                          extraSlides: Seq[ExtraSlide])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Sequence.reactClass)

  def sortProposals(proposals: Seq[ProposalModel]): Seq[ProposalModel] = {
    scala.util.Random.shuffle(proposals)
  }

  def selectorFactory: (Dispatch) => (AppState, Props[SequenceContainerProps]) => Sequence.SequenceProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[SequenceContainerProps]) =>
      {
        val shouldReload: Boolean = state.connectedUser.nonEmpty

        Sequence.SequenceProps(
          sequence = props.wrapped.sequence,
          progressBarColor = props.wrapped.progressBarColor,
          extraSlides = props.wrapped.extraSlides,
          shouldReload = shouldReload
        )
      }
    }
}
