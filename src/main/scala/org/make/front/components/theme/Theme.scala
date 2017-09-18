package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.presentationals._
import org.make.front.components.proposals.ProposalsListContainer.MatrixWrappedProps
import org.make.front.components.theme.ThemeHeader.ThemeHeaderProps
import org.make.front.models.{Theme => ThemeModel}

object Theme {

  final case class ThemeProps(theme: Option[ThemeModel], themeSlug: String)

  lazy val reactClass: ReactClass = React.createClass[ThemeProps, Unit](
    render = (self) =>
      <("theme")()(
        <.ThemeHeaderComponent(^.wrapped := ThemeHeaderProps(self.props.wrapped.theme))(),
        //TODO: fix 5 errors of setState before render. These are logged in console.
        <.PoliticalActionsContainerComponent()(),
        <.ProposalsContainerComponent(^.wrapped := MatrixWrappedProps(self.props.wrapped.themeSlug))()
    )
  )
}
