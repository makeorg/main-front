package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.activateAccount.ActivateAccountContainer
import org.make.front.components.authenticate.resetPassword.ResetPasswordContainer
import org.make.front.components.home.Home
import org.make.front.components.operation.VFFSequenceContainer
import org.make.front.components.proposal.ProposalContainer
import org.make.front.components.search.SearchResultsContainer
import org.make.front.components.theme.ThemeContainer
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
          /*TODO : reactive later*/
          /*<.Route(
            ^.exact := true,
            ^.path := "/operation/:operationSlug",
            ^.component := OperationContainer.reactClass
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/operation/:operationSlug/consultation/:sequenceSlug",
            ^.component := OperationSequenceContainer.reactClass
          )(),*/
          <.Route(
            ^.exact := true,
            ^.path := "/consultation/comment-lutter-contre-les-violences-faites-aux-femmes",
            ^.component := VFFSequenceContainer.reactClass
          )(),
          <.Route(^.exact := true, ^.path := "/theme/:themeSlug", ^.component := ThemeContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/search", ^.component := SearchResultsContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/", ^.component := Home.reactClass)()
      )
    )
  )
}
