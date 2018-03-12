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

object MVEOperationIntro {

  case class MVEOperationIntroProps(operation: OperationModel, language: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[MVEOperationIntroProps, Unit](
        displayName = "MVEOperationIntro",
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
            OperationPartnerModel(name = "BlaBlaCar", imageUrl = blaBlaCarLogo.toString, imageWidth = 120),
            OperationPartnerModel(name = "Orange", imageUrl = orangeLogo.toString, imageWidth = 37),
            OperationPartnerModel(name = "AccorHotels", imageUrl = accorHotelsLogo.toString, imageWidth = 132),
            OperationPartnerModel(name = "Mairie de Paris", imageUrl = mairieDeParisLogo.toString, imageWidth = 146)
          )

          <.div(^.className := Seq(OperationIntroStyles.wrapper, MVEOperationIntroStyles.wrapper))(
            <.div(^.className := OperationIntroStyles.presentationInnerWrapper)(
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.div(^.className := Seq(OperationIntroStyles.titleWrapper, MVEOperationIntroStyles.titleWrapper))(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.mve.intro.label"))),
                  <.p(^.className := Seq(OperationIntroStyles.logoWrapper))(
                    <.img(^.src := mveLogo.toString, ^.alt := unescape(I18n.t("operation.mve.intro.title")))()
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
                          MVEOperationIntroStyles.separatorLine,
                          OperationIntroStyles.separatorLineToTheLeft,
                          MVEOperationIntroStyles.separatorLineToTheLeft
                        )
                      )()
                    ),
                    <.div(^.className := Seq(TableLayoutStyles.cell, OperationIntroStyles.separatorTextWrapper))(
                      <.p(
                        ^.className := Seq(
                          OperationIntroStyles.separatorText,
                          MVEOperationIntroStyles.separatorText,
                          TextStyles.smallerText
                        )
                      )(unescape(I18n.t("operation.mve.intro.partners.intro")))
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
                          MVEOperationIntroStyles.separatorLine,
                          OperationIntroStyles.separatorLineToTheRight,
                          MVEOperationIntroStyles.separatorLineToTheRight
                        )
                      )()
                    )
                  )
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
              ^.className := Seq(OperationIntroStyles.explanationWrapper, MVEOperationIntroStyles.explanationWrapper)
            )(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRow)(
                <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.mve.intro.article.title"))),
                <.div(^.className := OperationIntroStyles.explanationTextWrapper)(
                  <.p(^.className := Seq(OperationIntroStyles.explanationText, TextStyles.smallText))(
                    unescape(I18n.t("operation.mve.intro.article.text"))
                  )
                ),
                <.p(^.className := OperationIntroStyles.ctaWrapper)(
                  <.a(
                    ^.onClick := onClick,
                    ^.href := unescape(I18n.t("operation.mve.intro.article.see-more.link")),
                    ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                    ^.target := "_blank"
                  )(unescape(I18n.t("operation.mve.intro.article.see-more.label")))
                )
              )
            ),
            <.style()(OperationIntroStyles.render[String], MVEOperationIntroStyles.render[String])
          )
        }
      )
}

object MVEOperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      position.relative,
      background := s"linear-gradient(130deg, #f6dee3, #d5e7ff)",
      ThemeStyles.MediaQueries
        .beyondMedium(
          &.before(
            content := "' '",
            position.absolute,
            top(`0`),
            left(`0`),
            height(100.%%),
            width(25.%%),
            width :=! "calc(50% - 300px)",
            backgroundImage := s"url(${mveOldPeople.toString})",
            backgroundSize := s"auto ${400.pxToEm().value}",
            backgroundRepeat := "no-repeat",
            backgroundPosition := "100% 50%"
          ),
          &.after(
            content := "' '",
            position.absolute,
            top(`0`),
            right(`0`),
            height(100.%%),
            width(25.%%),
            width :=! "calc(50% - 300px)",
            backgroundImage := s"url(${mveYoungPeople.toString})",
            backgroundSize := s"auto ${400.pxToEm().value}",
            backgroundRepeat := "no-repeat",
            backgroundPosition := "0% 50%"
          )
        ),
      (ThemeStyles.MediaQueries.beyondMedium & media.all.resolution(192 dpi))(
        &.before(backgroundImage := s"url(${mveOldPeople2x.toString})!important"),
        &.after(backgroundImage := s"url(${mveYoungPeople2x.toString})!important")
      ),
      media.minWidth(1575.pxToEm())(
        /* TODO: find a way to preserve order/priorities of media queries */
        &.before(backgroundPosition := "0 50%!important"),
        &.after(backgroundPosition := "100% 50%!important")
      )
    )

  val titleWrapper: StyleA = style(maxWidth(480.pxToEm()))

  val separatorLine: StyleA =
    style(backgroundColor(ThemeStyles.BorderColor.light))

  val separatorLineToTheLeft: StyleA = style(
    background := s"linear-gradient(to left, rgba(0,0,0,1) 0%, rgba(0,0,0,0) 100%)"
  )

  val separatorLineToTheRight: StyleA = style(
    background := s"linear-gradient(to right, rgba(0,0,0,1) 0%, rgba(0,0,0,0) 100%)"
  )

  val separatorText: StyleA = style(color(ThemeStyles.TextColor.light))

  val explanationWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.blackTransparent))
}
