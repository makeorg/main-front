package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.models.{GradientColor => GradientColorModel, Operation => OperationModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.Main.CssSettings._

object VFFIntro {

  case class VFFIntroProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[VFFIntroProps, Unit](
        displayName = "VFFIntro",
        render = (self) => {

          def onClick: () => Unit = { () =>
            FacebookPixel.fbq("trackCustom", "click-button-learn-more")
          }

          val operation: OperationModel =
            self.props.wrapped.operation

          val gradient: GradientColorModel = operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          final case class PartnerModel(name: String, imageUrl: String, imageWidth: Int)

          val partners: Seq[PartnerModel] = Seq(
            PartnerModel(name = "Kering Foundation", imageUrl = keringFoundationLogo.toString, imageWidth = 80),
            PartnerModel(name = "Facebook", imageUrl = facebookLogo.toString, imageWidth = 80)
          )

          val gradientValues: GradientColorModel =
            self.props.wrapped.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicVFFIntroStyles extends StyleSheet.Inline {
            import dsl._

            val gradient = style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(^.className := Seq(VFFIntroStyles.wrapper, DynamicVFFIntroStyles.gradient))(
            <.div(^.className := VFFIntroStyles.presentationInnerWrapper)(
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.div(^.className := VFFIntroStyles.titleWrapper)(
                  <.p(^.className := Seq(TextStyles.label))(unescape(I18n.t("operation.vff-header.label"))),
                  <.p(^.className := Seq(VFFIntroStyles.logoWrapper))(
                    <.img(
                      ^.src := self.props.wrapped.operation.logoUrl.getOrElse(""),
                      ^.alt := unescape(I18n.t("operation.vff-header.title"))
                    )()
                  ),
                  <.p(^.className := Seq(VFFIntroStyles.infos, TextStyles.label))(
                    unescape(I18n.t("operation.vff-header.period"))
                  )
                ),
                <.div(^.className := Seq(TableLayoutStyles.wrapper, VFFIntroStyles.separator))(
                  <.div(
                    ^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, VFFIntroStyles.separatorLineWrapper)
                  )(<.hr(^.className := Seq(VFFIntroStyles.separatorLine, VFFIntroStyles.separatorLineToTheLeft))()),
                  <.div(^.className := Seq(TableLayoutStyles.cell, VFFIntroStyles.separatorTextWrapper))(
                    <.p(^.className := Seq(VFFIntroStyles.separatorText, TextStyles.smallerText))(
                      unescape(I18n.t("operation.vff-header.partners.intro"))
                    )
                  ),
                  <.div(
                    ^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, VFFIntroStyles.separatorLineWrapper)
                  )(<.hr(^.className := Seq(VFFIntroStyles.separatorLine, VFFIntroStyles.separatorLineToTheRight))())
                ),
                <.ul(^.className := VFFIntroStyles.partnersList)(
                  partners.map(
                    partner =>
                      <.li(^.className := VFFIntroStyles.partnerItem)(
                        <.img(
                          ^.src := partner.imageUrl,
                          ^.alt := partner.name,
                          ^("width") := partner.imageWidth.toString,
                          ^.className := VFFIntroStyles.partnerLogo
                        )()
                    )
                  )
                ),
                <.p(^.className := Seq(VFFIntroStyles.otherPartners, TextStyles.smallText))(
                  unescape(I18n.t("operation.vff-header.partners.others"))
                )
              )
            ),
            <.div(^.className := VFFIntroStyles.explanationWrapper)(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRowWithCols)(
                <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colThirdBeyondSmall))(
                  <.img(
                    ^.src := VFFIll.toString,
                    ^.alt := unescape(I18n.t("operation.vff-header.title")),
                    ^.className := VFFIntroStyles.explanationIll
                  )()
                ),
                <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colTwoThirdsBeyondSmall))(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.vff-header.article.title"))),
                  <.div(^.className := VFFIntroStyles.explanationTextWrapper)(
                    <.p(^.className := Seq(VFFIntroStyles.explanationText, TextStyles.smallText))(
                      unescape(I18n.t("operation.vff-header.article.content"))
                    )
                  ),
                  <.p(^.className := VFFIntroStyles.ctaWrapper)(
                    <.a(
                      ^.onClick := onClick,
                      ^.href := I18n.t("operation.vff-header.article.see-more.link"),
                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                      ^.target := "_blank"
                    )(unescape(I18n.t("operation.vff-header.article.see-more.label")))
                  )
                )
              )
            ),
            <.style()(VFFIntroStyles.render[String], DynamicVFFIntroStyles.render[String])
          )
        }
      )
}

object VFFIntroStyles extends StyleSheet.Inline {

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
    style(
      height(1.px),
      width(100.%%),
      margin(`0`),
      border.none,
      backgroundColor(ThemeStyles.BorderColor.white),
      opacity(0.2)
    )

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
