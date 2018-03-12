package org.make.front.components.operation.intro

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{facebookLogo, keringFoundationLogo, I18n, VFFGBWhiteLogo, VFFIll, VFFIll2x}
import org.make.front.models.{
  GradientColor     => GradientColorModel,
  OperationExpanded => OperationModel,
  OperationPartner  => OperationPartnerModel
}
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object VFFGBOperationIntro {
  case class VFFGBOperationIntroProps(operation: OperationModel, language: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[VFFGBOperationIntroProps, Unit](
        displayName = "VFFGBOperationIntro",
        render = (self) => {

          def onClick: () => Unit = { () =>
            TrackingService.track(
              "click-button-learn-more",
              TrackingContext(TrackingLocation.operationPage, Some(self.props.wrapped.operation.slug))
            )
          }

          val operation: OperationModel =
            self.props.wrapped.operation

          val partners = Seq(
            OperationPartnerModel(
              name = "Kering Foundation",
              imageUrl = keringFoundationLogo.toString,
              imageWidth = 80
            ),
            OperationPartnerModel(name = "Facebook", imageUrl = facebookLogo.toString, imageWidth = 80)
          )

          val gradientValues: GradientColorModel =
            operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicVFFGBOperationIntroStyles extends StyleSheet.Inline {
            import dsl._
            val gradient: StyleA =
              style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(^.className := Seq(OperationIntroStyles.wrapper, DynamicVFFGBOperationIntroStyles.gradient))(
            <.div(^.className := OperationIntroStyles.presentationInnerWrapper)(
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.div(^.className := Seq(OperationIntroStyles.titleWrapper, VFFGBOperationIntroStyles.titleWrapper))(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.vff-gb.intro.label"))),
                  <.p(^.className := Seq(OperationIntroStyles.logoWrapper))(
                    <.img(^.src := VFFGBWhiteLogo.toString, ^.alt := unescape(I18n.t("operation.vff-gb.intro.title")))()
                  ),
                  <.p(^.className := Seq(OperationIntroStyles.infos, TextStyles.label))(
                    unescape(I18n.t("operation.vff-gb.intro.period"))
                  ),
                  <.div(^.className := Seq(TableLayoutStyles.wrapper, OperationIntroStyles.separator))(
                    <.div(
                      ^.className := Seq(
                        TableLayoutStyles.cellVerticalAlignMiddle,
                        OperationIntroStyles.separatorLineWrapper
                      )
                    )(
                      <.hr(
                        ^.className := Seq(
                          OperationIntroStyles.separatorLine,
                          OperationIntroStyles.separatorLineToTheLeft
                        )
                      )()
                    ),
                    <.div(^.className := Seq(TableLayoutStyles.cell, OperationIntroStyles.separatorTextWrapper))(
                      <.p(^.className := Seq(OperationIntroStyles.separatorText, TextStyles.smallerText))(
                        unescape(I18n.t("operation.vff-gb.intro.partners.intro"))
                      )
                    ),
                    <.div(
                      ^.className := Seq(
                        TableLayoutStyles.cellVerticalAlignMiddle,
                        OperationIntroStyles.separatorLineWrapper
                      )
                    )(
                      <.hr(
                        ^.className := Seq(
                          OperationIntroStyles.separatorLine,
                          OperationIntroStyles.separatorLineToTheRight
                        )
                      )()
                    )
                  ),
                  <.ul(^.className := OperationIntroStyles.partnersList)(
                    partners.map(
                      partner =>
                        <.li(^.className := OperationIntroStyles.partnerItem)(
                          <.img(
                            ^.src := partner.imageUrl,
                            ^.alt := partner.name,
                            ^("width") := partner.imageWidth.toString,
                            ^.className := OperationIntroStyles.partnerLogo
                          )()
                      )
                    )
                  )
                )
              )
            ),
            <.div(^.className := OperationIntroStyles.explanationWrapper)(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRowWithCols)(
                <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colThirdBeyondSmall))(
                  <.img(
                    ^.src := VFFIll.toString,
                    ^("srcset") := VFFIll.toString + " 1x," + VFFIll2x.toString + " 2x",
                    ^.alt := unescape(I18n.t("operation.vff-gb.intro.title")),
                    ^.className := OperationIntroStyles.explanationIll
                  )()
                ),
                <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colTwoThirdsBeyondSmall))(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.vff-gb.intro.article.title"))),
                  <.div(^.className := OperationIntroStyles.explanationTextWrapper)(
                    <.p(^.className := Seq(OperationIntroStyles.explanationText, TextStyles.smallText))(
                      unescape(I18n.t("operation.vff-gb.intro.article.text"))
                    )
                  ),
                  <.p(^.className := OperationIntroStyles.ctaWrapper)(
                    <.a(
                      ^.onClick := onClick,
                      ^.href := unescape(I18n.t("operation.vff-gb.intro.article.see-more.link")),
                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                      ^.target := "_blank"
                    )(unescape(I18n.t("operation.vff-gb.intro.article.see-more.label")))
                  )
                )
              )
            ),
            <.style()(
              OperationIntroStyles.render[String],
              VFFGBOperationIntroStyles.render[String],
              DynamicVFFGBOperationIntroStyles.render[String]
            )
          )
        }
      )

}

object VFFGBOperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val titleWrapper: StyleA = style(maxWidth(470.pxToEm()))

}