package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
import org.make.front.components.showcase.TrendingShowcaseContainer.TrendingShowcaseContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape

object Home {
  lazy val reactClass: ReactClass =
    React.createClass[Unit, Unit](
      displayName = "Home",
      render = (_) =>
        <("home")()(
          <.IntroComponent.empty,
          <.ExplanationsComponent.empty,
          <.TrendingShowcaseContainerComponent(
            ^.wrapped := TrendingShowcaseContainerProps(
              trending = "hot",
              introTranslationKey = "content.homepage.expressYourself",
              title = unescape(I18n.t("content.homepage.mostPopular"))
            )
          )(),
          <.ThemeShowcaseContainerComponent(
            ^.wrapped := ThemeShowcaseContainerProps(introTranslationKey = "content.homepage.expressYourself")
          )(),
          <.NavInThemesContainerComponent.empty
      )
    )
}
