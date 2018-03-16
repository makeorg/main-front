package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.styles.utils._

object WaitingForOperation {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "WaitingForOperation",
        render = (self) => {

          <.div(^.className := Seq(WaitingForOperationStyles.wrapper, TableLayoutStyles.fullHeightWrapper))(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := Seq(TableLayoutStyles.cell, WaitingForOperationStyles.mainHeaderWrapper))(
                <.MainHeaderContainer.empty
              )
            ),
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := Seq(WaitingForOperationStyles.content, TableLayoutStyles.cellVerticalAlignMiddle))(
                <.SpinnerComponent.empty
              )
            ),
            <.style()(WaitingForOperationStyles.render[String])
          )
        }
      )
}

object WaitingForOperationStyles extends StyleSheet.Inline {

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
