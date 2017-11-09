package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.models.{GradientColor => GradientColorModel, Operation => OperationModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TableLayoutStyles, TextStyles}
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

          val operation: OperationModel =
            self.props.wrapped.operation

          val gradient: GradientColorModel = operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          final case class PartnerModel(name: String, imageUrl: String, imageWidth: Int)

          val partners: Seq[PartnerModel] = Seq(
            PartnerModel(name = "Expedia", imageUrl = expediaLogo.toString, imageWidth = 96),
            PartnerModel(name = "L'Oréal", imageUrl = lOrealLogo.toString, imageWidth = 89),
            PartnerModel(name = "Axa", imageUrl = axaLogo.toString, imageWidth = 30),
            PartnerModel(name = "Île de France", imageUrl = ileDeFranceLogo.toString, imageWidth = 98),
            PartnerModel(name = "Elle", imageUrl = elleLogo.toString, imageWidth = 55),
            PartnerModel(name = "Women's Forum", imageUrl = womenSForumLogo.toString, imageWidth = 118)
          )

          <.div(
            ^.className := Seq(VFFIntroStyles.wrapper, VFFIntroStyles.gradientBackground(gradient.from, gradient.to))
          )(
            <.div(^.className := VFFIntroStyles.presentationInnerWrapper)(
              <.div(^.className := RowRulesStyles.centeredRow)(
                <.div(^.className := ColRulesStyles.col)(
                  <.div(^.className := VFFIntroStyles.titleWrapper)(
                    <.p(^.className := Seq(TextStyles.label))(unescape(I18n.t("operation.vff-header.label"))),
                    <.p(^.className := Seq(VFFIntroStyles.logoWrapper))(
                      <.img(^.src := vffLogo.toString, ^.alt := unescape(I18n.t("operation.vff-header.title")))()
                    ),
                    <.p(^.className := Seq(VFFIntroStyles.infos, TextStyles.label))(
                      unescape(I18n.t("operation.vff-header.period"))
                    )
                  ),
                  <.div(^.className := Seq(TableLayoutStyles.wrapper, VFFIntroStyles.separatorWrapper))(
                    <.div(
                      ^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, VFFIntroStyles.separatorLineWrapper)
                    )(<.hr(^.className := Seq(VFFIntroStyles.separatorLine, VFFIntroStyles.separatorLineToTheLeft))()),
                    <.div(^.className := Seq(TableLayoutStyles.cell, VFFIntroStyles.separatorTextWrapper))(
                      <.p(^.className := Seq(VFFIntroStyles.separator, TextStyles.smallerText))(
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
                  )
                )
              )
            ),
            <.div(^.className := VFFIntroStyles.explanationWrapper)(
              <.div(^.className := RowRulesStyles.narrowerCenteredRow)(
                <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colThirdBeyondSmall))(
                  <.img(
                    ^.src := vffIll.toString,
                    ^.alt := unescape(I18n.t("operation.vff-header.title")),
                    ^.className := VFFIntroStyles.explanationIll
                  )()
                ),
                <.div(^.className := Seq(ColRulesStyles.col, ColRulesStyles.colTwoThirdsBeyondSmall))(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.vff-header.article.title"))),
                  <.p(^.className := Seq(VFFIntroStyles.explanation, TextStyles.smallText))(
                    unescape(I18n.t("operation.vff-header.article.content"))
                  ),
                  <.p(^.className := VFFIntroStyles.ctaWrapper)(
                    <.a(
                      ^.href := I18n.t("operation.vff-header.article.see-more.link"),
                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                      ^.target := "_blank"
                    )(unescape(I18n.t("operation.vff-header.article.see-more.label")))
                  )
                )
              )
            ),
            <.style()(VFFIntroStyles.render[String])
          )
        }
      )
}

object VFFIntroStyles extends StyleSheet.Inline {

  import dsl._

  def gradientBackground(from: String, to: String): StyleA =
    style(background := s"linear-gradient(130deg, $from, $to)")

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.black))

  val presentationInnerWrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(ThemeStyles.SpacingValue.larger.pxToEm())),
      paddingBottom(ThemeStyles.SpacingValue.small.pxToEm())
    )

  val titleWrapper: StyleA = style(maxWidth(470.pxToEm()), marginLeft.auto, marginRight.auto)
  val logoWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val infos: StyleA =
    style(width(100.%%), textAlign.center, backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent))

  val separatorWrapper: StyleA =
    style(margin(ThemeStyles.SpacingValue.medium.pxToEm(), `0`, ThemeStyles.SpacingValue.small.pxToEm()), opacity(0.5))

  val separatorLineWrapper: StyleA =
    style(width(50.%%), paddingTop(2.pxToEm()))

  val separatorLine: StyleA =
    style(height(1.px), width(100.%%), margin(`0`), border.none, backgroundColor(ThemeStyles.BorderColor.white))

  val separatorLineToTheLeft: StyleA = style(
    maxWidth(290.pxToEm()),
    marginLeft.auto,
    background := s"linear-gradient(to left, rgba(255,255,255,1) 0%,rgba(255,255,255,0) 100%)"
  )

  val separatorLineToTheRight: StyleA = style(
    maxWidth(290.pxToEm()),
    marginRight.auto,
    background := s"linear-gradient(to right, rgba(255,255,255,1) 0%,rgba(255,255,255,0) 100%)"
  )

  val separatorTextWrapper: StyleA = style(padding(`0`, 20.pxToEm()))

  val separator: StyleA = style(color(ThemeStyles.TextColor.white))

  val partnersList: StyleA = style(textAlign.center)

  val partnerItem: StyleA = style(
    display.inlineBlock,
    verticalAlign.middle,
    padding(ThemeStyles.SpacingValue.small.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(
      paddingLeft((ThemeStyles.SpacingValue.largerMedium / 2).pxToEm()),
      paddingRight((ThemeStyles.SpacingValue.largerMedium / 2).pxToEm())
    )
  )

  val partnerLogo: StyleA = style()

  val explanationWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent)
    )

  val explanationIll: StyleA =
    style(width(100.%%), ThemeStyles.MediaQueries.belowSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm())))

  val explanation: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.small.pxToEm(13)),
    ThemeStyles.MediaQueries.beyondSmall(marginTop(ThemeStyles.SpacingValue.small.pxToEm(16))),
    color(ThemeStyles.TextColor.white)
  )

  val ctaWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))
}
