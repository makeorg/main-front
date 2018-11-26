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
import org.make.front.styles
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
      .createClass[ConsultationHeaderProps, Unit](displayName = "ConsultationHeader", componentDidMount = { _ =>
        AffixMethods.tabAffixScrollrAF
      }, render = {
        self =>
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
          }

          val startDateActions: Option[js.Date] = self.props.wrapped.operation.startDateActions

          <.header(^.className := js.Array(DynamicConsultationHeaderStyles.gradient))(
            <.div(
              ^.className := js
                .Array(
                  ConsultationHeaderStyles.titleWrapper,
                  ConsultationHeaderStyles.headerWrapper(!self.props.wrapped.operation.featureSettings.action)
                )
            )(self.props.wrapped.operation.operationTypeRibbon.map { consultationRibbon =>
              <.p(^.className := ConsultationHeaderStyles.labelWrapper)(
                <.span(^.className := TextStyles.label)(consultationRibbon)
              )
            }, <.ConsultationLogoComponent(^.wrapped := ConsultationLogoProps(consultation, self.props.wrapped.language))()),
            if (self.props.wrapped.operation.featureSettings.action) {
              <.div(^.id := "tabAffixContainer")(
                <.div(^.id := "tabAffixElement")(
                  <.div(^.className := ConsultationHeaderStyles.tabWrapper)(
                    <.button(
                      ^.className := js.Array(
                        ConsultationHeaderStyles.tab,
                        ConsultationHeaderStyles.tabSelection(self.props.wrapped.activeTab == "consultation")
                      ),
                      ^.onClick := changeTab("consultation")
                    )(
                      <.p(^.className := ConsultationHeaderStyles.titleLink)(
                        unescape(I18n.t("operation.intro.consultation"))
                      ),
                      <.p(^.className := ConsultationHeaderStyles.dateLink)(
                        unescape(I18n.t("operation.intro.from")) +
                          " " +
                          unescape(
                            I18n
                              .l(
                                consultation.startDate.getOrElse(new js.Date()),
                                DateLocalizeOptions("common.date.long")
                              )
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
              )
            },
            <.style()(ConsultationHeaderStyles.render[String], DynamicConsultationHeaderStyles.render[String])
          )
      })
}

object ConsultationHeaderStyles extends StyleSheet.Inline {
  import dsl._

  def headerWrapper: (Boolean) => StyleA =
    styleF.bool {
      case true  => styleS(paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()))
      case false => styleS(paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    }

  val titleWrapper: StyleA =
    style(
      marginRight.auto,
      marginLeft.auto,
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
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
      display.flex,
      maxWidth(ThemeStyles.containerMaxWidth),
      margin(`0`, auto),
      alignItems.flexEnd,
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val tab: StyleA =
    style(
      position.relative,
      width(100.%%),
      verticalAlign.bottom,
      padding(14.pxToEm(), 5.pxToEm(), 7.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.altGrey),
      textAlign.center,
      display.flex,
      flexFlow := "column",
      justifyContent.center,
      alignItems.center,
      ThemeStyles.MediaQueries.beyondLargeMedium(flexFlow := "row"),
      &.before(
        content := "''",
        position.absolute,
        top(-4.pxToEm()),
        left(`0`),
        width(100.%%),
        height(4.pxToEm()),
        backgroundColor(ThemeStyles.BackgroundColor.black)
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
      padding(`0`, 5.pxToEm(12)),
      ThemeStyles.MediaQueries.beyondMedium(fontSize(14.pxToEm()), padding(`0`, 5.pxToEm(14))),
    )

  val affixOn: StyleA =
    style(width(100.%%), left(`0`), top(`0`), zIndex(2), position.fixed, boxShadow := s"0 1px 5px rgba(0, 0, 0, 0.5)")
}
