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

package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{Location => LocationModel}
import org.make.services.proposal.ProposalService

import scala.scalajs.js

object LabelShowcaseContainer {

  final case class LabelShowcaseContainerProps(intro: String,
                                               label: String,
                                               title: String,
                                               maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(TrendingShowcase.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[LabelShowcaseContainerProps]) => TrendingShowcase.TrendingShowcaseProps =
    (_: Dispatch) => { (appState: AppState, props: Props[LabelShowcaseContainerProps]) =>
      TrendingShowcase.TrendingShowcaseProps(
        proposals = () =>
          ProposalService
            .searchProposals(
              labelsIds = Some(js.Array(props.wrapped.label)),
              limit = Some(2),
              language = Some(appState.language)
          ),
        intro = props.wrapped.intro,
        title = props.wrapped.title,
        maybeLocation = props.wrapped.maybeLocation,
        country = appState.country
      )
    }
}
