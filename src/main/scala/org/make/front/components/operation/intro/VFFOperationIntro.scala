package org.make.front.components.operation.intro

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{facebookLogo, keringFoundationLogo, I18n, VFFIll, VFFIll2x, VFFWhiteLogo}
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

import scala.scalajs.js

object VFFOperationIntro {

  case class VFFOperationIntroProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[VFFOperationIntroProps, Unit](
        displayName = "VFFOperationIntro",
        render = (self) => {

          def onClick: () => Unit = { () =>
            TrackingService.track(
              "click-button-learn-more",
              TrackingContext(TrackingLocation.operationPage, Some(self.props.wrapped.operation.slug))
            )
          }

          val operation: OperationModel =
            self.props.wrapped.operation

          val partners = js.Array(
            OperationPartnerModel(
              name = "Kering Foundation",
              imageUrl = keringFoundationLogo.toString,
              imageWidth = 80
            ),
            OperationPartnerModel(name = "Facebook", imageUrl = facebookLogo.toString, imageWidth = 80)
          )

          val gradientValues: GradientColorModel =
            operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicVFFOperationIntroStyles extends StyleSheet.Inline {
            import dsl._
            val gradient: StyleA =
              style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(^.className := js.Array(OperationIntroStyles.wrapper, DynamicVFFOperationIntroStyles.gradient))(
            <.div(^.className := js.Array(OperationIntroStyles.headingWrapper, LayoutRulesStyles.centeredRow))(
              <.div(^.className := VFFOperationIntroStyles.logoWrapper)(
                <.p(^.className := OperationIntroStyles.labelWrapper)(
                  <.span(^.className := TextStyles.label)(unescape(I18n.t("operation.vff-fr.intro.label")))
                ),
                <.img(^.src := VFFWhiteLogo.toString, ^.alt := unescape(I18n.t("operation.vff-fr.intro.title")))(),
                <.p(^.className := OperationIntroStyles.infosWrapper)(
                  <.span(^.className := js.Array(OperationIntroStyles.infosLabel, TextStyles.label))(
                    unescape(I18n.t("operation.vff-fr.intro.period"))
                  )
                )
              ),
              <.div(^.className := js.Array(TableLayoutStyles.wrapper, OperationIntroStyles.separator))(
                <.div(
                  ^.className := js
                    .Array(TableLayoutStyles.cellVerticalAlignMiddle, OperationIntroStyles.separatorLineWrapper)
                )(
                  <.hr(
                    ^.className := js
                      .Array(OperationIntroStyles.separatorLine, OperationIntroStyles.separatorLineToTheLeft)
                  )()
                ),
                <.div(^.className := js.Array(TableLayoutStyles.cell, OperationIntroStyles.separatorTextWrapper))(
                  <.p(^.className := js.Array(OperationIntroStyles.separatorText, TextStyles.smallerText))(
                    unescape(I18n.t("operation.vff-fr.intro.partners.intro"))
                  )
                ),
                <.div(
                  ^.className := js
                    .Array(TableLayoutStyles.cellVerticalAlignMiddle, OperationIntroStyles.separatorLineWrapper)
                )(
                  <.hr(
                    ^.className := js
                      .Array(OperationIntroStyles.separatorLine, OperationIntroStyles.separatorLineToTheRight)
                  )()
                )
              ),
              <.ul(^.className := OperationIntroStyles.partnersList)(
                partners
                  .map(
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
                  .toSeq
              ),
              <.p(^.className := js.Array(OperationIntroStyles.otherPartners, TextStyles.smallText))(
                unescape(I18n.t("operation.vff-fr.intro.partners.others"))
              )
            ),
            <.div(^.className := OperationIntroStyles.explanationWrapper)(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRowWithCols)(
                <.div(^.className := js.Array(ColRulesStyles.col, ColRulesStyles.colThirdBeyondSmall))(
                  <.img(
                    ^.src := VFFIll.toString,
                    ^("srcset") := VFFIll.toString + " 1x," + VFFIll2x.toString + " 2x",
                    ^.alt := unescape(I18n.t("operation.vff-fr.intro.title")),
                    ^.className := OperationIntroStyles.explanationIll
                  )()
                ),
                <.div(^.className := js.Array(ColRulesStyles.col, ColRulesStyles.colTwoThirdsBeyondSmall))(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.vff-fr.intro.article.title"))),
                  <.div(^.className := OperationIntroStyles.explanationTextWrapper)(
                    <.p(^.className := js.Array(OperationIntroStyles.explanationText, TextStyles.smallText))(
                      unescape(I18n.t("operation.vff-fr.intro.article.text"))
                    )
                  ),
                  <.p(^.className := OperationIntroStyles.ctaWrapper)(
                    <.a(
                      ^.onClick := onClick,
                      ^.href := unescape(I18n.t("operation.vff-fr.intro.article.see-more.link")),
                      ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA),
                      ^.target := "_blank"
                    )(unescape(I18n.t("operation.vff-fr.intro.article.see-more.label")))
                  )
                )
              )
            ),
            <.style()(
              OperationIntroStyles.render[String],
              VFFOperationIntroStyles.render[String],
              DynamicVFFOperationIntroStyles.render[String]
            )
          )
        }
      )
}

object VFFOperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val logoWrapper: StyleA = style(maxWidth(417.pxToEm()), marginLeft.auto, marginRight.auto)

}
