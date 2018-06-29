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
import org.make.front.models.{
  SequenceId,
  Label             => LabelModel,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.services.proposal.{ProposalService, SearchResult}

import scala.concurrent.Future
import scala.scalajs.js

object ThemeShowcaseContainer {

  final case class ThemeShowcaseContainerProps(themeSlug: String,
                                               maybeIntro: Option[String] = None,
                                               maybeNews: Option[String] = None,
                                               maybeOperation: Option[OperationModel] = None,
                                               maybeSequenceId: Option[SequenceId] = None,
                                               maybeLocation: Option[LocationModel] = None)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ThemeShowcase.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ThemeShowcaseContainerProps]) => ThemeShowcase.ThemeShowcaseProps =
    (_: Dispatch) => { (appState: AppState, props: Props[ThemeShowcaseContainerProps]) =>
      val themes = appState.themes
      val maybeTheme = themes.find(_.slug == props.wrapped.themeSlug)

      maybeTheme.map { theme =>
        ThemeShowcase.ThemeShowcaseProps(
          proposals = () =>
            ProposalService
              .searchProposals(
                themesIds = js.Array(theme.id),
                labelsIds = Some(js.Array(LabelModel.Star.name)),
                limit = Some(3),
                language = Some(appState.language),
                country = Some(appState.country)
            ),
          theme = theme,
          maybeIntro = props.wrapped.maybeIntro,
          maybeNews = props.wrapped.maybeNews,
          maybeOperation = props.wrapped.maybeOperation,
          maybeSequenceId = props.wrapped.maybeSequenceId,
          maybeLocation = props.wrapped.maybeLocation,
          country = appState.country
        )
      }.getOrElse(
        ThemeShowcase.ThemeShowcaseProps(
          proposals = () => Future.successful(SearchResult(total = 0, results = js.Array(), seed = None)),
          theme = TranslatedThemeModel.empty,
          country = appState.country
        )
      )
    }
}
