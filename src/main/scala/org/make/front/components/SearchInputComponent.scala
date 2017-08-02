package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.facades.Autosuggest._
import org.make.front.facades._
import org.scalajs.dom.raw.FocusEvent

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.{Dictionary, Dynamic}
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object SearchInputComponent {
  type Self = React.Self[Unit, State]

  case class State(`value`: String, suggestions: js.Array[js.Object])

  val theme: Dictionary[String] =
    Map[String, String](
      "container" -> SearchInputStyles.container.htmlClass,
      "containerOpen" -> SearchInputStyles.containerOpen.htmlClass,
      "input" -> SearchInputStyles.input.htmlClass,
      "inputOpen" -> SearchInputStyles.inputOpen.htmlClass,
      "inputFocused" -> SearchInputStyles.inputFocused.htmlClass,
      "suggestionsContainer" -> SearchInputStyles.suggestionsContainer.htmlClass,
      "suggestionsContainerOpen" -> SearchInputStyles.suggestionsContainerOpen.htmlClass,
      "suggestionsList" -> SearchInputStyles.suggestionsList.htmlClass,
      "suggestion" -> SearchInputStyles.suggestion.htmlClass,
      "suggestionFirst" -> SearchInputStyles.suggestionFirst.htmlClass,
      "suggestionHighlighted" -> SearchInputStyles.suggestionHighlighted.htmlClass,
      "sectionContainer" -> SearchInputStyles.sectionContainer.htmlClass,
      "sectionContainerFirst" -> SearchInputStyles.sectionContainerFirst.htmlClass,
      "sectionContainerTitle" -> SearchInputStyles.sectionContainerTitle.htmlClass
    ).toJSDictionary

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, State](
        getInitialState = (_) => State("", js.Array()),
        render = (self) => {

          def onChange(event: FocusEvent, parameters: OnChangeExtraParameters): Unit = {
            self.setState(_.copy(value = parameters.newValue))
          }

          def onBlur(event: FocusEvent, selectedObject: js.Object): Unit = {}

          <.div()(
            <.Autosuggest(
              ^.suggestions := js.Array(),
              ^.onSuggestionsFetchRequested := (
                (event: OnSuggestionFetchRequestedExtraParameters) => self.setState(_.copy(value = event.value))
              ),
              ^.onSuggestionsClearRequested := (() => self.setState(State("", js.Array()))),
              ^.renderSuggestion := ((suggestion: js.Object,
                                      _: OnRenderSuggestionExtraParameters) => SuggestionRender(suggestion)),
              ^.getSuggestionValue := (
                (suggestion: js.Object) => suggestion.asInstanceOf[PropositionSuggestion].content
              ),
              ^.inputProps := InputProps(self.state.value, onChange, onBlur, "search", I18n.t("search.placeholder")),
              ^.theme := theme
            )(),
            <.style()(SearchInputStyles.render[String])
          )
        }
      )

}

@js.native
trait PropositionSuggestion extends js.Object {
  def id: String
  def title: String
  def content: String
}

object PropositionSuggestion {
  def apply(id: String, title: String, content: String): PropositionSuggestion = {
    Dynamic.literal(id = id, title = title, content = content).asInstanceOf[PropositionSuggestion]
  }
}

object SuggestionRender {
  // Use a function to render
  def apply(suggestion: js.Object): ReactElement = {
    <.span()()

  }
}

object SearchInputStyles extends StyleSheet.Inline {
  import dsl._

  val container: StyleA = style(border(none))
  val containerOpen: StyleA = style(border(none))
  val input: StyleA =
    style(
      height(40.px),
      width(500.px),
      borderRadius(40.px),
      border(1.px, solid, c"#CCC"),
      backgroundColor(c"#F7F7F7"),
      marginLeft(50.px),
      marginRight(50.px),
      paddingLeft(20.px),
      paddingRight(20.px)
    )
  val inputFocused: StyleA = style(borderColor(c"#3898EC"), outline(none))
  val inputOpen: StyleA = style(border(none))
  val suggestionsContainer: StyleA = style(border(none))
  val suggestionsContainerOpen: StyleA = style(border(none))
  val suggestionsList: StyleA = style(border(none))
  val suggestion: StyleA = style(border(none))
  val suggestionFirst: StyleA = style(border(none))
  val suggestionHighlighted: StyleA = style(border(none))
  val sectionContainer: StyleA = style(border(none))
  val sectionContainerFirst: StyleA = style(border(none))
  val sectionContainerTitle: StyleA = style(border(none))

}
