package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.consultation.ConsultationLogo.ConsultationLogoProps
import org.make.front.facades.Localize.DateLocalizeOptions
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.models.{GradientColor => GradientColorModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._

import scala.scalajs.js

object ConsultationHeader {

  case class ConsultationHeaderState(activeTab: String)
  case class ConsultationHeaderProps(operation: OperationModel,
                                     language: String,
                                     activeTab: String,
                                     countryCode: String)

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ConsultationHeaderProps, ConsultationHeaderState](
          displayName = "ConsultationHeader",
          getInitialState = { self =>
            ConsultationHeaderState(self.props.wrapped.activeTab)
          },
          render = { self =>
            def changeTab(newTab: String): () => Unit = { () =>
              self.props.history.replace(
                s"/${self.props.wrapped.countryCode}/consultation/${self.props.wrapped.operation.slug}/$newTab"
              )
            }

            val consultation: OperationModel = self.props.wrapped.operation

            val gradientValues: GradientColorModel =
              consultation.gradient.getOrElse(GradientColorModel("#ab92ca", "#ab92ca"))

            object DynamicConsultationHeaderStyles extends StyleSheet.Inline {
              import dsl._

              val gradient: StyleA =
                style(background := s"linear-gradient(115deg, ${gradientValues.from}, ${gradientValues.to})")

              val titleWidth: StyleA =
                style(maxWidth(consultation.logoWidth.pxToEm()))
            }

            val startDateActions: Option[js.Date] = self.props.wrapped.operation.startDateActions

            <.header(^.className := DynamicConsultationHeaderStyles.gradient)(
              <.div(
                ^.className := js
                  .Array(ConsultationHeaderStyles.titleWrapper, DynamicConsultationHeaderStyles.titleWidth)
              )(
                <.p(^.className := ConsultationHeaderStyles.labelWrapper)(
                  <.span(^.className := TextStyles.label)(unescape(I18n.t("operation.vff-fr.intro.label")))
                ),
                <.ConsultationLogoComponent(
                  ^.wrapped := ConsultationLogoProps(consultation, self.props.wrapped.language)
                )()
              ),
              //todo: Check translation with product team
              <.div(^.className := js.Array(LayoutRulesStyles.centeredRow, ConsultationHeaderStyles.tabWrapper))(
                <.button(
                  ^.className := js.Array(
                    ConsultationHeaderStyles.tab,
                    ConsultationHeaderStyles.tabSelection(self.state.activeTab == "consultation")
                  ),
                  ^.onClick := changeTab("consultation")
                )(
                  <.span(^.className := ConsultationHeaderStyles.titleLink)(
                    unescape(I18n.t("operation.intro.consultation"))
                  ),
                  <.span(^.className := ConsultationHeaderStyles.dateLink)(
                    unescape(I18n.t("operation.intro.from")) +
                      " " +
                      unescape(
                        I18n.l(consultation.startDate.getOrElse(new js.Date()), DateLocalizeOptions("common.date.long"))
                      ) +
                      " " +
                      unescape(I18n.t("operation.intro.to")) +
                      " " +
                      unescape(
                        I18n.l(consultation.endDate.getOrElse(new js.Date()), DateLocalizeOptions("common.date.long"))
                      )
                  )
                ),
                <.button(
                  ^.className := js.Array(
                    ConsultationHeaderStyles.tab,
                    ConsultationHeaderStyles.tabSelection(self.state.activeTab != "consultation")
                  ),
                  ^.onClick := changeTab("actions")
                )(
                  <.span(^.className := ConsultationHeaderStyles.titleLink)(unescape(I18n.t("operation.intro.action"))),
                  startDateActions.map { date =>
                    <.span(^.className := ConsultationHeaderStyles.dateLink)(
                      unescape(I18n.t("operation.intro.datefrom")) + " " +
                        unescape(I18n.l(date, DateLocalizeOptions("common.date.long")))
                    )
                  }
                )
              ),
              <.style()(ConsultationHeaderStyles.render[String], DynamicConsultationHeaderStyles.render[String])
            )
          }
        )
    )
}

object ConsultationHeaderStyles extends StyleSheet.Inline {
  import dsl._

  val titleWrapper: StyleA =
    style(
      marginRight.auto,
      marginLeft.auto,
      paddingTop(30.pxToEm()),
      paddingBottom(10.pxToEm()),
      paddingRight(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val labelWrapper: StyleA =
    style(marginBottom(15.pxToEm()))

  val tabWrapper: StyleA =
    style(display.flex)

  val tab: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      width(50.%%),
      paddingTop(20.pxToEm()),
      paddingBottom(20.pxToEm()),
      textAlign.center,
      color(ThemeStyles.TextColor.white),
      textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)",
      unsafeChild("span")(display.block),
      ThemeStyles.MediaQueries
        .beyondLarge(unsafeChild("span")(display.inlineBlock, paddingLeft(5.pxToEm()), paddingRight(5.pxToEm()))),
      &.before(
        content := "''",
        position.absolute,
        bottom(-4.pxToEm()),
        left(`0`),
        width(100.%%),
        height(4.pxToEm()),
        backgroundColor(ThemeStyles.BackgroundColor.black),
        transition := "opacity .2s ease-in-out",
      )
    )

  val tabSelection: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(&.before(opacity(1)))
      } else styleS(&.before(opacity(0)))
  )

  val titleLink: StyleA =
    style(TextStyles.title, fontSize(16.pxToEm()), ThemeStyles.MediaQueries.beyondMedium(fontSize(18.pxToEm())))

  val dateLink: StyleA =
    style(
      ThemeStyles.Font.circularStdBook,
      fontSize(12.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(fontSize(14.pxToEm())),
    )
}
