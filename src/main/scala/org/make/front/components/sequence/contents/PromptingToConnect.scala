package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.AuthenticateWithSocialNetworksContainer.AuthenticateWithSocialNetworksContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Operation => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

object PromptingToConnect {

  final case class PromptingToConnectProps(operation: OperationModel, clickOnButtonHandler: () => Unit)

  final case class PromptingToConnectState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToConnectProps, PromptingToConnectState](
        displayName = "PromptingToConnect",
        getInitialState = { _ =>
          PromptingToConnectState(isProposalModalOpened = false)
        },
        render = { self =>
          val closeProposalModal: () => Unit = () => {
            self.setState(state => state.copy(isProposalModalOpened = false))
          }

          val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
          }

          <.div(^.className := PromptingToConnectStyles.wrapper)(
            <.div(^.className := RowRulesStyles.row)(
              <.div(^.className := Seq(ColRulesStyles.col, PromptingToConnectStyles.titleWrapper))(
                <.p(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(
                  unescape(I18n.t("sequence.prompting-to-connect.title"))
                )
              )
            ),
            <.div(^.className := RowRulesStyles.evenNarrowerCenteredRow)(
              <.div(^.className := ColRulesStyles.col)(
                <.div(^.className := PromptingToConnectStyles.introWrapper)(
                  <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("sequence.prompting-to-connect.intro")))
                ),
                <.AuthenticateWithSocialNetworksComponent(
                  ^.wrapped := AuthenticateWithSocialNetworksContainerProps(
                    note = unescape(I18n.t("sequence.prompting-to-connect.caution")),
                    onSuccessfulLogin = () => {}
                  )
                )(),
                <.div(^.className := PromptingToConnectStyles.separatorWrapper)(
                  <.p(^.className := Seq(PromptingToConnectStyles.separator, TextStyles.mediumText))(
                    I18n.t("sequence.prompting-to-connect.separator")
                  )
                ),
                <.button(
                  ^.className := Seq(PromptingToConnectStyles.cta, CTAStyles.basic, CTAStyles.basicOnButton),
                  ^.onClick := self.props.wrapped.clickOnButtonHandler
                )(unescape(I18n.t("sequence.prompting-to-connect.authenticate-with-email-cta"))),
                <.div(^.className := PromptingToConnectStyles.loginScreenAccessWrapper)(
                  <.p(^.className := Seq(PromptingToConnectStyles.loginScreenAccess, TextStyles.smallText))(
                    unescape(I18n.t("sequence.prompting-to-connect.login-screen-access.intro") + " "),
                    <.a(^.className := TextStyles.boldText)(
                      unescape(I18n.t("sequence.prompting-to-connect.login-screen-access.link-support"))
                    )
                  )
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
    padding :=! s"0 ${20.pxToEm().value}",
    margin :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
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
      borderBottom :=! s"1px solid ${ThemeStyles.BorderColor.veryLight.value}",
      margin :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0"
    )

  val loginScreenAccess: StyleA =
    style(color(ThemeStyles.TextColor.lighter), unsafeChild("a")(color(ThemeStyles.ThemeColor.primary)))

  val cta: StyleA =
    style(width(100.%%))

}
