package org.make.front.components.authenticate.login

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.AuthenticateWithFacebookContainer.AuthenticateWithFacebookContainerProps
import org.make.front.components.authenticate.AuthenticateWithGoogleContainer.AuthenticateWithGoogleContainerProps
import org.make.front.components.authenticate.login.LoginWithEmailContainer.LoginWithEmailContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

object LoginWithSocialNetworksOrEmail {

  case class LoginWithSocialNetworksOrEmailProps(onSuccessfulLogin: () => Unit = () => {})

  val reactClass: ReactClass = React.createClass[LoginWithSocialNetworksOrEmailProps, Unit](
    displayName = "LoginWithEmailOrSocialNetworks",
    render = { self =>
      <.div()(
        <.div(^.className := LoginWithSocialNetworksOrEmailStyles.introWrapper)(
          <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("authenticate.login.with-social-networks-intro")))
        ),
        <.ul()(
          <.li(^.className := LoginWithSocialNetworksOrEmailStyles.facebookConnectButtonWrapper)(
            <.AuthenticateWithFacebookContainerComponent(
              ^.wrapped := AuthenticateWithFacebookContainerProps(
                onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin
              )
            )()
          ),
          <.li(^.className := LoginWithSocialNetworksOrEmailStyles.googleConnectButtonWrapper)(
            <.AuthenticateWithGoogleContainerComponent(
              ^.wrapped := AuthenticateWithGoogleContainerProps(
                onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin
              )
            )()
          )
        ),
        <.div(^.className := LoginWithSocialNetworksOrEmailStyles.separatorWrapper)(
          <.p(^.className := Seq(LoginWithSocialNetworksOrEmailStyles.separator, TextStyles.smallText))(
            I18n.t("authenticate.login.separator")
          )
        ),
        <.div(^.className := LoginWithSocialNetworksOrEmailStyles.introWrapper)(
          <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("authenticate.login.with-email-intro")))
        ),
        <.LoginWithEmailComponent(
          ^.wrapped := LoginWithEmailContainerProps(note = "", onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin)
        )(),
        <.style()(LoginWithSocialNetworksOrEmailStyles.render[String])
      )
    }
  )
}

object LoginWithSocialNetworksOrEmailStyles extends StyleSheet.Inline {

  import dsl._

  val introWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

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

  val googleConnectButtonWrapper: StyleA =
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

  val separatorWrapper: StyleA =
    style(
      margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(margin(ThemeStyles.SpacingValue.medium.pxToEm(), `0`)),
      overflow.hidden,
      textAlign.center
    )

  val separator: StyleA = style(
    position.relative,
    display.inlineBlock,
    padding(`0`, 20.pxToEm()),
    ThemeStyles.Font.playfairDisplayItalic,
    fontStyle.italic,
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
}
