package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.helpers.NumberFormat
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.mutable.StyleSheet.Inline

object ProgressBar {

  final case class ProgressBarProps(value: Int, total: Int, color: String)

  final case class ProgressBarState(value: Int, total: Int)

  lazy val reactClass: ReactClass =
    React.createClass[ProgressBarProps, ProgressBarState](
      displayName = "ProgressBar",
      getInitialState = { self =>
        ProgressBarState(value = self.props.wrapped.value, total = self.props.wrapped.total)
      },
      componentWillReceiveProps = { (self, props) =>
        self.setState(ProgressBarState(value = self.props.wrapped.value, total = self.props.wrapped.total))
      },
      render = { self =>
        object DynamicProgressBarStyles extends Inline {
          import dsl._

          def progressValue(progression: Int): StyleA =
            style(marginLeft := s"${progression.toString}%")

          def progressBar(progression: Int, color: String): StyleA =
            style((&.after)(backgroundColor := s"${color}", width := s"${progression.toString}%"))
        }

        val progressionInPercent: Int = NumberFormat.formatToPercent(self.state.value, self.state.total)

        <.div(^.className := ProgressBarStyles.progressBar)(
          <.p(
            ^.className := Seq(
              ProgressBarStyles.progressValue,
              DynamicProgressBarStyles.progressValue(progressionInPercent)
            )
          )(<.span(^.className := TextStyles.smallText)(self.state.value + "/" + self.state.total)),
          <.p(
            ^.className := Seq(
              ProgressBarStyles.progressBarTrack,
              DynamicProgressBarStyles.progressBar(progressionInPercent, "rgb(237, 24, 68)")
            )
          )(),
          <.style()(ProgressBarStyles.render[String], DynamicProgressBarStyles.render[String])
        )

      }
    )
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
      padding :=! s"0 ${15.pxToEm().value}",
      lineHeight(24.pxToEm()),
      textAlign.center,
      whiteSpace.nowrap,
      color(ThemeStyles.TextColor.light),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      transform := "translateX(-50%)",
      (&.after)(
        content := "''",
        position.absolute,
        left(50.%%),
        top(100.%%),
        marginLeft(-6.pxToEm()),
        borderRight :=! s"${6.pxToEm().value} solid transparent",
        borderTop :=! s"${6.pxToEm().value} solid ${ThemeStyles.BackgroundColor.white.value}",
        borderLeft :=! s"${6.pxToEm().value} solid transparent"
      )
    )

  val progressBarTrack: StyleA =
    style(
      position.relative,
      height(3.px),
      width(100.%%),
      backgroundColor(ThemeStyles.BorderColor.veryLight),
      (&.after)(content := "''", position.absolute, top(`0`), left(`0`), height(100.%%), width(0.%%))
    )

}
