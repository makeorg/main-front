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

package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  ThemeId           => ThemeIdModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.services.tracking.TrackingLocation

object ProposalTileWithThemeContainer {

  final case class ProposalTileWithThemeContainerProps(proposal: ProposalModel,
                                                       index: Int,
                                                       maybeOperation: Option[OperationModel],
                                                       maybeSequenceId: Option[SequenceId],
                                                       maybeLocation: Option[LocationModel],
                                                       trackingLocation: TrackingLocation)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ProposalTileWithTheme.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState,
                     Props[ProposalTileWithThemeContainerProps]) => ProposalTileWithTheme.ProposalTileWithThemeProps =
    (_: Dispatch) => { (appState: AppState, props: Props[ProposalTileWithThemeContainerProps]) =>
      def searchThemeById(themeId: ThemeIdModel): Option[TranslatedThemeModel] = {
        appState.themes.find(_.id.value == themeId.value)
      }

      val theme: Option[TranslatedThemeModel] =
        props.wrapped.proposal.themeId.flatMap(themeId => searchThemeById(themeId))

      val themeName: Option[String] = theme.map(_.title)

      val themeSlug: Option[String] = theme.map(_.slug)

      ProposalTileWithTheme.ProposalTileWithThemeProps(
        proposal = props.wrapped.proposal,
        themeName = themeName.getOrElse(""),
        themeSlug = themeSlug.getOrElse(""),
        index = props.wrapped.index,
        maybeTheme = theme,
        maybeOperation = props.wrapped.maybeOperation,
        maybeSequenceId = props.wrapped.maybeSequenceId,
        maybeLocation = props.wrapped.maybeLocation,
        trackingLocation = props.wrapped.trackingLocation,
        country = appState.country
      )
    }
}
