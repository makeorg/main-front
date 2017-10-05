package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
import org.make.front.components.showcase.TrendingShowcaseContainer.TrendingShowcaseContainerProps
import org.make.front.facades.{logoMake, I18n, Replacements}
import org.make.front.facades.Unescape.unescape

object Home {
  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "Home",
        render = { self =>
          <("home")()(
            <.IntroComponent.empty,
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(themeSlug = "developpement-durable-energie")
            )(),
            <.ExplanationsComponent.empty,
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "trending",
                intro = unescape(I18n.t("content.homepage.expressYourself")),
                title = unescape(I18n.t("content.homepage.mostPopular"))
              )
            )(),
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(themeSlug = "economie-emploi-travail")
            )(),
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "hot",
                intro = unescape("départagez-les&nbsp;!"),
                title = unescape(I18n.t("content.homepage.mostDebated"))
              )
            )(),
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(themeSlug = "vivre-ensemble-solidarites")
            )(),
            <.NavInThemesContainerComponent.empty
          )
        }
      )

}
