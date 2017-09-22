package org.make.front.components.authenticate.login

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.authenticate.AuthenticateWithSocialNetworksContainer.AuthenticateWithSocialNetworksContainerProps
import org.make.front.components.authenticate.login.LoginWithEmailContainer.LoginWithEmailContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object LoginWithEmailOrSocialNetworks {

  case class LoginWithEmailOrSocialNetworksProps(onSuccessfulLogin: () => Unit = () => {})

  val reactClass: ReactClass = React.createClass[LoginWithEmailOrSocialNetworksProps, Unit](render = { self =>
    <.div()(
      <.div(^.className := LoginWithEmailOrSocialNetworksStyles.introWrapper)(
        <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("form.login.socialConnect")))
      ),
      <.AuthenticateWithSocialNetworksComponent(
        ^.wrapped := AuthenticateWithSocialNetworksContainerProps(
          note = "",
          onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin
        )
      )(),
      <.div(^.className := LoginWithEmailOrSocialNetworksStyles.separatorWrapper)(
        <.p(^.className := Seq(LoginWithEmailOrSocialNetworksStyles.separator, TextStyles.mediumText))(
          I18n.t("form.or")
        )
      ),
      <.div(^.className := LoginWithEmailOrSocialNetworksStyles.introWrapper)(
        <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("form.login.stdConnect")))
      ),
      <.LoginWithEmailComponent(
        ^.wrapped := LoginWithEmailContainerProps(note = "", onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin)
      )(),
      <.style()(LoginWithEmailOrSocialNetworksStyles.render[String])
    )
  })

}

object LoginWithEmailOrSocialNetworksStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val introWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

  val separatorWrapper: StyleA = style(textAlign.center, overflow.hidden)
  val separator: StyleA = style(
    position.relative,
    display.inlineBlock,
    padding := s"0 ${20.pxToEm().value}",
    margin := s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0",
    backgroundColor.white,
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
