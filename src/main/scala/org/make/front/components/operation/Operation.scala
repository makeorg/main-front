package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.operation.OperationHeader.OperationHeaderProps
import org.make.front.components.operation.OperationIntro.OperationIntroProps
import org.make.front.components.operation.ResultsInOperationContainer.ResultsInOperationContainerProps
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.ThemeStyles
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Operation {

  final case class OperationProps(futureMaybeOperation: Future[Option[OperationModel]],
                                  maybeTheme: Option[TranslatedThemeModel],
                                  maybeSequence: Option[SequenceModel],
                                  maybeLocation: Option[LocationModel])

  final case class OperationState(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[OperationProps, OperationState](
        displayName = "Operation",
        componentDidMount = {
          self =>
            self.props.wrapped.futureMaybeOperation.onComplete {
              case Success(maybeOperation) =>
                maybeOperation match {
                  case Some(operation) =>
                    self.setState(_.copy(operation = operation))
                    TrackingService
                      .track(
                        "display-page-operation",
                        TrackingContext(
                          TrackingLocation.operationPage,
                          operationSlug = Some(self.state.operation.slug)
                        ),
                        Map("id" -> self.state.operation.operationId.value)
                      )
                  case _ =>
                    self.setState(_.copy(operation = OperationModel.empty))
                }
              case Failure(_) => // Let parent handle logging error
            }
        },
        getInitialState = { _ =>
          OperationState(OperationModel.empty)
        },
        render = (self) => {
          <("operation")()(
            <.div(^.className := OperationComponentStyles.mainHeaderWrapper)(<.MainHeaderComponent.empty),
            <.OperationIntroComponent(^.wrapped := OperationIntroProps(operation = self.state.operation))(),
            <.OperationHeaderComponent(
              ^.wrapped := OperationHeaderProps(
                self.state.operation,
                maybeSequence = self.props.wrapped.maybeSequence,
                maybeLocation = self.props.wrapped.maybeLocation
              )
            )(),
            <.div(^.className := OperationComponentStyles.contentWrapper)(
              <.ResultsInOperationContainerComponent(
                ^.wrapped := ResultsInOperationContainerProps(
                  currentOperation = self.state.operation,
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
