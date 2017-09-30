package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.showcase.ShowcaseContainer.ShowcaseContainerProps

object Home {
  lazy val reactClass: ReactClass =
    React.createClass[Unit, Unit](
      displayName = "Home",
      render = (_) =>
        <("home")()(
          <.MainIntroComponent.empty,
          <.ShowcaseContainerComponent(
            ^.wrapped := ShowcaseContainerProps(introTranslationKey = "content.homepage.expressYourself")
          )(),
          <.NavInThemesContainerComponent.empty
      )
    )
}
