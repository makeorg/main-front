package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.containers.ThemeContainerComponent

object ContainerComponent {
  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div()(
        <.Switch()(
          <.Route(^.exact := true, ^.path := "/theme/:themeSlug", ^.component := ThemeContainerComponent.reactClass)(),
          <.Route(^.exact := true, ^.path := "/", ^.component := HomeComponent.reactClass)()
        )
    )
  )
}
