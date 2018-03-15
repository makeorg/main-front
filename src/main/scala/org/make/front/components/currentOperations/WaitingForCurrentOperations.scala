package org.make.front.components.currentOperations

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.styles.utils._

object WaitingForCurrentOperations {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "WaitingForCurrentOperations",
        render = (_) => {

          <.div(^.className := Seq(WaitingForCurrentOperationsStyles.wrapper, TableLayoutStyles.fullHeightWrapper))(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := Seq(TableLayoutStyles.cell, WaitingForCurrentOperationsStyles.mainHeaderWrapper))(
                <.MainHeaderContainer.empty
              )
            ),
            <.div(^.className := Seq(TableLayoutStyles.row, WaitingForCurrentOperationsStyles.fullHeight))(
              <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(<.SpinnerComponent.empty)
            ),
            <.style()(WaitingForCurrentOperationsStyles.render[String])
          )
        }
      )
}

object WaitingForCurrentOperationsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      tableLayout.fixed,
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm())),
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )

  val fullHeight: StyleA =
    style(height(100.%%))

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)

}
