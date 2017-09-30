package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.theme.ResultsInThemeContainer.ResultsInThemeContainerProps
import org.make.front.components.theme.ThemeHeader.ThemeHeaderProps
import org.make.front.models.{Theme => ThemeModel}
import org.make.front.styles.ThemeStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object Theme {

  final case class ThemeProps(theme: ThemeModel)

  lazy val reactClass: ReactClass = React.createClass[ThemeProps, Unit](render = (self) => {
    <("theme")()(
      <.ThemeHeaderComponent(^.wrapped := ThemeHeaderProps(self.props.wrapped.theme))(),
      <.div(^.className := ThemeComponentStyles.contentWrapper)(
        <.PoliticalActionsContainerComponent()(),
        <.ResultsInThemeContainerComponent(
          ^.wrapped := ResultsInThemeContainerProps(currentTheme = self.props.wrapped.theme)
        )(),
        <.NavInThemesContainerComponent.empty
      ),
      <.style()(ThemeComponentStyles.render[String])
    )
  })
}

object ThemeComponentStyles extends StyleSheet.Inline {

  import dsl._

  val contentWrapper: StyleA =
    style(display.block, backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

}
