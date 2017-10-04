package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.authenticate.AuthenticateWithSocialNetworksContainer.AuthenticateWithSocialNetworksContainerProps
import org.make.front.components.authenticate.register.RegisterContainer.RegisterUserProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object RegisterWithSocialNetworksOrEmail {

  case class RegisterWithSocialNetworksOrEmailProps(onSuccessfulLogin: () => Unit = () => {})

  val regular: ReactClass = React.createClass[RegisterWithSocialNetworksOrEmailProps, Unit](
    displayName = "RegisterWithSocialNetworksOrEmail",
    render = { self =>
      <.div()(
        <.div(^.className := RegisterWithSocialNetworksOrEmailStyles.introWrapper)(
          <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("form.register.withSocial")))
        ),
        <.AuthenticateWithSocialNetworksComponent(
          ^.wrapped := AuthenticateWithSocialNetworksContainerProps(
            note = unescape(I18n.t("form.register.noPublishedContent")),
            onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin
          )
        )(),
        <.div(^.className := RegisterWithSocialNetworksOrEmailStyles.separatorWrapper)(
          <.p(^.className := Seq(RegisterWithSocialNetworksOrEmailStyles.separator, TextStyles.mediumText))(
            I18n.t("form.or")
          )
        ),
        <.div(^.className := RegisterWithSocialNetworksOrEmailStyles.introWrapper)(
          <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("form.register.withForm")))
        ),
        <.RegisterWithEmailComponent(
          ^.wrapped := RegisterUserProps(
            note = unescape(I18n.t("form.register.termsAgreed")),
            onSuccessfulRegistration = self.props.wrapped.onSuccessfulLogin
          )
        )(),
        <.style()(RegisterWithSocialNetworksOrEmailStyles.render[String])
      )
    }
  )

  val expanded: ReactClass = React.createClass[RegisterWithSocialNetworksOrEmailProps, Unit](render = { self =>
    <.div()(
      <.div(^.className := RegisterWithSocialNetworksOrEmailStyles.introWrapper)(
        <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("form.register.withSocial")))
      ),
      <.AuthenticateWithSocialNetworksComponent(
        ^.wrapped := AuthenticateWithSocialNetworksContainerProps(
          note = unescape(I18n.t("form.register.noPublishedContent")),
          onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin
        )
      )(),
      <.div(^.className := RegisterWithSocialNetworksOrEmailStyles.separatorWrapper)(
        <.p(^.className := Seq(RegisterWithSocialNetworksOrEmailStyles.separator, TextStyles.mediumText))(
          I18n.t("form.or")
        )
      ),
      <.div(^.className := RegisterWithSocialNetworksOrEmailStyles.introWrapper)(
        <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("form.register.withForm")))
      ),
      <.RegisterWithEmailExpandedComponent(
        ^.wrapped := RegisterUserProps(
          note = unescape(I18n.t("form.register.termsAgreed")),
          onSuccessfulRegistration = self.props.wrapped.onSuccessfulLogin
        )
      )(),
      <.style()(RegisterWithSocialNetworksOrEmailStyles.render[String])
    )
  })

}

object RegisterWithSocialNetworksOrEmailStyles extends StyleSheet.Inline {

  import dsl._

  val introWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

  val separatorWrapper: StyleA = style(textAlign.center, overflow.hidden)

  val separator: StyleA = style(
    position.relative,
    display.inlineBlock,
    padding :=! s"0 ${20.pxToEm().value}",
    margin :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0",
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
