package org.make.front.components.users.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.users.authenticate.AuthenticateWithSocialNetworksContainer.AuthenticateWithSocialNetworksContainerProps
import org.make.front.components.users.authenticate.register.RegisterContainer.RegisterUserProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape

object RegisterWithSocialNetworksOrEmail {

  case class RegisterWithSocialNetworksOrEmailProps(onSuccessfulLogin: () => Unit = () => {})

  val regular: ReactClass = React.createClass[RegisterWithSocialNetworksOrEmailProps, Unit](render = { self =>
    <.div()(
      <.AuthenticateWithSocialNetworksComponent(
        ^.wrapped := AuthenticateWithSocialNetworksContainerProps(
          intro = unescape(I18n.t("form.register.socialConnect")),
          note = unescape(I18n.t("form.register.noPublishedContent")),
          onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin
        )
      )(),
      <.p()(I18n.t("form.or")),
      <.RegisterWithEmailComponent(
        ^.wrapped := RegisterUserProps(
          intro = unescape(I18n.t("form.register.withForm")),
          note = unescape(I18n.t("form.register.termsAgreed")),
          onSuccessfulRegistration = self.props.wrapped.onSuccessfulLogin
        )
      )()
    )
  })

  val expanded: ReactClass = React.createClass[RegisterWithSocialNetworksOrEmailProps, Unit](render = { self =>
    <.div()(
      <.AuthenticateWithSocialNetworksComponent(
        ^.wrapped := AuthenticateWithSocialNetworksContainerProps(
          intro = unescape(I18n.t("form.register.socialConnect")),
          note = unescape(I18n.t("form.register.noPublishedContent")),
          onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin
        )
      )(),
      <.p()(I18n.t("form.or")),
      <.RegisterWithEmailExpandedComponent(
        ^.wrapped := RegisterUserProps(
          intro = unescape(I18n.t("form.register.withForm")),
          note = unescape(I18n.t("form.register.termsAgreed")),
          onSuccessfulRegistration = self.props.wrapped.onSuccessfulLogin
        )
      )()
    )
  })

}
