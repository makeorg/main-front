package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.activateAccount.ActivateAccountContainer
import org.make.front.components.authenticate.resetPassword.ResetPasswordContainer
import org.make.front.components.currentOperations.CurrentOperationsContainer
import org.make.front.components.error.ErrorContainer
import org.make.front.components.home.HomeContainer
import org.make.front.components.home.{Home, HomeContainer}
import org.make.front.components.maintenance.Maintenance
import org.make.front.components.operation.OperationContainer
import org.make.front.components.operation.sequence.SequenceOfTheOperationContainer
import org.make.front.components.proposal.ProposalContainer
import org.make.front.components.search.SearchResultsContainer
import org.make.front.components.theme.MaybeThemeContainer
import org.make.front.components.userProfile.UserProfileContainer
import org.make.front.helpers.DetectedCountry.getDetectedCountry

import scala.scalajs.js.Dynamic

object Container {

  // toDo: refactor this with a router middleware for country detection and redirect

  lazy val reactClass = WithRouter(
    React.createClass[Unit, Unit](
      displayName = "Container",
      componentWillUpdate = { (_, _, _) =>
        Dynamic.global.scrollTo(0, 0)
      },
      render = (_) =>
        <.Switch()(
          // @deprecated
          Seq(
            "/password-recovery/:userId/:resetToken",
            "/account-activation/:userId/:verificationToken",
            "/proposal/:proposalSlug",
            "/profile",
            "/consultation/:operationSlug",
            "/consultation/:operationSlug/selection",
            "/theme/:themeSlug",
            "/search"
          ).map { route =>
            <.Route(^.exact := true, ^.path := route, ^.component := RedirectToCountryRoute())()
          },
          <.Route(
            ^.exact := true,
            ^.path := "/:country/password-recovery/:userId/:resetToken",
            ^.component := CountryDetector(ResetPasswordContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/account-activation/:userId/:verificationToken",
            ^.component := CountryDetector(ActivateAccountContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/proposal/:proposalSlug",
            ^.component := CountryDetector(ProposalContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/profile",
            ^.component := CountryDetector(UserProfileContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug",
            ^.component := CountryDetector(OperationContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug/selection",
            ^.component := CountryDetector(SequenceOfTheOperationContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/theme/:themeSlug",
            ^.component := CountryDetector(MaybeThemeContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/search",
            ^.component := CountryDetector(SearchResultsContainer.reactClass)
          )(),
          <.Route(^.exact := true, ^.path := "/404", ^.component := ErrorContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/maintenance", ^.component := Maintenance.reactClass)(),
          <.Route(^.exact := true, ^.path := "/:country/soon", ^.component := CurrentOperationsContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/:country", ^.component := CountryDetector(HomeContainer.reactClass))(),
          <.Route(^.exact := true, ^.path := "/", ^.render := { (_: React.Props[Unit]) =>
            <.Redirect(^.to := s"/$getDetectedCountry")()
          })(),
          <.Route(^.exact := true, ^.path := "/:country/soon", ^.component := CurrentOperationsContainer.reactClass)(),
          <.Route(^.exact := false, ^.path := "/", ^.component := ErrorContainer.reactClass)()
      )
    )
  )
}
