package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.AppComponentStyles
import org.make.front.components.presentationals.ThemeHeaderComponent.ThemeHeaderProps
import org.make.front.models.Theme

object ThemeComponent {

  final case class ThemeProps(theme: Option[Theme])

  lazy val reactClass: ReactClass = React.createClass[ThemeProps, Unit](
    render = (self) =>
      <.div(^.className := "theme")(
        <.ThemeHeaderComponent(^.wrapped := ThemeHeaderProps(self.props.wrapped.theme))(),
        <.div(^.className := AppComponentStyles.container)(
          <.PoliticalActionsComponent()(),
          <.ProposalMatrixComponent()()
        )
      )
  )
}
