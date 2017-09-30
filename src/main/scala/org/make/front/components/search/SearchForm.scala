package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.facades.Autosuggest._
import org.make.front.facades._
import org.make.front.helpers.QueryString
import org.make.front.styles.ThemeStyles
import org.make.front.styles.ui.InputStyles
import org.scalajs.dom.raw.FocusEvent

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.{Dictionary, Dynamic, URIUtils}
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object SearchForm {
  type Self = React.Self[Unit, State]

  case class State(value: String, suggestions: js.Array[js.Object])

  val autoSuggestTheme: Dictionary[String] =
    Map[String, String](
      "container" -> Seq(
        InputStyles.wrapper.htmlClass,
        InputStyles.withIcon.htmlClass,
        SearchFormStyles.searchInputWithIconWrapper.htmlClass
      ).mkString(" "),
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
        .createClass[Unit, State](
          getInitialState = (self) => {
            State(URIUtils.decodeURI(QueryString.parse(self.props.location.search).getOrElse("q", "")), js.Array())
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
                val currentValue: String = URIUtils.encodeURI(self.state.value)
                self.props.history.push(s"/search?q=$currentValue")
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
                    placeholder = I18n.t("content.header.searchPlaceholder")
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
  def apply(id: String, title: String, content: String, tags: Seq[String]): ProposalSuggestion = {
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
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), (&.before)(content := "'\\F002'"))
}
