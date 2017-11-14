package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.theme.Theme.ThemeProps
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles.base.TableLayoutStyles

object MaybeTheme {

  final case class MaybeThemeProps(maybeTheme: Option[TranslatedThemeModel] = None)

  lazy val reactClass: ReactClass =
    React
      .createClass[MaybeThemeProps, Unit](
        displayName = "MaybeTheme",
        render = { self =>
          self.props.wrapped.maybeTheme.map { theme =>
            <.ThemeComponent(^.wrapped := ThemeProps(theme = theme))()
          }.getOrElse(
            <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := TableLayoutStyles.cell)(<.MainHeaderComponent.empty)
              ),
              <.div(^.className := TableLayoutStyles.fullHeightRow)(
                <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(<.SpinnerComponent.empty)
              )
            )
          )
        }
      )
}
