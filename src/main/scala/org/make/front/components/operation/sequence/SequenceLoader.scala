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

package org.make.front.components.operation.sequence
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components.RichVirtualDOMElements
import org.make.front.components.operation.sequence.SequenceOfTheOperation.SequenceOfTheOperationProps
import org.make.front.models.{ProposalId, SequenceId, OperationExpanded => OperationModel, Sequence => SequenceModel}
import org.scalajs.dom
import io.github.shogowada.scalajs.reactjs.router.RouterProps._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.Success

object SequenceLoader {

  final case class SequenceLoaderProps(loadSequenceAndOperation: () => Future[Option[(OperationModel, SequenceModel)]],
                                       operationSlug: String,
                                       country: String,
                                       language: String,
                                       redirectHome: () => Unit,
                                       isConnected: Boolean,
                                       startSequence: (SequenceId) => (js.Array[ProposalId]) => Future[SequenceModel])

  final case class SequenceLoaderState(operation: Option[OperationModel],
                                       sequence: Option[SequenceModel],
                                       country: String,
                                       canUpdate: Boolean)

  val reactClass: ReactClass =
    React.createClass[SequenceLoaderProps, SequenceLoaderState](
      displayName = "SequenceLoader",
      getInitialState = { self =>
        SequenceLoaderState(None, None, self.props.wrapped.country, true)
      },
      componentWillReceiveProps = {
        (self, props) =>
          if (self.state.operation.isEmpty || self.state.operation.exists(
                operation =>
                  operation.slug != props.wrapped.operationSlug ||
                    operation.country != props.wrapped.country
              )) {
            self.setState(_.copy(country = props.wrapped.country, operation = None, sequence = None))
            props.wrapped.loadSequenceAndOperation().onComplete {
              case Success(Some((operation, sequence))) =>
                if (operation.isExpired) {
                  dom.window.location
                    .assign(
                      operation.getWordingByLanguage(props.wrapped.language).flatMap(_.learnMoreUrl).getOrElse("/")
                    )
                } else if (!operation.isActive) {
                  props.history.push("/404")
                } else if (operation.country == self.state.country) {
                  self.setState(_.copy(operation = Some(operation), sequence = Some(sequence)))
                }
              case Success(None) =>
                if (self.state.country == props.wrapped.country) {
                  props.history.push("/404")
                }
              case _ =>
            }
          }
      },
      shouldComponentUpdate = { (self, props, state) =>
        state.canUpdate
      },
      render = { self =>
        def handleCanUpdate(canUpdate: Boolean): Unit = {
          self.setState(state => state.copy(canUpdate = canUpdate))
        }

        (for {
          operation <- self.state.operation
          sequence  <- self.state.sequence
        } yield
          <.SequenceOfTheOperationComponent(
            ^.wrapped := SequenceOfTheOperationProps(
              isConnected = self.props.wrapped.isConnected,
              operation = operation,
              startSequence = self.props.wrapped.startSequence(sequence.sequenceId),
              redirectHome = self.props.wrapped.redirectHome,
              sequence = sequence,
              language = self.props.wrapped.language,
              country = self.props.wrapped.country,
              handleCanUpdate = handleCanUpdate
            )
          )()).getOrElse(<.WaitingForSequence.empty)
      }
    )
}
