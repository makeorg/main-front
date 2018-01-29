package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.models.{
  OperationWording,
  GradientColor     => GradientColorModel,
  OperationExpanded => OperationModel
}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.Main.CssSettings._
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

object OperationIntro {

  case class OperationIntroProps(operation: OperationModel, language: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[OperationIntroProps, Unit](
        displayName = "OperationIntro",
        render =
          (self) => {

            def onClick: () => Unit = { () =>
              TrackingService.track(
                "click-button-learn-more",
                TrackingContext(TrackingLocation.operationPage, Some(self.props.wrapped.operation.slug))
              )
            }

            val operation: OperationModel =
              self.props.wrapped.operation
            val wording: OperationWording = operation.getWordingByLanguageOrError(self.props.wrapped.language)

            val gradientValues: GradientColorModel =
              operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

            object DynamicOperationIntroStyles extends StyleSheet.Inline {
              import dsl._

              val gradient: StyleA =
                style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")

              val labelWrapper: StyleA = style(textAlign :=! operation.greatCauseLabelAlignment.getOrElse("left"))

              val titleWrapper: StyleA = style(maxWidth(operation.logoMaxWidth.getOrElse(470).pxToEm()))

            }

            <.div(^.className := Seq(OperationIntroStyles.wrapper, DynamicOperationIntroStyles.gradient))(
              <.div(^.className := OperationIntroStyles.presentationInnerWrapper)(
                <.div(^.className := LayoutRulesStyles.centeredRow)(
                  <.div(
                    ^.className := Seq(OperationIntroStyles.titleWrapper, DynamicOperationIntroStyles.titleWrapper)
                  )(if (wording.label.isDefined) {
                    <.div(^.className := DynamicOperationIntroStyles.labelWrapper)(
                      <.p(^.className := TextStyles.label)(unescape(wording.label.getOrElse("")))
                    )
                  }, if (operation.logoUrl.isDefined) {
                    <.p(^.className := Seq(OperationIntroStyles.logoWrapper))(
                      <.img(^.src := operation.logoUrl.getOrElse(""), ^.alt := unescape(wording.title))()
                    )
                  }, if (wording.period.isDefined) {
                    <.p(^.className := Seq(OperationIntroStyles.infos, TextStyles.label))(
                      unescape(wording.period.getOrElse(""))
                    )
                  }),
                  if (operation.partners.nonEmpty) {
                    Seq(
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
                            unescape(I18n.t("operation.intro.partners.intro"))
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
                        operation.partners.map(
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
                      ),
                      if (wording.mentionUnderThePartners.isDefined) {
                        <.p(^.className := Seq(OperationIntroStyles.otherPartners, TextStyles.smallText))(
                          unescape(wording.mentionUnderThePartners.getOrElse(""))
                        )
                      }
                    )
                  }
                )
              ),
              if (operation.illustration.isDefined || wording.explanation.isDefined) {
                <.div(^.className := OperationIntroStyles.explanationWrapper)(
                  <.div(^.className := LayoutRulesStyles.narrowerCenteredRowWithCols)(
                    <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colThirdBeyondSmall))(
                      if (operation.illustration.isDefined) {
                        val imageSrcset: String =
                          operation.illustration
                            .map(_.illUrl)
                            .getOrElse("") + " 1x," + operation.illustration
                            .map(_.ill2xUrl)
                            .getOrElse("") + " 2x"
                        <.img(
                          ^.src := operation.illustration.map(_.illUrl).getOrElse(""),
                          ^("srcset") := imageSrcset,
                          ^.alt := unescape(wording.title),
                          ^.className := OperationIntroStyles.explanationIll
                        )()
                      }
                    ),
                    <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colTwoThirdsBeyondSmall))(
                      <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.intro.article.title"))),
                      if (wording.explanation.isDefined) {
                        <.div(^.className := OperationIntroStyles.explanationTextWrapper)(
                          <.p(^.className := Seq(OperationIntroStyles.explanationText, TextStyles.smallText))(
                            unescape(wording.explanation.getOrElse(""))
                          )
                        )
                      },
                      if (wording.learnMoreUrl.isDefined) {
                        <.p(^.className := OperationIntroStyles.ctaWrapper)(
                          <.a(
                            ^.onClick := onClick,
                            ^.href := unescape(wording.learnMoreUrl.getOrElse("")),
                            ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                            ^.target := "_blank"
                          )(unescape(I18n.t("operation.intro.article.see-more.label")))
                        )
                      }
                    )
                  )
                )
              },
              <.style()(OperationIntroStyles.render[String], DynamicOperationIntroStyles.render[String])
            )
          }
      )
}

object OperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.black))

  val presentationInnerWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(
        padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`, ThemeStyles.SpacingValue.large.pxToEm())
      )
    )

  val titleWrapper: StyleA = style(maxWidth(470.pxToEm()), marginLeft.auto, marginRight.auto)
  val logoWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val infos: StyleA =
    style(width(100.%%), textAlign.center, backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent))

  val separator: StyleA =
    style(margin(ThemeStyles.SpacingValue.medium.pxToEm(), `0`, ThemeStyles.SpacingValue.small.pxToEm()))

  val separatorLineWrapper: StyleA =
    style(width(50.%%), paddingTop(2.pxToEm()))

  val separatorLine: StyleA =
    style(height(1.px), width(100.%%), backgroundColor(ThemeStyles.BorderColor.white), opacity(0.2))

  val separatorLineToTheLeft: StyleA = style(
    maxWidth(290.pxToEm()),
    marginLeft.auto,
    background := s"linear-gradient(to left, rgba(255,255,255,1) 0%, rgba(255,255,255,0) 100%)"
  )

  val separatorLineToTheRight: StyleA = style(
    maxWidth(290.pxToEm()),
    marginRight.auto,
    background := s"linear-gradient(to right, rgba(255,255,255,1) 0%, rgba(255,255,255,0) 100%)"
  )

  val separatorTextWrapper: StyleA = style(padding(`0`, 20.pxToEm()))

  val separatorText: StyleA = style(color(ThemeStyles.TextColor.white), opacity(0.5))

  val partnersList: StyleA = style(textAlign.center)

  val partnerItem: StyleA = style(
    display.inlineBlock,
    verticalAlign.middle,
    padding((ThemeStyles.SpacingValue.small / 2).pxToEm(), ThemeStyles.SpacingValue.small.pxToEm())
  )

  val partnerLogo: StyleA = style()

  val otherPartners: StyleA = style(textAlign.center, color(ThemeStyles.TextColor.white), opacity(0.5))

  val explanationWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent)
    )

  val explanationIll: StyleA =
    style(width(100.%%), ThemeStyles.MediaQueries.belowSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm())))

  val explanationTextWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val explanationText: StyleA = style(color(ThemeStyles.TextColor.white))

  val ctaWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))
}
