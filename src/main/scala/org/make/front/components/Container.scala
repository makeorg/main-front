package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.activateAccount.ActivateAccountContainer
import org.make.front.components.home.Home
import org.make.front.components.authenticate.resetPassword.ResetPasswordContainer
import org.make.front.components.search.SearchResultsContainer
import org.make.front.components.theme.ThemeContainer
import org.make.front.components.operation.{OperationContainer, OperationSSequence, OperationSSequenceContainer}

object Container {

  lazy val reactClass = WithRouter(
    React.createClass[Unit, Unit](
      displayName = "Container",
      render = (_) =>
        <.Switch()(
          <.Route(
            ^.exact := true,
            ^.path := "/reset-password/change-password/:userId/:resetToken",
            ^.component := ResetPasswordContainer.reactClass
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/account-activation/:userId/:verificationToken",
            ^.component := ActivateAccountContainer.reactClass
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/operation/:operationSlug",
            ^.component := OperationContainer.reactClass
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/operation/:operationSlug/sequence/:sequenceSlug",
            ^.component := OperationSSequenceContainer.reactClass
          )(),
          <.Route(^.exact := true, ^.path := "/theme/:themeSlug", ^.component := ThemeContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/search", ^.component := SearchResultsContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/", ^.component := Home.reactClass)()
      )
    )
  )
}
