package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.politicalActions.PoliticalActionsContainer.PoliticalActionsContainerProps
import org.make.front.components.theme.ResultsInThemeContainer.ResultsInThemeContainerProps
import org.make.front.components.theme.ThemeHeader.ThemeHeaderProps
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._

object Theme {

  final case class ThemeProps(theme: TranslatedThemeModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[ThemeProps, Unit](
        displayName = "Theme",
        render = (self) => {
          <.div()(
            <.div(^.className := ThemeComponentStyles.mainHeaderWrapper)(<.MainHeaderComponent.empty),
            <.ThemeHeaderComponent(^.wrapped := ThemeHeaderProps(self.props.wrapped.theme))(),
            <.div(^.className := ThemeComponentStyles.contentWrapper)(
              <.PoliticalActionsContainerComponent(
                ^.wrapped := PoliticalActionsContainerProps(Some(self.props.wrapped.theme))
              )(),
              <.ResultsInThemeContainerComponent(
                ^.wrapped := ResultsInThemeContainerProps(currentTheme = self.props.wrapped.theme)
              )(),
              <.NavInThemesContainerComponent.empty
            ),
            <.style()(ThemeComponentStyles.render[String])
          )
        }
      )
}

object ThemeComponentStyles extends StyleSheet.Inline {

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
