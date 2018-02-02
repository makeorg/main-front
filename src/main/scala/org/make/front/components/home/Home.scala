package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.home.MVEFeaturedOperation.MVEFeaturedOperationProps
import org.make.front.components.home.VFFFeaturedOperation.VFFFeaturedOperationProps
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
import org.make.front.components.showcase.TrendingShowcaseContainer.TrendingShowcaseContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.Location
import org.make.front.styles.ThemeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object Home {

  final case class HomeProps(countryCode: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[HomeProps, Unit](
        displayName = "Home",
        componentDidMount = { _ =>
          TrackingService.track("display-page-home", TrackingContext(TrackingLocation.homepage))
        },
        render = (self) => {
          <.div(^.className := HomeStyles.wrapper)(
            <.div(^.className := HomeStyles.mainHeaderWrapper)(<.MainHeaderContainer.empty),
            <.h1(^.style := Map("display" -> "none"))("Make.org"),
            if (self.props.wrapped.countryCode == "FR") {
              <.MVEFeaturedOperationComponent(
                ^.wrapped := MVEFeaturedOperationProps(trackingLocation = TrackingLocation.homepage)
              )()
            } else {
              <.WelcomeComponent.empty
            },
            <.ActionsShowcaseComponent.empty,
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "sante-alimentation",
                maybeIntro = Some(unescape(I18n.t("home.showcase-1.intro"))),
                maybeNews = Some(I18n.t("home.showcase-1.news")),
                maybeOperation = None,
                maybeSequenceId = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.ExplanationsComponent.empty,
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "trending",
                intro = unescape(I18n.t("home.showcase-2.intro")),
                title = unescape(I18n.t("home.showcase-2.title")),
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "economie-emploi-travail",
                maybeOperation = None,
                maybeSequenceId = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "hot",
                intro = unescape(I18n.t("home.showcase-3.intro")),
                title = unescape(I18n.t("home.showcase-3.title")),
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "vivre-ensemble-solidarites",
                maybeOperation = None,
                maybeSequenceId = None,
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
