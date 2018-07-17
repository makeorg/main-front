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
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.share.ShareMobile.ShareMobileProps
import org.make.front.facades.close
import org.make.front.models.{OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.RWDRulesLargeMediumStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object ConsultationShareMobile {

  case class ConsultationShareMobileState(isOpen: Boolean)
  case class ConsultationShareMobileProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationShareMobileProps, ConsultationShareMobileState](
        displayName = "ConsultationShareMobile",
        getInitialState = { self =>
          ConsultationShareMobileState(false)
        },
        render = { self =>
          def toggleSharing(): () => Unit = { () =>
            self.setState(_.copy(isOpen = !self.state.isOpen))
            TrackingService
              .track(
                eventName = "click-open-share-sequence",
                trackingContext = TrackingContext(TrackingLocation.operationPage, operationSlug = Some(self.props.wrapped.operation.slug)),
                internalOnlyParameters = Map("sequenceId" -> self.props.wrapped.operation.landingSequenceId.value)
              )
          }

          <.aside(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(
            <.button(
              ^.className := js.Array(
                ConsultationShareMobileStyles.shareTrigger,
                ConsultationShareMobileStyles.ctaTrigged(self.state.isOpen)
              ),
              ^.onClick := toggleSharing()
            )(
              <.i(
                ^.className := js.Array(
                  FontAwesomeStyles.share,
                  ConsultationShareMobileStyles.icon,
                  ConsultationShareMobileStyles.shareIcon,
                  ConsultationShareMobileStyles.shareIconTrigged(self.state.isOpen)
                )
              )(),
              <.img(
                ^.className := js.Array(
                  FontAwesomeStyles.share,
                  ConsultationShareMobileStyles.icon,
                  ConsultationShareMobileStyles.closeIcon,
                  ConsultationShareMobileStyles.closeIconTrigged(self.state.isOpen)
                ),
                ^.src := close.toString
              )()
            ),
            <.div(
              ^.className := js.Array(
                ConsultationShareMobileStyles.contentWrapper,
                ConsultationShareMobileStyles.wrapperOpening(self.state.isOpen)
              )
            )(<.ShareMobileComponent(^.wrapped := ShareMobileProps(operation = self.props.wrapped.operation))()),
            <.style()(ConsultationShareMobileStyles.render[String])
          )
        }
      )

}

object ConsultationShareMobileStyles extends StyleSheet.Inline {
  import dsl._

  val shareTrigger: StyleA =
    style(
      verticalAlign.middle,
      textAlign.center,
      position.fixed,
      zIndex(8),
      bottom(25.pxToEm()),
      right(25.pxToEm()),
      width(56.pxToEm()),
      height(56.pxToEm()),
      borderRadius(56.pxToEm()),
      boxShadow := s"0 0 14px 0 rgba(0, 0, 0, 0.3)",
      transition := "all .25s ease-in-out"
    )

  val ctaTrigged: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(backgroundColor(ThemeStyles.BackgroundColor.white))
      } else styleS(backgroundColor(ThemeStyles.ThemeColor.primary))
  )

  val icon: StyleA =
    style(
      position.absolute,
      fontSize(25.pxToEm()),
      width(25.pxToEm(25)),
      height(25.pxToEm(25)),
      top(50.%%),
      left(50.%%),
      marginTop(-(25 / 2).pxToEm(25)),
      marginLeft(-(25 / 2).pxToEm(25)),
      transition := "all .25s ease-in-out"
    )

  val shareIcon: StyleA =
    style(color(ThemeStyles.BackgroundColor.white))

  val shareIconTrigged: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(opacity(0), transform := s"scaleY(0)")
      } else styleS(opacity(1), transform := s"scaleY(1)")
  )

  val closeIcon: StyleA =
    style(color(ThemeStyles.BackgroundColor.black))

  val closeIconTrigged: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(opacity(1), transform := s"scaleY(1)")
      } else styleS(opacity(0), transform := s"scaleY(0)")
  )

  val contentWrapper: StyleA =
    style(
      position.fixed,
      zIndex(7),
      width(100.vw),
      height(100.vh),
      bottom(`0`),
      backgroundColor(rgba(0, 0, 0, 0.8)),
      transition := "all .25s ease-in-out"
    )

  val wrapperOpening: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(left(`0`), transform := s"scaleX(1)")
      } else styleS(left(100.vw), transform := s"scaleX(0)")
  )

}
