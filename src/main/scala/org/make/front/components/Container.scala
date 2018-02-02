package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.activateAccount.ActivateAccountContainer
import org.make.front.components.authenticate.resetPassword.ResetPasswordContainer
import org.make.front.components.home.Home
import org.make.front.components.error.ErrorContainer
import org.make.front.components.maintenance.Maintenance
import org.make.front.components.operation.sequence.SequenceOfTheOperationContainer
import org.make.front.components.search.SearchResultsContainer
import org.make.front.components.theme.MaybeThemeContainer
import org.make.front.components.operation.OperationContainer
import org.make.front.components.proposal.ProposalContainer
import org.make.front.components.userProfile.UserProfileContainer

import scala.scalajs.js.Dynamic

object Container {

  lazy val reactClass = WithRouter(
    React.createClass[Unit, Unit](
      displayName = "Container",
      componentWillUpdate = { (_, _, _) =>
        Dynamic.global.scrollTo(0, 0)
      },
      render = (_) =>
        <.Switch()(
          <.Route(
            ^.exact := true,
            ^.path := "/password-recovery/:userId/:resetToken",
            ^.component := ResetPasswordContainer.reactClass
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/account-activation/:userId/:verificationToken",
            ^.component := ActivateAccountContainer.reactClass
          )(),
          <.Route(^.exact := true, ^.path := "/proposal/:proposalSlug", ^.component := ProposalContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/profile", ^.component := UserProfileContainer.reactClass)(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug",
            ^.component := OperationContainer.reactClass
          )(),
          // @deprecated should be removed
          <.Route(
            ^.exact := true,
            ^.path := "/consultation/:operationSlug",
            ^.component := OperationContainer.reactClass
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug/selection",
            ^.component := SequenceOfTheOperationContainer.reactClass
          )(),
          // @deprecated should be removed
          <.Route(
            ^.exact := true,
            ^.path := "/consultation/:operationSlug/selection",
            ^.component := SequenceOfTheOperationContainer.reactClass
          )(),
          <.Route(^.exact := true, ^.path := "/theme/:themeSlug", ^.component := MaybeThemeContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/search", ^.component := SearchResultsContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/404", ^.component := ErrorContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/maintenance", ^.component := Maintenance.reactClass)(),
          <.Route(^.exact := true, ^.path := "/", ^.component := Home.reactClass)(),
          <.Route(^.exact := false, ^.path := "/", ^.component := ErrorContainer.reactClass)()
      )
    )
  )
}
