/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.helpers.NumberFormat
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import scalacss.internal.Attr

import scala.scalajs.js

object ProgressBar {

  final case class ProgressBarProps(value: Int, total: Int, maybeThemeColor: Option[String])

  final case class ProgressBarState(value: Int, total: Int)

  lazy val reactClass: ReactClass =
    React.createClass[ProgressBarProps, ProgressBarState](displayName = "ProgressBar", getInitialState = { self =>
      ProgressBarState(value = 0, total = 0)
    }, componentWillReceiveProps = { (self, props) =>
      self.setState(ProgressBarState(value = props.wrapped.value, total = props.wrapped.total))
    }, render = {
      self =>
        object DynamicProgressBarStyles extends StyleSheet.Inline {
          import dsl._

          def progressValue(progression: Int): StyleA =
            style(marginLeft :=! s"${progression.toString}%")

          def limitedProgressValue(maybeThemeColor: Option[String]): (Boolean) => StyleA = styleF.bool(
            limited =>
              if (limited && maybeThemeColor.nonEmpty) {
                styleS(
                  backgroundColor :=! maybeThemeColor.get,
                  color(ThemeStyles.TextColor.white),
                  &.after(Attr.real("border-top-color") := maybeThemeColor.get)
                )
              } else {
                styleS()
            }
          )

          def progressBar(progression: Int, themeColor: String): StyleA =
            style(&.after(backgroundColor :=! themeColor, width :=! s"${progression.toString}%"))
        }

        val progressionInPercent: Int = if (self.state.total != 0) {
          NumberFormat.formatToPercent(self.state.value + 1, self.state.total)
        } else {
          0
        }
        <.div(^.className := ProgressBarStyles.progressBar)(
          <.p(
            ^.className := js.Array(
              ProgressBarStyles.progressValue,
              DynamicProgressBarStyles.progressValue(progressionInPercent),
              DynamicProgressBarStyles
                .limitedProgressValue(self.props.wrapped.maybeThemeColor)(self.state.value + 1 == self.state.total)
            )
          )(<.span(^.className := TextStyles.smallText)(if (self.state.total != 0) {
            self.state.value + 1 + "/" + self.state.total
          } else { "0/0" })),
          <.p(
            ^.className := js.Array(
              ProgressBarStyles.progressBarTrack,
              DynamicProgressBarStyles
                .progressBar(progressionInPercent, self.props.wrapped.maybeThemeColor.getOrElse("rgba(0, 0, 0, 0.2)"))
            )
          )(),
          <.style()(ProgressBarStyles.render[String], DynamicProgressBarStyles.render[String])
        )
    })
}

object ProgressBarStyles extends StyleSheet.Inline {
  import dsl._

  val progressBar: StyleA =
    style(position.relative)

  val progressValue: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      marginBottom(12.pxToEm()),
      padding(`0`, 10.pxToEm()),
      lineHeight(24.pxToEm()),
      textAlign.center,
      whiteSpace.nowrap,
      color(ThemeStyles.TextColor.light),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      transform := "translateX(-50%)",
      /*transition := "margin .2s ease-in-out, color .2s ease-in-out, background-color .2s ease-in-out",*/
      &.after(
        content := "''",
        position.absolute,
        left(50.%%),
        top(100.%%),
        marginLeft(-6.pxToEm()),
        borderRight(6.pxToEm(), solid, transparent),
        borderTop(6.pxToEm(), solid, ThemeStyles.BackgroundColor.white),
        borderLeft(6.pxToEm(), solid, transparent) /*,
        transition := "border-color .2s ease-in-out"*/
      )
    )

  val progressBarTrack: StyleA =
    style(
      position.relative,
      height(3.px),
      width(100.%%),
      backgroundColor(ThemeStyles.BorderColor.veryLight),
      &.after(
        content := "''",
        position.absolute,
        top(`0`),
        left(`0`),
        height(100.%%),
        width(0.%%),
        backgroundColor(rgba(0, 0, 0, 0.2)) /*,
        transition := "width .2s ease-in-out"*/
      )
    )

}
