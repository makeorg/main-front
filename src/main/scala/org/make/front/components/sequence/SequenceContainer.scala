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

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.Failure
import scala.scalajs.js.JSConverters._

object SequenceContainer {

  final case class SequenceContainerProps(loadSequence: (js.Array[ProposalId]) => Future[SequenceModel],
                                          sequence: SequenceModel,
                                          progressBarColor: Option[String],
                                          extraSlides: js.Array[ExtraSlide],
                                          maybeTheme: Option[TranslatedThemeModel],
                                          maybeOperation: Option[OperationModel],
                                          maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Sequence.reactClass)

  def sortProposals(proposals: js.Array[ProposalModel]): js.Array[ProposalModel] = {
    scala.util.Random.shuffle(proposals.toSeq).toJSArray
  }

  def selectorFactory: (Dispatch) => (AppState, Props[SequenceContainerProps]) => Sequence.SequenceProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[SequenceContainerProps]) =>
      {
        val isConnected: Boolean = state.connectedUser.nonEmpty

        val reloadSequence: (js.Array[ProposalId]) => Future[SequenceModel] = { forcedProposals =>
          val result = props.wrapped.loadSequence(forcedProposals).map { sequence =>
            val proposals = sequence.proposals
            val voted = proposals.filter(_.votes.exists(_.hasVoted))
            val notVoted = proposals.filter(_.votes.forall(!_.hasVoted))
            sequence.copy(proposals = voted ++ sortProposals(notVoted))
          }

          result.onComplete {
            case Failure(_) => dispatch(NotifyError(I18n.t("error-message.main")))
            case _          =>
          }
          result
        }

        Sequence.SequenceProps(
          loadSequence = reloadSequence,
          sequence = props.wrapped.sequence,
          progressBarColor = props.wrapped.progressBarColor,
          extraSlides = props.wrapped.extraSlides,
          isConnected = isConnected,
          maybeTheme = props.wrapped.maybeTheme,
          maybeOperation = props.wrapped.maybeOperation,
          maybeLocation = props.wrapped.maybeLocation
        )
      }
    }
}
