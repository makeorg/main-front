package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.models.{Operation => OperationModel}

object PromptingToContinueAfterTheSequence {

  final case class PromptingToContinueAfterTheSequenceProps(operation: OperationModel, clickOnButtonHandler: () => Unit)

  final case class PromptingToContinueAfterTheSequenceState()

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToContinueAfterTheSequenceProps, PromptingToContinueAfterTheSequenceState](
        displayName = "PromptingToContinueAfterTheSequence",
        getInitialState = { _ =>
          PromptingToContinueAfterTheSequenceState()
        },
        render = { self =>
          <.div()(<.style()(PromptingToContinueAfterTheSequenceStyles.render[String]))
        }
      )
}

object PromptingToContinueAfterTheSequenceStyles extends StyleSheet.Inline {}
