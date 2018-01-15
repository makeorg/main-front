package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.authenticate.AuthenticateWithFacebookContainer.AuthenticateWithFacebookContainerProps
import org.make.front.components.authenticate.AuthenticateWithGoogleContainer.AuthenticateWithGoogleContainerProps
import org.make.front.models.OperationId
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

object AuthenticateWithSocialNetworks {

  case class AuthenticateWithSocialNetworksProps(note: String = "",
                                                 onSuccessfulLogin: () => Unit = () => {},
                                                 operationId: Option[OperationId])

  val reactClass: ReactClass =
    React
      .createClass[AuthenticateWithSocialNetworksProps, Unit](
        displayName = "AuthenticateWithSocialNetworks",
        render = { self =>
          <.div()(
            <.ul()(
              <.li(^.className := AuthenticateWithSocialNetworksStyles.facebookConnectButtonWrapper)(
                <.AuthenticateWithFacebookContainerComponent(
                  ^.wrapped := AuthenticateWithFacebookContainerProps(
                    onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin,
                    operationId = self.props.wrapped.operationId
                  )
                )()
              ),
              <.li(^.className := AuthenticateWithSocialNetworksStyles.googleConnectButtonWrapper)(
                <.AuthenticateWithGoogleContainerComponent(
                  ^.wrapped := AuthenticateWithGoogleContainerProps(
                    onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin,
                    operationId = self.props.wrapped.operationId
                  )
                )()
              )
            ),
            if (self.props.wrapped.note != "") {
              <.div(^.className := AuthenticateWithSocialNetworksStyles.noteWrapper)(
                <.p(^.className := Seq(AuthenticateWithSocialNetworksStyles.note, TextStyles.smallText))(
                  self.props.wrapped.note
                )
              )
            },
            <.style()(AuthenticateWithSocialNetworksStyles.render[String])
          )
        }
      )
}

object AuthenticateWithSocialNetworksStyles extends StyleSheet.Inline {

  import dsl._

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

  val noteWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
    ThemeStyles.MediaQueries
      .beyondSmall(marginTop(ThemeStyles.SpacingValue.small.pxToEm())),
    textAlign.center
  )

  val note: StyleA = style(color(ThemeStyles.TextColor.lighter))
}
