package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.operation.OperationHeader.OperationHeaderProps
import org.make.front.components.operation.OperationIntro.OperationIntroProps
import org.make.front.components.operation.ResultsInOperationContainer.ResultsInOperationContainerProps
import org.make.front.models.{Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object Operation {

  final case class OperationProps(operation: OperationModel, language: String, onWillMount: () => Unit)

  lazy val reactClass: ReactClass =
    React
      .createClass[OperationProps, Unit](
        displayName = "Operation",
        componentWillMount = { self =>
          self.props.wrapped.onWillMount()
        },
        componentDidMount = { self =>
          TrackingService
            .track(
              "display-page-operation",
              TrackingContext(TrackingLocation.operationPage, operationSlug = Some(self.props.wrapped.operation.slug)),
              Map("id" -> self.props.wrapped.operation.operationId.value)
            )
        },
        render = (self) => {
          if (self.props.wrapped.operation.isActive) {
            <("operation")()(
              <.div(^.className := OperationComponentStyles.mainHeaderWrapper)(<.MainHeaderContainer.empty),
              if (self.props.wrapped.operation.logoUrl.nonEmpty) {
                <.OperationIntroComponent(
                  ^.wrapped := OperationIntroProps(
                    operation = self.props.wrapped.operation,
                    language = self.props.wrapped.language
                  )
                )()
              },
              <.OperationHeaderComponent(
                ^.wrapped := OperationHeaderProps(
                  self.props.wrapped.operation,
                  maybeLocation = Some(LocationModel.OperationPage(self.props.wrapped.operation.operationId)),
                  language = self.props.wrapped.language
                )
              )(),
              <.div(^.className := OperationComponentStyles.contentWrapper)(
                <.ResultsInOperationContainerComponent(
                  ^.wrapped := ResultsInOperationContainerProps(
                    currentOperation = self.props.wrapped.operation,
                    maybeLocation = Some(LocationModel.OperationPage(self.props.wrapped.operation.operationId))
                  )
                )()
              ),
              <.style()(OperationComponentStyles.render[String])
            )
          } else {
            <("operation")()()
          }
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
