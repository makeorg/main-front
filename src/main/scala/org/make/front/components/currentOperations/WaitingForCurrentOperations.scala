package org.make.front.components.currentOperations

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.styles.utils._

import scala.scalajs.js

object WaitingForCurrentOperations {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "WaitingForCurrentOperations",
        render = (_) => {

          <.div(
            ^.className := js.Array(WaitingForCurrentOperationsStyles.wrapper, TableLayoutStyles.fullHeightWrapper)
          )(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(
                ^.className := js.Array(TableLayoutStyles.cell, WaitingForCurrentOperationsStyles.mainHeaderWrapper)
              )(<.MainHeaderContainer.empty)
            ),
            <.div(^.className := TableLayoutStyles.row)(
              <.div(
                ^.className := js
                  .Array(WaitingForCurrentOperationsStyles.content, TableLayoutStyles.cellVerticalAlignMiddle)
              )(<.SpinnerComponent.empty)
            ),
            <.style()(WaitingForCurrentOperationsStyles.render[String])
          )
        }
      )
}

object WaitingForCurrentOperationsStyles extends StyleSheet.Inline {

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
