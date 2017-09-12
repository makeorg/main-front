package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.ActivateAccount.ActivateAccountContainerComponent
import org.make.front.components.Home.HomeComponent
import org.make.front.components.ResetPassword.PasswordResetContainerComponent
import org.make.front.components.Theme.ThemeContainerComponent

object ContainerComponent {

  lazy val reactClass = WithRouter(
    React.createClass[Unit, Unit](
      render = (_) =>
        <.Switch()(
          <.Route(
            ^.exact := true,
            ^.path := "/reset-password/change-password/:userId/:resetToken",
            ^.component := PasswordResetContainerComponent.reactClass
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/account-activation/:userId/:verificationToken",
            ^.component := ActivateAccountContainerComponent.reactClass
          )(),
          <.Route(^.exact := true, ^.path := "/theme/:themeSlug", ^.component := ThemeContainerComponent.reactClass)(),
          <.Route(^.exact := true, ^.path := "/", ^.component := HomeComponent.reactClass)()
      )
    )
  )
}
