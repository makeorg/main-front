package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.authenticate.AuthenticateWithSocialNetworksContainer.AuthenticateWithSocialNetworksContainerProps
import org.make.front.components.presentationals.RichVirtualDOMElements
import org.make.front.styles._

import scalacss.DevDefaults._

object App {

  lazy val reactClass = WithRouter(
    React.createClass[Unit, Unit](
      render = (_) =>
        <("app-container")(^.className := "App")(
          <.style()(
            Basic.render[String],
            LayoutRulesStyles.render[String],
            TextStyles.render[String],
            CTAStyles.render[String],
            InputStyles.render[String],
            TagStyles.render[String],
            ModalStyles.render[String]
          ),
          <.MainHeaderComponent.empty,
          <.NotificationContainerComponent.empty,
          <.ContainerComponent.empty,
          <.AuthenticateWithSocialNetworksComponent(
            ^.wrapped := AuthenticateWithSocialNetworksContainerProps(
              title = "Testons le composant",
              comment = "C'est trop de la balle de bombe"
            )
          )(),
          <.NavInThemesContainerComponent.empty,
          <.MainFooterComponent.empty,
          <.ConnectUserContainerComponent.empty,
          <.PasswordRecoveryContainerComponent.empty,
          <.style()(RWDHideRulesStyles.render[String])
      )
    )
  )
}
