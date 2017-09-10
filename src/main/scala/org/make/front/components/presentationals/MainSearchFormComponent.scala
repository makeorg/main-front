package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.facades.Autosuggest._
import org.make.front.facades._
import org.make.front.styles.{InputStyles, ThemeStyles}
import org.scalajs.dom.raw.FocusEvent

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.{Dictionary, Dynamic, URIUtils}
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object MainSearchFormComponent {
  type Self = React.Self[Unit, State]

  case class State(value: String, suggestions: js.Array[js.Object])

  val autoSuggestTheme: Dictionary[String] =
    Map[String, String](
      "container" -> Seq(
        InputStyles.withIconWrapper.htmlClass,
        MainSearchFormStyles.searchInputWithIconWrapper.htmlClass
      ).mkString(" "),
      "containerOpen" -> "",
      "input" -> Seq(
        InputStyles.basic.htmlClass,
        InputStyles.withIcon.htmlClass,
        MainSearchFormStyles.searchInput.htmlClass
      ).mkString(" "),
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
          getInitialState = (_) => State("", js.Array()),
          render =
            (self) => {

              def onChange(event: FocusEvent, parameters: OnChangeExtraParameters): Unit = {
                self.setState(_.copy(value = parameters.newValue))
                // TODO: trigger search and update suggestions
              }

              val onSubmit: () => Boolean = () => {
                val currentValue = URIUtils.encodeURI(self.state.value)
                self.props.history.push(s"/search?q=$currentValue")
                false
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
                    self.state.value,
                    onChange,
                    "search",
                    I18n.t("content.header.searchPlaceholder")
                  ),
                  ^.theme := autoSuggestTheme
                )(),
                <.style()(MainSearchFormStyles.render[String])
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

object MainSearchFormStyles extends StyleSheet.Inline {
  import dsl._

  val searchInput: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey))

  val searchInputWithIconWrapper: StyleA =
    style(position.relative, (&.before)(content := "'\\F002'"))
}
