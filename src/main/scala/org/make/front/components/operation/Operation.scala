package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.operation.OperationHeader.OperationHeaderProps
import org.make.front.components.operation.ResultsInOperationContainer.ResultsInOperationContainerProps
import org.make.front.components.operation.VFFIntro.VFFIntroProps
import org.make.front.facades.FacebookPixel
import org.make.front.models.{
  Location        => LocationModel,
  TranslatedTheme => TranslatedThemeModel,
  Operation       => OperationModel,
  Sequence        => SequenceModel
}
import org.make.front.styles.ThemeStyles

import scala.scalajs.js
import org.make.front.models.{Operation => OperationModel}

object Operation {

  final case class OperationProps(operation: OperationModel,
                                  maybeTheme: Option[TranslatedThemeModel],
                                  maybeSequence: Option[SequenceModel],
                                  maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[OperationProps, Unit](
        displayName = "Operation",
        componentDidMount = { self =>
          FacebookPixel
            .fbq(
              "trackCustom",
              "display-page-operation",
              js.Dictionary("id" -> self.props.wrapped.operation.operationId.value)
            )
        },
        render = (self) => {
          <("operation")()(
            <.div(^.className := OperationComponentStyles.mainHeaderWrapper)(<.MainHeaderComponent.empty),
            <.VFFIntroComponent(^.wrapped := VFFIntroProps(operation = self.props.wrapped.operation))(),
            <.OperationHeaderComponent(
              ^.wrapped := OperationHeaderProps(
                self.props.wrapped.operation,
                maybeSequence = self.props.wrapped.maybeSequence,
                maybeLocation = self.props.wrapped.maybeLocation
              )
            )(),
            <.div(^.className := OperationComponentStyles.contentWrapper)(
              <.ResultsInOperationContainerComponent(
                ^.wrapped := ResultsInOperationContainerProps(
                  currentOperation = self.props.wrapped.operation,
                  maybeTheme = self.props.wrapped.maybeTheme,
                  maybeSequence = self.props.wrapped.maybeSequence,
                  maybeLocation = self.props.wrapped.maybeLocation
                )
              )()
            ),
            <.style()(OperationComponentStyles.render[String])
          )
        }
      )
}

object OperationComponentStyles extends StyleSheet.Inline {

  import dsl._

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)

  val contentWrapper: StyleA =
    style(display.block, backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

}
