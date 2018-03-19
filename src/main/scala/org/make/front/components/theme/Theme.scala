package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.politicalActions.PoliticalActionsContainer.PoliticalActionsContainerProps
import org.make.front.components.theme.ResultsInThemeContainer.ResultsInThemeContainerProps
import org.make.front.components.theme.ThemeHeader.ThemeHeaderProps
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

object Theme {

  final case class ThemeProps(theme: TranslatedThemeModel,
                              maybeOperation: Option[OperationModel],
                              maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[ThemeProps, Unit](
        displayName = "Theme",
        componentDidMount = { self =>
          TrackingService
            .track(
              "display-page-theme",
              TrackingContext(TrackingLocation.themePage),
              Map("themeId" -> self.props.wrapped.theme.id.value)
            )
        },
        render = (self) => {
          <.div()(
            <.div(^.className := ThemePageStyles.mainHeaderWrapper)(<.MainHeaderContainer.empty),
            <.ThemeHeaderComponent(^.wrapped := ThemeHeaderProps(self.props.wrapped.theme))(),
            <.div(^.className := ThemePageStyles.contentWrapper)(
              <.PoliticalActionsContainerComponent(
                ^.wrapped := PoliticalActionsContainerProps(Some(self.props.wrapped.theme))
              )(),
              <.ResultsInThemeContainerComponent(
                ^.wrapped := ResultsInThemeContainerProps(
                  currentTheme = self.props.wrapped.theme,
                  maybeOperation = self.props.wrapped.maybeOperation,
                  maybeLocation = self.props.wrapped.maybeLocation
                )
              )(),
              <.NavInThemesContainerComponent.empty
            ),
            <.style()(ThemePageStyles.render[String])
          )
        }
      )
}

object ThemePageStyles extends StyleSheet.Inline {

  import dsl._

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)

  val contentWrapper: StyleA =
    style(
      display.block,
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(ThemeStyles.SpacingValue.medium.pxToEm())),
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )
}
