package org.make.front.components.presentationals

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.models._
import org.make.front.styles.BulmaStyles

object HomeComponent {
  lazy val reactClass: ReactClass =
    React.createClass[Unit, Unit](
      render = (_) =>
        <.div()(
          <.HomeHeaderComponent.empty,
          <.div(^.className := BulmaStyles.Layout.container)(
            <.div(
              ^.className := BulmaStyles.Grid.Columns.columns
            )()
          )
        )
    )
}
