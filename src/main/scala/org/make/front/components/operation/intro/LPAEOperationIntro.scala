package org.make.front.components.operation.intro

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.{CercleEconomistesLogo, I18n, LpaeIll, LpaeIll2x, MakeOrgLogo, XPartners}
import org.make.front.facades.Unescape.unescape
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

object LPAEOperationIntro {

  case class LPAEOperationIntroProps(operation: OperationModel, language: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[LPAEOperationIntroProps, Unit](
        displayName = "LPAEOperationIntro",
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
              name = "Le Cercle des Economistes",
              imageUrl = CercleEconomistesLogo.toString,
              imageWidth = 74
            ),
            OperationPartnerModel(name = "x", imageUrl = XPartners.toString, imageWidth = 9),
            OperationPartnerModel(name = "Make.org", imageUrl = MakeOrgLogo.toString, imageWidth = 51)
          )

          val gradientValues: GradientColorModel =
            operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicLPAEOperationIntroStyles extends StyleSheet.Inline {
            import dsl._
            val gradient: StyleA =
              style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(^.className := Seq(OperationIntroStyles.wrapper, DynamicLPAEOperationIntroStyles.gradient))(
            <.div(^.className := OperationIntroStyles.presentationInnerWrapper)(
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.div(^.className := Seq(OperationIntroStyles.titleWrapper, LPAEOperationIntroStyles.titleWrapper))(
                  <.p(^.className := Seq(OperationIntroStyles.logoWrapper))(
                    <.img(
                      ^.src := operation.logoUrl.getOrElse(""),
                      ^.alt := unescape(I18n.t("operation.lpae.intro.title"))
                    )()
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
                        unescape(I18n.t("operation.lpae.intro.partners.intro"))
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
                    ^.src := LpaeIll.toString,
                    ^("srcset") := LpaeIll.toString + " 1x," + LpaeIll2x.toString + " 2x",
                    ^.alt := unescape(I18n.t("operation.lpae.intro.title")),
                    ^.className := OperationIntroStyles.explanationIll
                  )()
                ),
                <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colTwoThirdsBeyondSmall))(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.lpae.intro.article.title"))),
                  <.div(^.className := OperationIntroStyles.explanationTextWrapper)(
                    <.p(^.className := Seq(OperationIntroStyles.explanationText, TextStyles.smallText))(
                      unescape(I18n.t("operation.lpae.intro.article.text"))
                    )
                  ),
                  <.p(^.className := OperationIntroStyles.ctaWrapper)(
                    <.a(
                      ^.onClick := onClick,
                      ^.href := unescape(I18n.t("operation.lpae.intro.article.see-more.link")),
                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                      ^.target := "_blank"
                    )(unescape(I18n.t("operation.lpae.intro.article.see-more.label")))
                  )
                )
              )
            ),
            <.style()(
              OperationIntroStyles.render[String],
              LPAEOperationIntroStyles.render[String],
              DynamicLPAEOperationIntroStyles.render[String]
            )
          )
        }
      )
}

object LPAEOperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val titleWrapper: StyleA = style(maxWidth(830.pxToEm()))

}
