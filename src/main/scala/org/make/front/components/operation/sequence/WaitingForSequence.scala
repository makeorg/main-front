package org.make.front.components.operation.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.styles.utils._

object WaitingForSequence {

  val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "WaitingForSequence",
        render = { _ =>
          <.section(^.className := Seq(TableLayoutStyles.fullHeightWrapper, WaitingForSequenceStyles.wrapper))(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := Seq(TableLayoutStyles.cell, WaitingForSequenceStyles.mainHeaderWrapper))(
                <.MainHeaderContainer.empty
              )
            ),
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := Seq(WaitingForSequenceStyles.content, TableLayoutStyles.cellVerticalAlignMiddle))(
                <.SpinnerComponent.empty
              )
            ),
            <.style()(WaitingForSequenceStyles.render[String])
          )
        }
      )

}

object WaitingForSequenceStyles extends StyleSheet.Inline {

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
    style(visibility.hidden)

}
