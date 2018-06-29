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

package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.facades.Autosuggest._
import org.make.front.facades._
import org.make.front.helpers.QueryString
import org.make.front.styles.ThemeStyles
import org.make.front.styles.ui.InputStyles
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom.raw.FocusEvent

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.{Dictionary, Dynamic, URIUtils}

object SearchForm {
  type Self = React.Self[Unit, State]

  case class SearchFormProps(country: String)
  case class State(value: String,
                   suggestions: js.Array[js.Object],
                   operationSlug: Option[String] = None,
                   themeSlug: Option[String] = None)

  val autoSuggestTheme: Dictionary[String] =
    Map[String, String](
      "container" -> js
        .Array(
          InputStyles.wrapper.htmlClass,
          InputStyles.withIcon.htmlClass,
          SearchFormStyles.searchInputWithIconWrapper.htmlClass
        )
        .mkString(" "),
      "containerOpen" -> "",
      "input" -> "",
      "inputOpen" -> "",
      "suggestionsContainer" -> "",
      "suggestionsContainerOpen" -> "",
      "suggestionsList" -> "",
      "suggestion" -> "",
      "suggestionFirst" -> "",
      "suggestionHighlighted" -> "",
      "sectionContainer" -> "",
      "sectionContainerFirst" -> "",
      "sectionContainerTitle" -> ""
    ).toJSDictionary

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[SearchFormProps, State](
          displayName = "SearchForm",
          shouldComponentUpdate = (self, props, state) => true,
          getInitialState = (self) => {

            State(
              value = URIUtils.decodeURI(QueryString.parse(self.props.location.search).getOrElse("q", "")),
              suggestions = js.Array(),
              operationSlug = self.props.`match`.params.get("operationSlug"),
              themeSlug = self.props.`match`.params.get("themeSlug")
            )
          },
          componentWillReceiveProps = (self, nextProps) => {
            self.setState(
              _.copy(value = URIUtils.decodeURI(QueryString.parse(nextProps.location.search).getOrElse("q", "")))
            )
          },
          render =
            (self) => {
              def onChange(event: FocusEvent, parameters: OnChangeExtraParameters): Unit = {
                self.setState(_.copy(value = parameters.newValue))
                // TODO: trigger search and update suggestions
              }

              // TODO: handle theme context : search with more weight to proposals from the same theme
              def onSubmit: (SyntheticEvent) => Unit = (e: SyntheticEvent) => {
                e.preventDefault()
                TrackingService
                  .track(
                    "click-navbar-search-submit",
                    TrackingContext(TrackingLocation.navBar),
                    Map("query" -> self.state.value)
                  )
                val currentValue: String = URIUtils.encodeURI(self.state.value)

                val url: String = self.state.operationSlug.map { operationSlugValue =>
                  s"/${self.props.wrapped.country}/consultation/$operationSlugValue/search?q=$currentValue"
                }.getOrElse(self.state.themeSlug.map { themeSlugValue =>
                  s"/${self.props.wrapped.country}/theme/$themeSlugValue/search?q=$currentValue"
                }.getOrElse(s"/${self.props.wrapped.country}/search?q=$currentValue"))

                self.props.history.push(url)
              }

              <.form(^.onSubmit := onSubmit)(
                <.Autosuggest(
                  ^.suggestions := self.state.suggestions,
                  ^.onSuggestionsFetchRequested := (
                    (event: OnSuggestionFetchRequestedExtraParameters) => self.setState(_.copy(value = event.value))
                  ),
                  ^.onSuggestionsClearRequested := (() => self.setState(State("", js.Array()))),
                  ^.renderSuggestion := ((suggestion: js.Object,
                                          _: OnRenderSuggestionExtraParameters) => SuggestionRender(suggestion)),
                  ^.getSuggestionValue := (
                    (suggestion: js.Object) => suggestion.asInstanceOf[ProposalSuggestion].content
                  ),
                  ^.inputProps := InputProps(
                    value = self.state.value,
                    onChange = onChange,
                    inputType = "search",
                    placeholder = I18n.t("search.form.placeholder")
                  ),
                  ^.theme := autoSuggestTheme
                )(),
                <.style()(SearchFormStyles.render[String])
              )
            }
        )
    )

}

@js.native
trait ProposalSuggestion extends js.Object {
  def id: String
  def title: String
  def content: String
  def tags: js.Array[String]
}

object ProposalSuggestion {
  def apply(id: String, title: String, content: String, tags: js.Array[String]): ProposalSuggestion = {
    Dynamic
      .literal(id = id, title = title, content = content, tags = tags.toJSArray)
      .asInstanceOf[ProposalSuggestion]
  }
}

object SuggestionRender {
  // Use a function to render
  def apply(suggestion: js.Object): ReactElement = {
    // TODO: implement.me when we need to add suggestions in search
    <.span()()

  }
}

object SearchFormStyles extends StyleSheet.Inline {
  import dsl._

  val searchInputWithIconWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), &.before(content := "'\\F002'"))
}
