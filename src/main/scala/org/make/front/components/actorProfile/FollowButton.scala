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

package org.make.front.components.actorProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.actorProfile.FollowButtonContainer.ShowModal
import org.make.front.components.authenticate.LoginOrRegister.LoginOrRegisterProps
import org.make.front.components.modals.Modal.ModalProps
import org.make.front.facades.{I18n, Replacements}
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext
import scalacss.internal.mutable.StyleSheet

import scala.scalajs.js

object FollowButton {

  final case class FollowButtonProps(isFollowedByUser: Boolean, triggerFollowToggle: () => ShowModal, actorName: String)

  final case class FollowButtonState(isAuthenticateModalOpened: Boolean)

  val reactClass: ReactClass =
    React
      .createClass[FollowButtonProps, FollowButtonState](
        displayName = "FollowButton",
        getInitialState = { _ =>
          FollowButtonState(isAuthenticateModalOpened = false)
        },
        render = self => {
          def submit: () => Unit = { () =>
            val showModal: ShowModal = self.props.wrapped.triggerFollowToggle()
            self.setState(_.copy(isAuthenticateModalOpened = showModal.value))
          }

          <.div(^.className := FollowButtonStyles.wrapper)(
            <.button(
              ^.className := js.Array(
                CTAStyles.basic,
                CTAStyles.basicOnA,
                FollowButtonStyles.button,
                FollowButtonStyles.activeButton(self.props.wrapped.isFollowedByUser)
              ),
              ^.onClick := submit
            )(if (self.props.wrapped.isFollowedByUser) {
              <("ButtonWithIcon")()(
                <.i(^.className := js.Array(FollowButtonStyles.icon, FontAwesomeStyles.check))(),
                I18n.t("actor-profile.followed")
              )
            } else {
              I18n.t("actor-profile.follow")
            }),
            <.ModalComponent(
              ^.wrapped := ModalProps(
                isModalOpened = self.state.isAuthenticateModalOpened,
                closeCallback = () => self.setState(_.copy(isAuthenticateModalOpened = false))
              )
            )(
              <.LoginOrRegisterComponent(
                ^.wrapped := LoginOrRegisterProps(
                  displayView = "register",
                  trackingContext = TrackingContext(TrackingLocation.triggerFromFollow, None),
                  trackingParameters = Map.empty,
                  trackingInternalOnlyParameters = Map.empty,
                  onSuccessfulLogin = () => {
                    self.setState(_.copy(isAuthenticateModalOpened = false))
                    self.props.wrapped.triggerFollowToggle()
                  },
                  registerTitle = Some(
                    unescape(
                      I18n.t(
                        "actor-profile.contributions.title",
                        replacements = Replacements(("actor-name", self.props.wrapped.actorName))
                      )
                    )
                  )
                )
              )()
            ),
            <.style()(FollowButtonStyles.render[String])
          )
        }
      )
}

object FollowButtonStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(display.flex, justifyContent.center, margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`))

  val button: StyleA =
    style(
      display.flex,
      justifyContent.center,
      width(100.%%),
      ThemeStyles.MediaQueries.beyondLargeMedium(maxWidth(280.pxToEm()))
    )

  val icon: StyleA =
    style(marginRight(5.pxToEm()))

  val activeButton: Boolean => StyleA = styleF.bool(
    isFollowedByUser =>
      if (isFollowedByUser) {
        styleS(color(ThemeStyles.TextColor.lighter), backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent))
      } else {
        styleS()
    }
  )

}
