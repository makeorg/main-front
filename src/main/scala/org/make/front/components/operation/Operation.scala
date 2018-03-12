package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.operation.OperationHeader.OperationHeaderProps
import org.make.front.components.operation.ResultsInOperationContainer.ResultsInOperationContainerProps
import org.make.front.components.operation.intro.ClimatParisOperationIntro.ClimatParisOperationIntroProps
import org.make.front.components.operation.intro.LPAEOperationIntro.LPAEOperationIntroProps
import org.make.front.components.operation.intro.MVEOperationIntro.MVEOperationIntroProps
import org.make.front.components.operation.intro.VFFOperationIntro.VFFOperationIntroProps
import org.make.front.components.operation.intro.VFFGBOperationIntro.VFFGBOperationIntroProps
import org.make.front.components.operation.intro.VFFITOperationIntro.VFFITOperationIntroProps
import org.make.front.models.{Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object Operation {

  final case class OperationProps(operation: OperationModel,
                                  countryCode: String,
                                  language: String,
                                  onWillMount: () => Unit)

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
              // @todo: refactor this part
              if (self.props.wrapped.operation.slug == "climatparis") {
                <.ClimatParisOperationIntroComponent(
                  ^.wrapped := ClimatParisOperationIntroProps(
                    operation = self.props.wrapped.operation,
                    language = self.props.wrapped.language
                  )
                )()
              },
              if (self.props.wrapped.operation.slug == "lpae") {
                <.LPAEOperationIntroComponent(
                  ^.wrapped := LPAEOperationIntroProps(
                    operation = self.props.wrapped.operation,
                    language = self.props.wrapped.language
                  )
                )()
              },
              if (self.props.wrapped.operation.slug == "mieux-vivre-ensemble") {
                <.MVEOperationIntroComponent(
                  ^.wrapped := MVEOperationIntroProps(
                    operation = self.props.wrapped.operation,
                    language = self.props.wrapped.language
                  )
                )()
              },
              if (self.props.wrapped.operation.slug == "vff") {
                if (self.props.wrapped.countryCode == "IT") {
                  <.VFFITOperationIntroComponent(
                    ^.wrapped := VFFITOperationIntroProps(
                      operation = self.props.wrapped.operation,
                      language = self.props.wrapped.language
                    )
                  )()
                } else if (self.props.wrapped.countryCode == "GB") {
                  <.VFFGBOperationIntroComponent(
                    ^.wrapped := VFFGBOperationIntroProps(
                      operation = self.props.wrapped.operation,
                      language = self.props.wrapped.language
                    )
                  )()
                } else {
                  <.VFFOperationIntroComponent(
                    ^.wrapped := VFFOperationIntroProps(
                      operation = self.props.wrapped.operation,
                      language = self.props.wrapped.language
                    )
                  )()
                }
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
