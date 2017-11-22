package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.AuthenticateWithSocialNetworksContainer.AuthenticateWithSocialNetworksContainerProps
import org.make.front.components.authenticate.LoginOrRegister.LoginOrRegisterProps
import org.make.front.components.modals.Modal.ModalProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Operation => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

object PromptingToConnect {

  final case class PromptingToConnectProps(operation: OperationModel,
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
          def openLoginAuthenticateModal() = () => {
            self.setState(state => state.copy(isAuthenticateModalOpened = true, loginOrRegisterView = "login"))
          }

          def openRegisterAuthenticateModal() = () => {
            self.setState(state => state.copy(isAuthenticateModalOpened = true, loginOrRegisterView = "register"))
          }

          def toggleAuthenticateModal() = () => {
            self.setState(state => state.copy(isAuthenticateModalOpened = !self.state.isAuthenticateModalOpened))
          }

          <.div(^.className := PromptingToConnectStyles.wrapper)(
            <.div(^.className := Seq(LayoutRulesStyles.row, PromptingToConnectStyles.titleWrapper))(
              <.p(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(
                unescape(I18n.t("sequence.prompting-to-connect.title"))
              )
            ),
            <.div(^.className := LayoutRulesStyles.evenNarrowerCenteredRow)(
              <.div(^.className := PromptingToConnectStyles.introWrapper)(
                <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("sequence.prompting-to-connect.intro")))
              ),
              <.AuthenticateWithSocialNetworksComponent(
                ^.wrapped := AuthenticateWithSocialNetworksContainerProps(
                  note = unescape(I18n.t("sequence.prompting-to-connect.caution")),
                  onSuccessfulLogin = () => {
                    self.props.wrapped.authenticateHandler()
                  }
                )
              )(),
              <.div(^.className := PromptingToConnectStyles.separatorWrapper)(
                <.p(^.className := Seq(PromptingToConnectStyles.separator, TextStyles.mediumText))(
                  I18n.t("sequence.prompting-to-connect.separator")
                )
              ),
              <.button(
                ^.className := Seq(PromptingToConnectStyles.cta, CTAStyles.basic, CTAStyles.basicOnButton),
                ^.onClick := openRegisterAuthenticateModal
              )(unescape(I18n.t("sequence.prompting-to-connect.authenticate-with-email-cta"))),
              <.div(^.className := PromptingToConnectStyles.loginScreenAccessWrapper)(
                <.p(^.className := Seq(PromptingToConnectStyles.loginScreenAccess, TextStyles.smallText))(
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
                    displayView = self.state.loginOrRegisterView,
                    onSuccessfulLogin = () => {
                      self.setState(_.copy(isAuthenticateModalOpened = false))
                      self.props.wrapped.authenticateHandler()
                    },
                    operation = Some(self.props.wrapped.operation.operationId)
                  )
                )()
              ),
              <.button(
                ^.className := Seq(
                  PromptingToConnectStyles.cta,
                  CTAStyles.basic,
                  CTAStyles.basicOnButton,
                  CTAStyles.moreDiscreet
                ),
                ^.onClick := self.props.wrapped.clickOnButtonHandler
              )(
                <.i(^.className := Seq(FontAwesomeStyles.stepForward))(),
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

  val separatorWrapper: StyleA = style(textAlign.center, overflow.hidden)

  val separator: StyleA = style(
    position.relative,
    display.inlineBlock,
    padding(`0`, 20.pxToEm()),
    margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
    ThemeStyles.Font.playfairDisplayItalic,
    fontStyle.italic,
    lineHeight(1),
    color(ThemeStyles.TextColor.lighter),
    (&.before)(
      content := "''",
      position.absolute,
      top(50.%%),
      left(100.%%),
      marginTop(-0.5.px),
      height(1.px),
      width(999999.pxToEm()),
      backgroundColor(ThemeStyles.BorderColor.veryLight)
    ),
    (&.after)(
      content := "''",
      position.absolute,
      top(50.%%),
      right(100.%%),
      marginTop(-0.5.px),
      height(1.px),
      width(999999.pxToEm()),
      backgroundColor(ThemeStyles.BorderColor.veryLight)
    )
  )

  val loginScreenAccessWrapper: StyleA =
    style(
      paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      borderBottom(1.px, solid, ThemeStyles.BorderColor.veryLight),
      margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`)
    )

  val loginScreenAccess: StyleA =
    style(color(ThemeStyles.TextColor.lighter), unsafeChild("button")(color(ThemeStyles.ThemeColor.primary)))

  val cta: StyleA =
    style(width(100.%%))

}
