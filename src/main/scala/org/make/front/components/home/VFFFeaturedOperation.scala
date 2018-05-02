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

object VFFFeaturedOperation {

  final case class VFFFeaturedOperationProps(trackingLocation: TrackingLocation)

  lazy val reactClass: ReactClass =
    React
      .createClass[VFFFeaturedOperationProps, Unit](
        displayName = "FeaturedOperation",
        render = (self) => {

          def onclick: () => Unit = { () =>
            TrackingService
              .track("click-homepage-header", TrackingContext(self.props.wrapped.trackingLocation, Some("vff")))
            scalajs.js.Dynamic.global.window.open(I18n.t("home.featured-operation.vff.learn-more.link"), "_blank")
          }

          <.section(^.className := js.Array(TableLayoutStyles.wrapper, VFFFeaturedOperationStyles.wrapper))(
            <.div(
              ^.className := js
                .Array(TableLayoutStyles.cellVerticalAlignMiddle, VFFFeaturedOperationStyles.innerWrapper)
            )(
              <.img(
                ^.className := VFFFeaturedOperationStyles.illustration,
                ^.src := featuredVFFIll.toString,
                ^("srcset") := featuredVFFIllSmall.toString + " 400w, " + featuredVFFIllSmall2x.toString + " 800w, " + featuredVFFIllMedium.toString + " 840w, " + featuredVFFIllMedium2x.toString + " 1680w, " + featuredVFFIll.toString + " 1350w, " + featuredVFFIll2x.toString + " 2700w",
                ^.alt := I18n.t("home.featured-operation.vff.title"),
                ^("data-pin-no-hover") := "true"
              )(),
              <.div(^.className := js.Array(VFFFeaturedOperationStyles.innerSubWrapper, LayoutRulesStyles.centeredRow))(
                <.div(^.className := VFFFeaturedOperationStyles.labelWrapper)(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("home.featured-operation.vff.label")))
                ),
                <.h2(
                  ^.className := js.Array(VFFFeaturedOperationStyles.title, TextStyles.veryBigText, TextStyles.boldText)
                )(unescape(I18n.t("home.featured-operation.vff.title"))),
                <.h3(^.className := js.Array(TextStyles.mediumText, VFFFeaturedOperationStyles.subTitle))(
                  unescape(I18n.t("home.featured-operation.vff.purpose"))
                ),
                <.p(^.className := VFFFeaturedOperationStyles.ctaWrapper)(
                  <.button(^.onClick := onclick, ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton))(
                    unescape(I18n.t("home.featured-operation.vff.learn-more.label"))
                  )
                ),
                <.style()(VFFFeaturedOperationStyles.render[String])
              )
            )
          )
        }
      )
}

object VFFFeaturedOperationStyles extends StyleSheet.Inline {

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
