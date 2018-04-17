package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.theme.Theme.ThemeProps
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{RWDHideRulesStyles, TableLayoutStyles}
import org.make.front.styles.utils._

import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

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
            <.section(^.className := Seq(TableLayoutStyles.fullHeightWrapper, MaybeThemeStyles.wrapper))(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := Seq(TableLayoutStyles.cell, MaybeThemeStyles.mainHeaderWrapper))(
                  <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
                  <.div(^.className := MaybeThemeStyles.fixedMainHeaderWrapper)(
                    <.CookieAlertContainerComponent.empty,
                    <.MainHeaderContainer.empty
                  )
                )
              ),
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := Seq(MaybeThemeStyles.content, TableLayoutStyles.cellVerticalAlignMiddle))(
                  <.SpinnerComponent.empty
                )
              ),
              <.style()(MaybeThemeStyles.render[String])
            )
          )
        }
      )
}

object MaybeThemeStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(tableLayout.fixed, backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val content: StyleA =
    style(
      height(100.%%),
      paddingBottom(ThemeStyles.SpacingValue.large.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.SpacingValue.evenLarger.pxToEm()))
    )

  val mainHeaderWrapper: StyleA =
    style(
      paddingBottom(50.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
    )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

}
