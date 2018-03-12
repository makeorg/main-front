package org.make.front.components.operation.intro

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{
  climatParisWhiteLogo,
  featuredClimatParis,
  featuredClimatParis2x,
  featuredClimatParisMedium,
  featuredClimatParisMedium2x,
  featuredClimatParisSmall,
  featuredClimatParisSmall2x,
  I18n
}
import org.make.front.models.{GradientColor => GradientColorModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object ClimatParisOperationIntro {

  case class ClimatParisOperationIntroProps(operation: OperationModel, language: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ClimatParisOperationIntroProps, Unit](
        displayName = "ClimatParisOperationIntro",
        render = (self) => {

          def onClick: () => Unit = { () =>
            TrackingService.track(
              "click-button-learn-more",
              TrackingContext(TrackingLocation.operationPage, Some(self.props.wrapped.operation.slug))
            )
          }

          val operation: OperationModel =
            self.props.wrapped.operation

          val gradientValues: GradientColorModel =
            operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicClimatParisOperationIntroStyles extends StyleSheet.Inline {
            import dsl._
            val gradient: StyleA =
              style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(
            ^.className := Seq(
              OperationIntroStyles.wrapper,
              ClimatParisOperationIntroStyles.wrapper,
              DynamicClimatParisOperationIntroStyles.gradient
            )
          )(
            <.img(
              ^.className := ClimatParisOperationIntroStyles.illustration,
              ^.src := featuredClimatParis.toString,
              ^("srcset") := featuredClimatParisSmall.toString + " 400w, " + featuredClimatParisSmall2x.toString + " 800w, " + featuredClimatParisMedium.toString + " 840w, " + featuredClimatParisMedium2x.toString + " 1680w, " + featuredClimatParis.toString + " 1350w, " + featuredClimatParis2x.toString + " 2700w",
              ^.alt := I18n.t("operation.climatparis.intro.title"),
              ^("data-pin-no-hover") := "true"
            )(),
            <.div(^.className := OperationIntroStyles.presentationInnerWrapper)(
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.div(
                  ^.className := Seq(OperationIntroStyles.titleWrapper, ClimatParisOperationIntroStyles.titleWrapper)
                )(
                  <.p(^.className := Seq(OperationIntroStyles.logoWrapper))(
                    <.img(
                      ^.src := climatParisWhiteLogo.toString,
                      ^.alt := unescape(I18n.t("operation.climatparis.intro.title"))
                    )()
                  )
                )
              )
            ),
            <.div(
              ^.className := Seq(
                OperationIntroStyles.explanationWrapper,
                ClimatParisOperationIntroStyles.explanationWrapper
              )
            )(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRow)(
                <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.climatparis.intro.article.title"))),
                <.div(^.className := OperationIntroStyles.explanationTextWrapper)(
                  <.p(^.className := Seq(OperationIntroStyles.explanationText, TextStyles.smallText))(
                    unescape(I18n.t("operation.climatparis.intro.article.text"))
                  )
                ),
                <.p(^.className := OperationIntroStyles.ctaWrapper)(
                  <.a(
                    ^.onClick := onClick,
                    ^.href := unescape(I18n.t("operation.climatparis.intro.article.see-more.link")),
                    ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                    ^.target := "_blank"
                  )(unescape(I18n.t("operation.climatparis.intro.article.see-more.label")))
                )
              )
            ),
            <.style()(
              OperationIntroStyles.render[String],
              ClimatParisOperationIntroStyles.render[String],
              DynamicClimatParisOperationIntroStyles.render[String]
            )
          )
        }
      )
}

object ClimatParisOperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA = style(position.relative, overflow.hidden)

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
      opacity(0.7),
      mixBlendMode := "multiply"
    )

  val titleWrapper: StyleA = style(maxWidth(360.pxToEm()))

  val explanationWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.blackTransparent))

}
