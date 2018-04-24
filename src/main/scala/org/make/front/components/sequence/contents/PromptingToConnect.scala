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
                                           sequenceId: SequenceId,
                                           clickOnButtonHandler: () => Unit,
                                           authenticateHandler: ()  => Unit)

  final case class PromptingToConnectState(isAuthenticateModalOpened: Boolean, loginOrRegisterView: String = "login")

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToConnectProps, PromptingToConnectState](
        displayName = "PromptingToConnect",
        getInitialState = { _ =>
          PromptingToConnectState(isAuthenticateModalOpened = false)
        },
        render = { self =>
          val defaultTrackingParameters = Map("sequenceId" -> self.props.wrapped.sequenceId.value)

          def openLoginAuthenticateModal() = () => {
            self.setState(state => state.copy(isAuthenticateModalOpened = true, loginOrRegisterView = "login"))
            TrackingService
              .track("click-sign-up-card-sign-in", self.props.wrapped.trackingContext, defaultTrackingParameters)
          }

          def openRegisterAuthenticateModal() = () => {
            TrackingService
              .track("click-sign-up-card-email", self.props.wrapped.trackingContext, defaultTrackingParameters)
            self.setState(state => state.copy(isAuthenticateModalOpened = true, loginOrRegisterView = "register"))
          }

          def toggleAuthenticateModal() = () => {
            self.setState(state => state.copy(isAuthenticateModalOpened = !self.state.isAuthenticateModalOpened))
          }

          val skipSignup: () => Unit = { () =>
            TrackingService.track("skip-sign-up-card", self.props.wrapped.trackingContext, defaultTrackingParameters)
            self.props.wrapped.clickOnButtonHandler()
          }

          <.div(^.className := PromptingToConnectStyles.wrapper)(
            <.div(^.className := js.Array(LayoutRulesStyles.row, PromptingToConnectStyles.titleWrapper))(
              <.p(^.className := js.Array(TextStyles.bigText, TextStyles.boldText))(
                unescape(I18n.t("sequence.prompting-to-connect.title"))
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
                      trackingParameters = defaultTrackingParameters,
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
                    displayView = self.state.loginOrRegisterView,
                    onSuccessfulLogin = () => {
                      self.setState(_.copy(isAuthenticateModalOpened = false))
                      self.props.wrapped.authenticateHandler()
                    },
                    operationId = Some(self.props.wrapped.operation.operationId)
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
                unescape("&nbsp;" + I18n.t("sequence.prompting-to-connect.next-cta"))
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
