package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.containers.{
  ContainerComponent,
  FooterContainerComponent,
  NotificationContainerComponent
}
import org.make.front.components.presentationals.HeaderComponent

object AppComponent {
  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div(^.className := "App")(
        <(HeaderComponent()).empty,
        <(ContainerComponent()).empty,
        <(FooterContainerComponent()).empty,
        <(NotificationContainerComponent()).empty
    )
  )

}
