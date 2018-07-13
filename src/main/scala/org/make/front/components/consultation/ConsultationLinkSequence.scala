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
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{GradientColor => GradientColorModel, OperationExpanded => OperationModel}
import org.make.front.styles.utils._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object ConsultationLinkSequence {

  case class ConsultationLinkSequenceProps(operation: OperationModel, country: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationLinkSequenceProps, Unit](
        displayName = "ConsultationLinkSequence",
        render = (self) => {
          val consultation: OperationModel = self.props.wrapped.operation

          val gradientValues: GradientColorModel =
            consultation.gradient.getOrElse(GradientColorModel("#ab92ca", "#ab92ca"))

          object DynamicConsultationLinkSequenceStyles extends StyleSheet.Inline {
            import dsl._

            val gradient: StyleA =
              style(background := s"linear-gradient(115deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          def trackingActions(): () => Unit = { () =>
            TrackingService
              .track(
                eventName = "click-sequence-open",
                trackingContext = TrackingContext(TrackingLocation.operationPage, operationSlug = Some(consultation.slug)),
                parameters = Map.empty,
                internalOnlyParameters = Map("sequenceId" -> consultation.landingSequenceId.value)
              )
          }

          <.aside(
            ^.className := js
              .Array(DynamicConsultationLinkSequenceStyles.gradient, ConsultationLinkSequenceStyles.wrapper)
          )(
            <.p(^.className := ConsultationLinkSequenceStyles.presentation)(
              unescape(I18n.t("operation.sequence.link.presentation"))
            ),
            <.div(^.className := ConsultationLinkSequenceStyles.sep)(),
            <.Link(
              ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA),
              ^.onClick := (trackingActions()),
              ^.to := s"/${self.props.wrapped.country}/consultation/${consultation.slug}/selection"
            )(
              <.i(^.className := js.Array(FontAwesomeStyles.play, ConsultationLinkSequenceStyles.ctaIcon))(),
              unescape(I18n.t("operation.sequence.link.cta"))
            ),
            <.style()(
              ConsultationLinkSequenceStyles.render[String],
              DynamicConsultationLinkSequenceStyles.render[String]
            )
          )
        }
      )
}

object ConsultationLinkSequenceStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      display.flex,
      alignItems.center,
      padding(40.pxToEm(), ThemeStyles.SpacingValue.smaller.pxToEm()),
      margin(20.pxToEm(), `0`)
    )

  val presentation: StyleA =
    style(
      ThemeStyles.Font.circularStdBold,
      fontSize(15.pxToEm()),
      color(ThemeStyles.TextColor.white),
      textAlign.right,
      width(475.pxToPercent(750)),
      paddingRight(15.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm())),
      ThemeStyles.MediaQueries.beyondLargeMedium(paddingLeft(120.pxToPercent(750)))
    )

  val sep: StyleA =
    style(
      width(2.pxToEm()),
      minHeight(75.pxToEm()),
      marginRight(15.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      opacity(.3)
    )

  val ctaIcon: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.small.pxToEm()))
}
