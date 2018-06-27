package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.styles._
import org.make.front.styles.base.{LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object CultureFeaturedOperation {

  final case class CultureFeaturedOperationProps(trackingLocation: TrackingLocation)

  lazy val reactClass: ReactClass =
    React
      .createClass[CultureFeaturedOperationProps, Unit](
      displayName = "FeaturedOperation",
      render = (self) => {

        def learnMoreOnFeaturedOperation: () => Unit = { () =>
          TrackingService
            .track(
              "click-homepage-header",
              TrackingContext(self.props.wrapped.trackingLocation, Some("chance-aux-jeunes"))
            )
          scalajs.js.Dynamic.global.window
            .open(I18n.t("home.featured-operation.culture.learn-more.link"), "_blank")
        }

        <.section(^.className := js.Array(TableLayoutStyles.wrapper, CultureFeaturedOperationStyles.wrapper))(
          <.div(
            ^.className := js
              .Array(TableLayoutStyles.cellVerticalAlignMiddle, CultureFeaturedOperationStyles.innerWrapper)
          )(
            <.img(
              ^.className := CultureFeaturedOperationStyles.illustration,
              ^.src := cultureLarge.toString,
              ^("srcset") := cultureSmall.toString + " 400w, " + cultureSmall2x.toString + " 800w, " + cultureMedium.toString + " 840w, " + cultureMedium2x.toString + " 1680w, " + cultureLarge.toString + " 1350w, " + cultureLarge2x.toString + " 2700w",
              ^.alt := I18n.t("home.featured-operation.culture.intro.title"),
              ^("data-pin-no-hover") := "true"
            )(),
            <.div(^.className := js.Array(CultureFeaturedOperationStyles.innerSubWrapper, LayoutRulesStyles.centeredRow))(
              <.div(^.className := CultureFeaturedOperationStyles.labelWrapper)(
                <.p(^.className := TextStyles.label)(
                  unescape(I18n.t("home.featured-operation.culture.label"))
                )
              ),
              <.h2(
                ^.className := js.Array(CultureFeaturedOperationStyles.title, TextStyles.veryBigText, TextStyles.boldText)
              )(unescape(I18n.t("home.featured-operation.culture.title"))),
              <.h3(^.className := js.Array(TextStyles.mediumText, CultureFeaturedOperationStyles.subTitle))(
                unescape(I18n.t("home.featured-operation.culture.purpose"))
              ),
              <.p(^.className := CultureFeaturedOperationStyles.ctaWrapper)(
                <.button(^.onClick := learnMoreOnFeaturedOperation, ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton))(
                  unescape(I18n.t("home.featured-operation.culture.learn-more.label"))
                )
              ),
              <.style()(CultureFeaturedOperationStyles.render[String])
            )
          )
        )
      }
    )
}

object CultureFeaturedOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, height(440.pxToEm()), background := s"black")

  val innerWrapper: StyleA =
    style(position.relative, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`), textAlign.center, overflow.hidden)

  val illustration: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      height.auto,
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, -50%)",
      opacity(0.85)
    )

  val innerSubWrapper: StyleA =
    style(position.relative, zIndex(1))

  val labelWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.larger.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val title: StyleA =
    style(color(ThemeStyles.TextColor.white), textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)")

  val subTitle: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      color(ThemeStyles.TextColor.white),
      textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)"
    )

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

}
