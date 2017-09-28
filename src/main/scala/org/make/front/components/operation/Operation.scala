package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.operation.OperationHeader.OperationHeaderProps
import org.make.front.components.operation.ResultsInOperationContainer.ResultsInOperationContainerProps
import org.make.front.models.{Operation => OperationModel}
import org.make.front.styles.ThemeStyles

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object Operation {

  final case class OperationProps(operation: OperationModel)

  lazy val reactClass: ReactClass = React.createClass[OperationProps, Unit](render = (self) => {
    <("operation")()(
      <.OperationHeaderComponent(^.wrapped := OperationHeaderProps(self.props.wrapped.operation))(),
      <.div(^.className := OperationComponentStyles.contentWrapper)(
        <.ResultsInOperationContainerComponent(
          ^.wrapped := ResultsInOperationContainerProps(currentOperation = self.props.wrapped.operation)
        )()
      ),
      <.style()(OperationComponentStyles.render[String])
    )
  })
}

object OperationComponentStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val contentWrapper: StyleA =
    style(display.block, backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

}
