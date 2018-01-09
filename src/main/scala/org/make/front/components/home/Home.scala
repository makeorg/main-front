package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.home.FeaturedOperationContainer.FeaturedOperationContainerProps
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
import org.make.front.components.showcase.TrendingShowcaseContainer.TrendingShowcaseContainerProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.models.{Location, OperationDesignData}
import org.make.front.styles.ThemeStyles

object Home {
  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "Home",
        componentDidMount = { _ =>
          FacebookPixel.fbq("trackCustom", "display-page-home")
        },
        render = { self =>
          <.div(^.className := HomeStyles.wrapper)(
            <.div(^.className := HomeStyles.mainHeaderWrapper)(<.MainHeaderComponent.empty),
            <.h1(^.style := Map("display" -> "none"))("Make.org"),
            <.FeaturedOperationContainerComponent(
              ^.wrapped := FeaturedOperationContainerProps(operationSlug = OperationDesignData.featuredOperationSlug)
            )(),
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "sante-alimentation",
                maybeIntro = Some(unescape(I18n.t("home.showcase-1.intro"))),
                maybeNews = Some(I18n.t("home.showcase-1.news")),
                maybeOperation = None,
                maybeSequence = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.ExplanationsComponent.empty,
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "trending",
                intro = unescape(I18n.t("home.showcase-2.intro")),
                title = unescape(I18n.t("home.showcase-2.title")),
                maybeTheme = None,
                maybeOperation = None,
                maybeSequence = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "economie-emploi-travail",
                maybeOperation = None,
                maybeSequence = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "hot",
                intro = unescape(I18n.t("home.showcase-3.intro")),
                title = unescape(I18n.t("home.showcase-3.title")),
                maybeTheme = None,
                maybeOperation = None,
                maybeSequence = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "vivre-ensemble-solidarites",
                maybeOperation = None,
                maybeSequence = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.NavInThemesContainerComponent.empty,
            <.style()(HomeStyles.render[String])
          )
        }
      )
}

object HomeStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(unsafeChild("> section")(borderBottom(1.px, solid, ThemeStyles.BorderColor.white)))

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)
}
