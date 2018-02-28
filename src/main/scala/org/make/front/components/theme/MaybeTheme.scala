package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.theme.Theme.ThemeProps
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.base.TableLayoutStyles

object MaybeTheme {

  final case class MaybeThemeProps(maybeTheme: Option[TranslatedThemeModel] = None,
                                   maybeOperation: Option[OperationModel],
                                   maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[MaybeThemeProps, Unit](
        displayName = "MaybeTheme",
        render = { self =>
          self.props.wrapped.maybeTheme.map { theme =>
            <.ThemeComponent(
              ^.wrapped := ThemeProps(
                theme = theme,
                maybeOperation = self.props.wrapped.maybeOperation,
                maybeLocation = self.props.wrapped.maybeLocation
              )
            )()
          }.getOrElse(
            <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := TableLayoutStyles.cell)(<.MainHeaderContainer.empty)
              ),
              <.div(^.className := TableLayoutStyles.fullHeightRow)(
                <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(<.SpinnerComponent.empty)
              )
            )
          )
        }
      )
}
