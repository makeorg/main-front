/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.Components._
import org.make.front.components.activateAccount.ActivateAccountContainer
import org.make.front.components.authenticate.resetPassword.ResetPasswordContainer
import org.make.front.components.error.ErrorContainer
import org.make.front.components.home.HomeContainer
import org.make.front.components.maintenance.Maintenance
import org.make.front.components.operation.sequence.SequenceOfTheOperationContainer
import org.make.front.components.proposal.ProposalContainer
import org.make.front.components.theme.MaybeThemeContainer
import org.make.front.components.userProfile.UserProfileContainer
import org.make.front.helpers.DetectedCountry.getDetectedCountry

import scala.scalajs.js
import scala.scalajs.js.Dynamic

object Container {

  // toDo: refactor this with a router middleware for country detection and redirect
  def manageParams(reactClass: ReactClass): ReactClass = {
    OperationDetector(CountryDetector(reactClass))
  }

  lazy val reactClass = WithRouter(
    React.createClass[Unit, Unit](
      displayName = "Container",
      componentWillUpdate = { (_, _, _) =>
        Dynamic.global.scrollTo(0, 0)
      },
      render = (_) =>
        <.Switch()(
          <.Route(^.exact := true, ^.path := "/", ^.render := { (_: React.Props[Unit]) =>
            <.Redirect(^.to := s"/$getDetectedCountry")()
          })(),
          <.Route(^.exact := true, ^.path := "/404", ^.component := ErrorContainer.reactClass)(),
          <.Route(^.exact := true, ^.path := "/maintenance", ^.component := Maintenance.reactClass)(),
          // @deprecated => Warning: /consultation/:operationSlug/selection is used by front-proxy (offline URL)
          js.Array(
              "/proposal/:proposalId/:proposalSlug",
              "/proposal/:proposalSlug",
              "/profile",
              "/consultation/:operationSlug",
              "/consultation/:operationSlug/selection",
              "/theme/:themeSlug"
            )
            .map { route =>
              <.Route(^.exact := true, ^.path := route, ^.component := RedirectToCountryRoute())()
            }
            .toSeq,
          <.Route(
            ^.exact := true,
            ^.path := "/:country([A-Za-z]{2,3})/soon",
            ^.component := Layout.reactClass(<.CurrentOperationsContainerComponent.empty)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/password-recovery/:userId/:resetToken",
            ^.component := manageParams(ResetPasswordContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/account-activation/:userId/:verificationToken",
            ^.component := manageParams(ActivateAccountContainer.reactClass)
          )(),
          //@Deprecated
          <.Route(
            ^.exact := true,
            ^.path := "/:country/theme/:themeSlug/proposal/:proposalSlug",
            ^.component := manageParams(ProposalContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/theme/:themeSlug/proposal/:proposalId/:proposalSlug",
            ^.component := manageParams(ProposalContainer.reactClass)
          )(),
          //@Deprecated
          <.Route(
            ^.exact := true,
            ^.path := "/:country/proposal/:proposalSlug",
            ^.component := manageParams(ProposalContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/proposal/:proposalId/:proposalSlug",
            ^.component := manageParams(ProposalContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/profile",
            ^.component := manageParams(UserProfileContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/profile/:organisationSlug",
            ^.component := manageParams(Layout.reactClass(<.ActorProfileContainerComponent.empty, withFooter = false))
          )(),
          // @deprecated
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug/proposal/:proposalSlug",
            ^.component := manageParams(ProposalContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug/proposal/:proposalId/:proposalSlug",
            ^.component := manageParams(ProposalContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug",
            ^.render := { (props: React.Props[Unit]) =>
              <.Redirect(
                ^.to := s"/${props.`match`.params("country")}/consultation/${props.`match`.params("operationSlug")}/consultation"
              )()
            }
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug/selection",
            ^.component := manageParams(SequenceOfTheOperationContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug/search",
            ^.component := manageParams(Layout.reactClass(<.SearchResultsContainerComponent.empty))
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/consultation/:operationSlug/:activeTab(consultation|actions)",
            ^.component := manageParams(
              Layout.reactClass(<.ConsultationContainerComponent.empty, fixedHeader = false, withFooter = false)
            )
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/theme/:themeSlug",
            ^.component := manageParams(MaybeThemeContainer.reactClass)
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/theme/:themeSlug/search",
            ^.component := manageParams(Layout.reactClass(<.SearchResultsContainerComponent.empty))
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country/search",
            ^.component := manageParams(Layout.reactClass(<.HomeSearchResultsContainerComponent.empty))
          )(),
          <.Route(
            ^.exact := true,
            ^.path := "/:country([A-Za-z]{2,3})/:operationSlug",
            ^.render := { (props: React.Props[Unit]) =>
              <.Redirect(
                ^.to := s"/${props.`match`.params("country")}/consultation/${props.`match`.params("operationSlug")}/consultation"
              )()
            }
          )(),
          // toDo: change patterns /:country and /:operationSlug to avoid conflicts
          <.Route(
            ^.exact := true,
            ^.path := "/:country([A-Za-z]{2,3})",
            ^.component := manageParams(HomeContainer.reactClass)
          )(),
          <.Route(^.exact := true, ^.path := "/:operationSlug", ^.component := RedirectToCountryRoute())(),
          <.Route(^.exact := false, ^.path := "/", ^.component := ErrorContainer.reactClass)()
      )
    )
  )
}
