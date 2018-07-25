/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
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
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object ConsultationHeader {

  case class ConsultationHeaderProps(operation: OperationModel,
                                     changeActiveTab: String => Unit,
                                     language: String,
                                     activeTab: String,
                                     countryCode: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationHeaderProps, Unit](
        displayName = "ConsultationHeader",
        componentDidMount = { _ =>
          AffixMethods.tabAffixScrollrAF
        },
        render = { self =>
          def changeTab(newTab: String): () => Unit = { () =>
            self.props.wrapped.changeActiveTab(newTab)
            TrackingService
              .track(
                eventName = "click-" + newTab + "-tab",
                trackingContext = TrackingContext(
                  TrackingLocation.operationPage,
                  operationSlug = Some(self.props.wrapped.operation.slug)
                )
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
            <.div(^.id := "tabAffixContainer")(
              <.div(^.id := "tabAffixElement")(
                <.div(^.className := js.Array(LayoutRulesStyles.centeredRow, ConsultationHeaderStyles.tabWrapper))(
                  <.button(
                    ^.className := js.Array(
                      ConsultationHeaderStyles.tab,
                      ConsultationHeaderStyles.tabSelection(self.props.wrapped.activeTab == "consultation")
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
                          I18n
                            .l(consultation.startDate.getOrElse(new js.Date()), DateLocalizeOptions("common.date.long"))
                        ) +
                        " " +
                        unescape(I18n.t("operation.intro.to")) +
                        " " +
                        unescape(
                          I18n
                            .l(consultation.endDate.getOrElse(new js.Date()), DateLocalizeOptions("common.date.long"))
                        )
                    )
                  ),
                  <.button(
                    ^.className := js.Array(
                      ConsultationHeaderStyles.tab,
                      ConsultationHeaderStyles.tabSelection(self.props.wrapped.activeTab != "consultation")
                    ),
                    ^.onClick := changeTab("actions")
                  )(
                    <.span(^.className := ConsultationHeaderStyles.titleLink)(
                      unescape(I18n.t("operation.intro.action"))
                    ),
                    startDateActions.map { date =>
                      <.span(^.className := ConsultationHeaderStyles.dateLink)(
                        unescape(I18n.t("operation.intro.datefrom")) + " " +
                          unescape(I18n.l(date, DateLocalizeOptions("common.date.long")))
                      )
                    }
                  )
                )
              )
            ),
            <.style()(ConsultationHeaderStyles.render[String], DynamicConsultationHeaderStyles.render[String])
          )
        }
      )
}

object ConsultationHeaderStyles extends StyleSheet.Inline {
  import dsl._

  val titleWrapper: StyleA =
    style(
      marginRight.auto,
      marginLeft.auto,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingTop(30.pxToEm()),
      paddingBottom(5.pxToEm()),
      paddingRight(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val labelWrapper: StyleA =
    style(textAlign.center, marginBottom(15.pxToEm()))

  val tabWrapper: StyleA =
    style(
      padding(`0`),
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val tab: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      verticalAlign.bottom,
      width :=! "calc(50% - 1px)",
      paddingTop(14.pxToEm()),
      paddingBottom(7.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.altGrey),
      textAlign.center,
      textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)",
      unsafeChild("span")(display.block),
      ThemeStyles.MediaQueries
        .beyondLarge(unsafeChild("span")(display.inlineBlock, paddingLeft(5.pxToEm()), paddingRight(5.pxToEm()))),
      &.before(
        content := "''",
        position.absolute,
        top(-4.pxToEm()),
        left(`0`),
        width(100.%%),
        height(4.pxToEm()),
        backgroundColor(ThemeStyles.BackgroundColor.black),
      )
    )

  val tabSelection: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(
          paddingTop(19.pxToEm()),
          backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
          borderBottom(1.pxToEm(), solid, ThemeStyles.BackgroundColor.lightGrey),
          &.before(opacity(1))
        )
      } else styleS(border(1.pxToEm(), solid, ThemeStyles.BackgroundColor.black), &.before(opacity(0)))
  )

  val titleLink: StyleA =
    style(TextStyles.title, fontSize(16.pxToEm()), ThemeStyles.MediaQueries.beyondMedium(fontSize(18.pxToEm())))

  val dateLink: StyleA =
    style(
      ThemeStyles.Font.circularStdBook,
      fontSize(12.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(fontSize(14.pxToEm()))
    )

  val affixOn: StyleA =
    style(width(100.%%), left(`0`), top(`0`), zIndex(2), position.fixed, boxShadow := s"0 1px 5px rgba(0, 0, 0, 0.5)")
}
