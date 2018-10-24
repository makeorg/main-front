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

package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.AuthenticateWithFacebookContainer.AuthenticateWithFacebookContainerProps
import org.make.front.components.authenticate.LoginOrRegister.LoginOrRegisterProps
import org.make.front.components.modals.Modal.ModalProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{SequenceId, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object PromptingToConnect {

  final case class PromptingToConnectProps(operation: OperationModel,
                                           trackingContext: TrackingContext,
                                           trackingParameters: Map[String, String],
                                           trackingInternalOnlyParameters: Map[String, String],
                                           sequenceId: SequenceId,
                                           clickOnButtonHandler: () => Unit,
                                           authenticateHandler: ()  => Unit,
                                           language: String,
                                           registerTitle: Option[String],
                                           nextCta: Option[String])

  final case class PromptingToConnectState(isAuthenticateModalOpened: Boolean, loginOrRegisterView: String = "login")

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToConnectProps, PromptingToConnectState](
        displayName = "PromptingToConnect",
        getInitialState = { _ =>
          PromptingToConnectState(isAuthenticateModalOpened = false)
        },
        render = { self =>
          val defaultTrackingInternalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters ++ Map(
            "sequenceId" -> self.props.wrapped.sequenceId.value
          )

          def openLoginAuthenticateModal() = () => {
            self.setState(state => state.copy(isAuthenticateModalOpened = true, loginOrRegisterView = "login"))
            TrackingService
              .track(
                eventName = "click-sign-up-card-sign-in",
                trackingContext = self.props.wrapped.trackingContext,
                parameters = self.props.wrapped.trackingParameters,
                internalOnlyParameters = defaultTrackingInternalOnlyParameters
              )
          }

          def openRegisterAuthenticateModal() = () => {
            TrackingService
              .track(
                eventName = "click-sign-up-card-email",
                trackingContext = self.props.wrapped.trackingContext,
                parameters = self.props.wrapped.trackingParameters,
                internalOnlyParameters = defaultTrackingInternalOnlyParameters
              )
            self.setState(state => state.copy(isAuthenticateModalOpened = true, loginOrRegisterView = "register"))
          }

          def toggleAuthenticateModal() = () => {
            self.setState(state => state.copy(isAuthenticateModalOpened = !self.state.isAuthenticateModalOpened))
          }

          val skipSignup: () => Unit = { () =>
            TrackingService.track(
              eventName = "skip-sign-up-card",
              trackingContext = self.props.wrapped.trackingContext,
              parameters = self.props.wrapped.trackingParameters,
              internalOnlyParameters = defaultTrackingInternalOnlyParameters
            )
            self.props.wrapped.clickOnButtonHandler()
          }

          <.div(^.className := PromptingToConnectStyles.wrapper)(
            <.div(^.className := js.Array(LayoutRulesStyles.row, PromptingToConnectStyles.titleWrapper))(
              <.p(^.className := js.Array(TextStyles.bigText, TextStyles.boldText))(
                self.props.wrapped.registerTitle.getOrElse(unescape(I18n.t("sequence.prompting-to-connect.title")))
              )
            ),
            <.div(^.className := LayoutRulesStyles.evenNarrowerCenteredRow)(
              <.div(^.className := PromptingToConnectStyles.introWrapper)(
                <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("sequence.prompting-to-connect.intro")))
              ),
              <.ul()(
                <.li(^.className := PromptingToConnectStyles.facebookConnectButtonWrapper)(
                  <.AuthenticateWithFacebookContainerComponent(
                    ^.wrapped := AuthenticateWithFacebookContainerProps(
                      trackingContext = self.props.wrapped.trackingContext,
                      trackingParameters = self.props.wrapped.trackingParameters,
                      trackingInternalOnlyParameters = defaultTrackingInternalOnlyParameters,
                      onSuccessfulLogin = () => {
                        self.props.wrapped.authenticateHandler()
                      },
                      operationId = Some(self.props.wrapped.operation.operationId)
                    )
                  )()
                ),
                <.li(^.className := PromptingToConnectStyles.mailConnectButtonWrapper)(
                  <.button(
                    ^.className := js.Array(PromptingToConnectStyles.cta, CTAStyles.basic, CTAStyles.basicOnButton),
                    ^.onClick := openRegisterAuthenticateModal
                  )(
                    <.i(^.className := js.Array(FontAwesomeStyles.envelopeTransparent))(),
                    unescape("&nbsp;" + I18n.t("sequence.prompting-to-connect.authenticate-with-email-cta"))
                  )
                )
              ),
              <.div(^.className := PromptingToConnectStyles.loginScreenAccessWrapper)(
                <.p(^.className := js.Array(PromptingToConnectStyles.loginScreenAccess, TextStyles.smallText))(
                  unescape(I18n.t("sequence.prompting-to-connect.login-screen-access.intro") + " "),
                  <.button(^.className := TextStyles.boldText, ^.onClick := openLoginAuthenticateModal)(
                    unescape(I18n.t("sequence.prompting-to-connect.login-screen-access.link-support"))
                  )
                )
              ),
              <.ModalComponent(
                ^.wrapped := ModalProps(
                  isModalOpened = self.state.isAuthenticateModalOpened,
                  closeCallback = toggleAuthenticateModal()
                )
              )(
                <.LoginOrRegisterComponent(
                  ^.wrapped := LoginOrRegisterProps(
                    trackingContext = self.props.wrapped.trackingContext,
                    trackingParameters = self.props.wrapped.trackingParameters,
                    trackingInternalOnlyParameters = defaultTrackingInternalOnlyParameters,
                    displayView = self.state.loginOrRegisterView,
                    onSuccessfulLogin = () => {
                      self.setState(_.copy(isAuthenticateModalOpened = false))
                      self.props.wrapped.authenticateHandler()
                    },
                    operationId = Some(self.props.wrapped.operation.operationId),
                    registerTitle = self.props.wrapped.operation.wordings
                      .find(_.language == self.props.wrapped.language)
                      .flatMap(_.registerTitle)
                  )
                )()
              ),
              <.hr(^.className := PromptingToConnectStyles.separator)(),
              <.button(
                ^.className := js.Array(
                  PromptingToConnectStyles.cta,
                  CTAStyles.basic,
                  CTAStyles.basicOnButton,
                  CTAStyles.moreDiscreet
                ),
                ^.onClick := skipSignup
              )(
                <.i(^.className := js.Array(FontAwesomeStyles.stepForward))(),
                self.props.wrapped.nextCta
                  .getOrElse(unescape("&nbsp;" + I18n.t("sequence.prompting-to-connect.next-cta")))
              )
            ),
            <.style()(PromptingToConnectStyles.render[String])
          )
        }
      )
}

object PromptingToConnectStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(textAlign.center)

  val titleWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val introWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val cta: StyleA =
    style(width(100.%%))

  val facebookConnectButtonWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries
        .beyondVerySmall(
          display.inlineBlock,
          verticalAlign.middle,
          width(50.%%),
          paddingRight(ThemeStyles.SpacingValue.smaller.pxToEm())
        )
    )

  val mailConnectButtonWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondVerySmall(
          display.inlineBlock,
          verticalAlign.middle,
          width(50.%%),
          marginTop.`0`,
          paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm())
        )
    )

  val loginScreenAccessWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val loginScreenAccess: StyleA =
    style(unsafeChild("button")(color(ThemeStyles.ThemeColor.primary)))

  val separator: StyleA =
    style(
      height(1.px),
      width(100.%%),
      backgroundColor(ThemeStyles.BorderColor.veryLight),
      margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(margin(ThemeStyles.SpacingValue.medium.pxToEm(), `0`))
    )

}
