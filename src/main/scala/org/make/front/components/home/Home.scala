package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
import org.make.front.components.showcase.TrendingShowcaseContainer.TrendingShowcaseContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object Home {
  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "Home",
        render = { self =>
          <("home")()(
            <.IntroComponent.empty,
            <.div(^.className := HomeStyles.item)(
              <.ThemeShowcaseContainerComponent(
                ^.wrapped := ThemeShowcaseContainerProps(
                  themeSlug = "developpement-durable-energie",
                  maybeIntro = Some(unescape(I18n.t("home.showcase-1.intro"))),
                  maybeNews = Some(I18n.t("home.showcase-1.news"))
                )
              )()
            ),
            <.div(^.className := HomeStyles.item)(<.ExplanationsComponent.empty),
            <.div(^.className := HomeStyles.item)(
              <.TrendingShowcaseContainerComponent(
                ^.wrapped := TrendingShowcaseContainerProps(
                  trending = "trending",
                  intro = unescape(I18n.t("home.showcase-2.intro")),
                  title = unescape(I18n.t("home.showcase-2.title"))
                )
              )()
            ),
            <.div(^.className := HomeStyles.item)(
              <.ThemeShowcaseContainerComponent(
                ^.wrapped := ThemeShowcaseContainerProps(themeSlug = "economie-emploi-travail")
              )()
            ),
            <.div(^.className := HomeStyles.item)(
              <.TrendingShowcaseContainerComponent(
                ^.wrapped := TrendingShowcaseContainerProps(
                  trending = "hot",
                  intro = unescape(I18n.t("home.showcase-3.intro")),
                  title = unescape(I18n.t("home.showcase-3.title"))
                )
              )()
            ),
            <.div(^.className := HomeStyles.item)(
              <.ThemeShowcaseContainerComponent(
                ^.wrapped := ThemeShowcaseContainerProps(themeSlug = "vivre-ensemble-solidarites")
              )()
            ),
            <.NavInThemesContainerComponent.empty,
            <.style()(HomeStyles.render[String])
          )
        }
      )
}

object HomeStyles extends StyleSheet.Inline {

  import dsl._

  val item: StyleA =
    style(display.block, boxSizing.contentBox, borderBottom :=! s"1px solid ${ThemeStyles.BorderColor.white.value}")

}
