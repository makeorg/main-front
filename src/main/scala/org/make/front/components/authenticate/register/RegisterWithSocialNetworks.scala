package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.authenticate.AuthenticateWithFacebookContainer.AuthenticateWithFacebookContainerProps
import org.make.front.components.authenticate.AuthenticateWithGoogleContainer.AuthenticateWithGoogleContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._
import org.make.front.styles.base.TextStyles

object RegisterWithSocialNetworks {

  case class RegisterWithSocialNetworksProps(onSuccessfulLogin: () => Unit = () => {})

  val reactClass: ReactClass =
    React
      .createClass[RegisterWithSocialNetworksProps, Unit](
        displayName = "RegisterWithSocialNetworks",
        render = { self =>
          <.div(^.className := RegisterWithSocialNetworksStyles.wrapper)(
            <.p(^.className := Seq(RegisterWithSocialNetworksStyles.text, TextStyles.smallText))(
              unescape(I18n.t("authenticate.register.with-social-networks.intro"))
            ),
            <.div(^.className := RegisterWithSocialNetworksStyles.buttonWrapper)(
              <.AuthenticateWithFacebookContainerComponent(
                ^.wrapped := AuthenticateWithFacebookContainerProps(
                  onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin,
                  isLookingLikeALink = true
                )
              )()
            ),
            <.p(^.className := Seq(RegisterWithSocialNetworksStyles.text, TextStyles.smallText))(
              unescape(I18n.t("authenticate.register.with-social-networks.separator"))
            ),
            <.div(^.className := RegisterWithSocialNetworksStyles.buttonWrapper)(
              <.AuthenticateWithGoogleContainerComponent(
                ^.wrapped := AuthenticateWithGoogleContainerProps(
                  onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin,
                  isLookingLikeALink = true
                )
              )()
            ),
            <.style()(RegisterWithSocialNetworksStyles.render[String])
          )
        }
      )
}

object RegisterWithSocialNetworksStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.lighter))

  val text: StyleA =
    style(display.inlineBlock, verticalAlign.baseline)

  val buttonWrapper: StyleA =
    style(display.inlineBlock, verticalAlign.baseline, color(ThemeStyles.ThemeColor.primary))

}
