package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.sequence.SequenceContainer.SequenceContainerProps
import org.make.front.models.{Operation => OperationModel, Sequence => SequenceModel}

object OperationSSequence {
  final case class OperationSSequenceProps(operation: OperationModel, sequence: SequenceModel)

  lazy val reactClass: ReactClass =
    React.createClass[OperationSSequenceProps, Unit](
      getInitialState = { self =>
        },
      render = { self =>
        <.div()(
          <.p()("operation's sequence"),
          <.SequenceContainerComponent(^.wrapped := SequenceContainerProps(self.props.wrapped.sequence))()
        )
      }
    )
}
