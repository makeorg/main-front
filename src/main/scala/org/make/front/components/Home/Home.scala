package org.make.front.components.Home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Showcase.ShowcaseContainerComponent.ShowcaseContainerProps
import org.make.front.components.presentationals._

object HomeComponent {
  lazy val reactClass: ReactClass =
    React.createClass[Unit, Unit](
      render = (_) =>
        <("home")()(
          <.MainIntroComponent.empty,
          <.ShowcaseContainerComponent(
            ^.wrapped := ShowcaseContainerProps(translationKey = "content.homepage.expressYourself")
          )()
      )
    )
}
