package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.AppComponentStyles
import org.make.front.components.containers.ProposalMatrixContainerComponent.MatrixWrappedProps
import org.make.front.components.presentationals.ThemeHeaderComponent.ThemeHeaderProps
import org.make.front.models.Theme

object ThemeComponent {

  final case class ThemeProps(theme: Option[Theme], themeSlug: String)

  lazy val reactClass: ReactClass = React.createClass[ThemeProps, Unit](
    render = (self) =>
      <.div(^.className := "theme")(
        //TODO: fix CSS WARNING: `themeTitleStyle-2`: Style being registered after stylesheet has been rendered
        <.ThemeHeaderComponent(^.wrapped := ThemeHeaderProps(self.props.wrapped.theme))(),
        <.div(^.className := AppComponentStyles.container)(
          //TODO: fix 5 errors of setState before render. These are logged in console.
          <.PoliticalActionsComponent()(),
          <.ProposalMatrixContainerComponent(^.wrapped := MatrixWrappedProps(self.props.wrapped.themeSlug))()
        )
    )
  )
}
