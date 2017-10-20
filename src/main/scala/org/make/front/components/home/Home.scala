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
    React
      .createClass[Unit, Unit](
        displayName = "Home",
        render = { self =>
          <("home")()(
            <.IntroComponent.empty,
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "developpement-durable-energie",
                maybeIntro = Some(unescape(I18n.t("home.showcase-1.intro"))),
                maybeNews = Some(I18n.t("home.showcase-1.news"))
              )
            )(),
            <.ExplanationsComponent.empty,
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "trending",
                intro = unescape(I18n.t("home.showcase-2.intro")),
                title = unescape(I18n.t("home.showcase-2.title"))
              )
            )(),
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(themeSlug = "economie-emploi-travail")
            )(),
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "hot",
                intro = unescape(I18n.t("home.showcase-3.intro")),
                title = unescape(I18n.t("home.showcase-3.title"))
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
