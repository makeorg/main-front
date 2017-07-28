package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.facades.Autosuggest._
import org.make.front.facades.InputProps
import org.scalajs.dom.raw.{FocusEvent, Event}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._


object SearchInputComponent {
  type Self = React.Self[Unit, State]

  case class State(
    `value`: String,
    suggestions: js.Array[js.Object]
  )

  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, State](
    getInitialState = (_) => State("", js.Array()),
    render = (self) => {

      def onChange(event: Event, newValue: String): Unit = {
        self.setState(_.copy(value = newValue))
      }

      def onBlur(event: FocusEvent, selectedObject: js.Object): Unit = {
      }

      <.div()(
        <.Autosuggest(
          ^.suggestions := suggestions,
          ^.onSuggestionsFetchRequested := ((value: String) => self.setState(_.copy(`value` = value))),
          ^.onSuggestionsClearRequested := (() => self.setState(State("", js.Array()))),
          ^.renderSuggestion := ((suggestion: js.Dictionary[String], query: String, isHiglighted: String) => SuggestionRender(suggestion)),
          ^.getSuggestionValue := ((suggestion: js.Dictionary[String]) => suggestion("title")),
          ^.inputProps := InputProps(self.state.value, onChange, onBlur, "search", "mon placeholder")
        )()
      )
    }
  )

  val suggestions: js.Array[js.Object] = Seq(
    Map(
      "id" ->  "aa",
      "title" ->  "Ma proposition 1",
      "content" -> "the content of my proposition 1"
    ).toJSDictionary.asInstanceOf[js.Object],
    Map(
      "id" ->  "bb",
      "title" ->  "Ma proposition 2",
      "content" -> "the content of my proposition 2"
    ).toJSDictionary.asInstanceOf[js.Object]
  ).toJSArray
}

object SuggestionRender {
  // Use a function to render
  def apply(suggestion: js.Dictionary[String]): ReactElement = {
    <.div()(
      suggestion("title")
    )
  }
}
