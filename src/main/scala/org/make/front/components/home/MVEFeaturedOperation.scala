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

object MVEFeaturedOperation {

  final case class MVEFeaturedOperationProps(trackingLocation: TrackingLocation)

  lazy val reactClass: ReactClass =
    React
      .createClass[MVEFeaturedOperationProps, Unit](
        displayName = "FeaturedOperation",
        render = (self) => {

          def onclick: () => Unit = { () =>
            TrackingService
              .track("click-homepage-header", TrackingContext(self.props.wrapped.trackingLocation, Some("mve")))
            scalajs.js.Dynamic.global.window.open(I18n.t("home.featured-operation.mve.learn-more.link"), "_blank")
          }

          <.section(^.className := Seq(TableLayoutStyles.wrapper, MVEFeaturedOperationStyles.wrapper))(
            <.div(
              ^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, MVEFeaturedOperationStyles.innerWrapper)
            )(
              <.img(
                ^.className := MVEFeaturedOperationStyles.illustration,
                ^.src := featuredMVE.toString,
                ^("srcset") := featuredMVESmall.toString + " 400w, " + featuredMVESmall2x.toString + " 800w, " + featuredMVE.toString + " 1200w, " + featuredMVE2x.toString + " 2400w",
                ^.alt := I18n.t("home.featured-operation.mve.title"),
                ^("data-pin-no-hover") := "true"
              )(),
              <.div(^.className := Seq(MVEFeaturedOperationStyles.innerSubWrapper, LayoutRulesStyles.centeredRow))(
                <.div(^.className := MVEFeaturedOperationStyles.labelWrapper)(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("home.featured-operation.mve.label")))
                ),
                <.h2(^.className := Seq(MVEFeaturedOperationStyles.title, TextStyles.veryBigText, TextStyles.boldText))(
                  unescape(I18n.t("home.featured-operation.mve.title"))
                ),
                <.h3(^.className := Seq(TextStyles.mediumText, MVEFeaturedOperationStyles.subTitle))(
                  unescape(I18n.t("home.featured-operation.mve.purpose"))
                ),
                <.p(^.className := MVEFeaturedOperationStyles.ctaWrapper)(
                  <.button(^.onClick := onclick, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                    unescape(I18n.t("home.featured-operation.mve.learn-more.label"))
                  )
                ),
                <.style()(MVEFeaturedOperationStyles.render[String])
              )
            )
          )
        }
      )
}

object MVEFeaturedOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, height(440.pxToEm()), background := s"linear-gradient(130deg, #f6dee3, #d5e7ff)")

  val innerWrapper: StyleA =
    style(position.relative, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`), textAlign.center, overflow.hidden)

  val illustration: StyleA =
    style(
      position.absolute,
      bottom(`0`),
      left(50.%%),
      height(400.pxToEm()),
      maxHeight.none,
      width.auto,
      maxWidth.none,
      transform := s"translate(-50%, 0)"
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
