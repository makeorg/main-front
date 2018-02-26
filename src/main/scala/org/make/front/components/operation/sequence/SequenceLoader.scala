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
import scala.util.Success

object SequenceLoader {

  final case class SequenceLoaderProps(loadSequenceAndOperation: () => Future[Option[(OperationModel, SequenceModel)]],
                                       operationSlug: String,
                                       country: String,
                                       language: String,
                                       redirectHome: () => Unit,
                                       isConnected: Boolean,
                                       startSequence: (SequenceId) => (Seq[ProposalId]) => Future[SequenceModel])

  final case class SequenceLoaderState(operation: Option[OperationModel],
                                       sequence: Option[SequenceModel],
                                       country: String)

  val reactClass: ReactClass =
    React.createClass[SequenceLoaderProps, SequenceLoaderState](
      displayName = "SequenceLoader",
      getInitialState = { self =>
        SequenceLoaderState(None, None, self.props.wrapped.country)
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
      render = { self =>
        <.div()(
          (
            for {
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
                  country = self.props.wrapped.country
                )
              )()
          ).toSeq
        )
      }
    )
}
