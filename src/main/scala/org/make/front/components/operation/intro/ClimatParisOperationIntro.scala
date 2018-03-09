package org.make.front.components.operation.intro

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades._
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{GradientColor => GradientColorModel, OperationExpanded => OperationModel}
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TextStyles}
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

          <.div(^.className := Seq(OperationIntroStyles.wrapper, DynamicClimatParisOperationIntroStyles.gradient))(
            <.div(^.className := OperationIntroStyles.presentationInnerWrapper)(
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.div(
                  ^.className := Seq(OperationIntroStyles.titleWrapper, ClimatParisOperationIntroStyles.titleWrapper)
                )(
                  <.p(^.className := Seq(OperationIntroStyles.logoWrapper))(
                    <.img(
                      ^.src := operation.logoUrl.getOrElse(""),
                      ^.alt := unescape(I18n.t("operation.climatparis.intro.title"))
                    )()
                  )
                )
              )
            ),
            <.div(^.className := OperationIntroStyles.explanationWrapper)(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRowWithCols)(
                <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colThirdBeyondSmall))(
                  <.img(
                    ^.src := climatParisIll.toString,
                    ^("srcset") := climatParisIll.toString + " 1x," + climatParisIll2x.toString + " 2x",
                    ^.alt := unescape(I18n.t("operation.lpae-intro.title")),
                    ^.className := OperationIntroStyles.explanationIll
                  )()
                ),
                <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colTwoThirdsBeyondSmall))(
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

  val titleWrapper: StyleA = style(maxWidth(360.pxToEm()))

}
