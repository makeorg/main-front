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

object FeaturedOperation {

  final case class FeaturedOperationProps(trackingLocation: TrackingLocation)

  lazy val reactClass: ReactClass =
    React
      .createClass[FeaturedOperationProps, Unit](
        displayName = "FeaturedOperation",
        render = (self) => {

          def onclick: () => Unit = { () =>
            TrackingService
              .track("click-homepage-header", TrackingContext(self.props.wrapped.trackingLocation, Some("vff")))
            scalajs.js.Dynamic.global.window.open(I18n.t("home.featured-operation.learn-more.link"), "_blank")
          }

          <.section(^.className := Seq(TableLayoutStyles.wrapper, FeaturedOperationStyles.wrapper))(
            <.div(^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, FeaturedOperationStyles.innerWrapper))(
              <.img(
                ^.className := FeaturedOperationStyles.illustration,
                ^.src := featuredVFF.toString,
                ^("srcset") := featuredVFFSmall.toString + " 400w, " + featuredVFFSmall2x.toString + " 800w, " + featuredVFFMedium.toString + " 840w, " + featuredVFFMedium2x.toString + " 1680w, " + featuredVFF.toString + " 1350w, " + featuredVFF2x.toString + " 2700w",
                ^.alt := I18n.t("home.featured-operation.title"),
                ^("data-pin-no-hover") := "true"
              )(),
              <.div(^.className := Seq(FeaturedOperationStyles.innerSubWrapper, LayoutRulesStyles.centeredRow))(
                <.div(^.className := FeaturedOperationStyles.labelWrapper)(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("home.featured-operation.label")))
                ),
                <.h2(^.className := Seq(FeaturedOperationStyles.title, TextStyles.veryBigText, TextStyles.boldText))(
                  unescape(I18n.t("home.featured-operation.title"))
                ),
                <.h3(^.className := Seq(TextStyles.mediumText, FeaturedOperationStyles.subTitle))(
                  unescape(I18n.t("home.featured-operation.purpose"))
                ),
                <.p(^.className := FeaturedOperationStyles.ctaWrapper)(
                  <.button(^.onClick := onclick, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                    unescape(I18n.t("home.featured-operation.learn-more.label"))
                  )
                ),
                <.style()(FeaturedOperationStyles.render[String])
              )
            )
          )
        }
      )
}

object FeaturedOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, height(440.pxToEm()), backgroundColor(ThemeStyles.BackgroundColor.black))

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
      opacity(0.7)
    )

  val innerSubWrapper: StyleA =
    style(position.relative, zIndex(1))

  val labelWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

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
