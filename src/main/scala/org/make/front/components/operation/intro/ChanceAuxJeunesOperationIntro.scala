package org.make.front.components.operation.intro

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.models.{OperationExpanded => OperationModel, OperationPartner => OperationPartnerModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.language.postfixOps

object ChanceAuxJeunesOperationIntro {

  case class ChanceAuxJeunesOperationIntroProps(operation: OperationModel, language: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ChanceAuxJeunesOperationIntroProps, Unit](
        displayName = "ChanceAuxJeunesOperationIntro",
        render = (self) => {

          def onClick: () => Unit = { () =>
            TrackingService.track(
              "click-button-learn-more",
              TrackingContext(TrackingLocation.operationPage, Some(self.props.wrapped.operation.slug))
            )
          }

          val partners = Seq(
            OperationPartnerModel(name = "Bnp Paribas", imageUrl = bnpParibasLogo.toString, imageWidth = 123),
            OperationPartnerModel(name = "AccorHotels", imageUrl = accorHotelsLogoCAJ.toString, imageWidth = 127),
            OperationPartnerModel(name = "Google", imageUrl = googleLogo.toString, imageWidth = 80),
            OperationPartnerModel(name = "Hauts De France", imageUrl = hautsDeFranceLogo.toString, imageWidth = 50),
            OperationPartnerModel(name = "Viva Tech", imageUrl = vivaTechLogo.toString, imageWidth = 88)
          )

          <.div(^.className := Seq(OperationIntroStyles.wrapper, ChanceAuxJeunesOperationIntroStyles.wrapper))(
            <.img(
              ^.className := ChanceAuxJeunesOperationIntroStyles.illustration,
              ^.src := chanceAuxJeunesIll.toString,
              ^("srcset") := chanceAuxJeunesIllSmall.toString + " 400w, " + chanceAuxJeunesIllSmall2x.toString + " 800w, " + chanceAuxJeunesIllMedium.toString + " 840w, " + chanceAuxJeunesIllMedium2x.toString + " 1680w, " + chanceAuxJeunesIll.toString + " 1350w, " + chanceAuxJeunesIll2x.toString + " 2700w",
              ^.alt := I18n.t("operation.chance-aux-jeunes.intro.title"),
              ^("data-pin-no-hover") := "true"
            )(),
            <.div(^.className := Seq(OperationIntroStyles.headingWrapper, LayoutRulesStyles.centeredRow))(
              <.div(^.className := ChanceAuxJeunesOperationIntroStyles.logoWrapper)(
                <.p(^.className := OperationIntroStyles.labelWrapper)(
                  <.span(^.className := TextStyles.label)(unescape(I18n.t("operation.chance-aux-jeunes.intro.label")))
                ),
                <.img(
                  ^.src := chanceAuxJeunesLogo.toString,
                  ^.alt := unescape(I18n.t("operation.chance-aux-jeunes.intro.title"))
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
                      ChanceAuxJeunesOperationIntroStyles.separatorLine,
                      OperationIntroStyles.separatorLineToTheLeft,
                      ChanceAuxJeunesOperationIntroStyles.separatorLineToTheLeft
                    )
                  )()
                ),
                <.div(^.className := Seq(TableLayoutStyles.cell, OperationIntroStyles.separatorTextWrapper))(
                  <.p(^.className := Seq(OperationIntroStyles.separatorText, TextStyles.smallerText))(
                    unescape(I18n.t("operation.chance-aux-jeunes.intro.partners.intro"))
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
                      ChanceAuxJeunesOperationIntroStyles.separatorLine,
                      OperationIntroStyles.separatorLineToTheRight,
                      ChanceAuxJeunesOperationIntroStyles.separatorLineToTheRight
                    )
                  )()
                )
              ),
              <.ul(
                ^.className := Seq(OperationIntroStyles.partnersList, LayoutRulesStyles.narrowerCenteredRowWithCols)
              )(
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
            ),
            <.div(
              ^.className := Seq(
                OperationIntroStyles.explanationWrapper,
                ChanceAuxJeunesOperationIntroStyles.explanationWrapper
              )
            )(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRow)(
                <.p(^.className := TextStyles.label)(
                  unescape(I18n.t("operation.chance-aux-jeunes.intro.article.title"))
                ),
                <.div(^.className := OperationIntroStyles.explanationTextWrapper)(
                  <.p(^.className := Seq(OperationIntroStyles.explanationText, TextStyles.smallText))(
                    unescape(I18n.t("operation.chance-aux-jeunes.intro.article.text"))
                  )
                ),
                <.p(^.className := OperationIntroStyles.ctaWrapper)(
                  <.a(
                    ^.onClick := onClick,
                    ^.href := unescape(I18n.t("operation.chance-aux-jeunes.intro.article.see-more.link")),
                    ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                    ^.target := "_blank"
                  )(unescape(I18n.t("operation.chance-aux-jeunes.intro.article.see-more.label")))
                )
              )
            ),
            <.style()(OperationIntroStyles.render[String], ChanceAuxJeunesOperationIntroStyles.render[String])
          )
        }
      )
}

object ChanceAuxJeunesOperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, overflow.hidden)

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

  val logoWrapper: StyleA = style(maxWidth(446.pxToEm()), marginLeft.auto, marginRight.auto)

  val separatorLine: StyleA =
    style(backgroundColor(ThemeStyles.BorderColor.light))

  val separatorLineToTheLeft: StyleA = style(
    background := s"linear-gradient(to left, rgba(255,255,255,1) 0%, rgba(255,255,255,0) 100%)"
  )

  val separatorLineToTheRight: StyleA = style(
    background := s"linear-gradient(to right, rgba(255,255,255,1) 0%, rgba(255,255,255,0) 100%)"
  )

  val separatorText: StyleA = style(color(ThemeStyles.TextColor.light))

  val explanationWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.blackTransparent))
}
