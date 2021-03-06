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

package org.make.front.components.actorProfile
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.DataLoader.DataLoaderProps
import org.make.front.components.{AppState, DataLoader}
import org.make.front.components.actorProfile.ActorProfile.ActorProfileProps
import org.make.front.models.{Organisation => OrganisationModel}
import org.make.services.organisation.OrganisationService

import scala.concurrent.Future

object ActorProfileContainer {

  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(DataLoader.reactClass))

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => DataLoaderProps[OrganisationModel] =
    (_: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      val actorSlug = props.`match`.params("organisationSlug")

      def futureActor: () => Future[Option[OrganisationModel]] = () => {
        OrganisationService.getOrganisationBySlug(actorSlug)
      }

      val shouldActorProfileUpdate: Option[OrganisationModel] => Boolean = { actor =>
        !actor.exists(_.slug != actorSlug)
      }

      DataLoaderProps[OrganisationModel](
        future = futureActor,
        shouldComponentUpdate = shouldActorProfileUpdate,
        componentDisplayedMeanwhileReactClass = WaitingForActorProfile.reactClass,
        componentReactClass = ActorProfile.reactClass,
        componentProps = { actor =>
          ActorProfileProps(actor = actor, country = state.country)
        },
        onNotFound = () => {
          props.history.push("/404")
        }
      )

    }
}
