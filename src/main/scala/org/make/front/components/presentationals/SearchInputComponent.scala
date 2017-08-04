package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.facades.Autosuggest._
import org.make.front.facades._
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles}
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

  def renderInput(props: InputProps): ReactElement = {
    <.p(^.className := Seq(BulmaStyles.Element.hasIconsLeft, BulmaStyles.Element.control))(
      <.input(^.className := SearchInputStyles.input, ^.placeholder := props.placeholder, ^.value := props.value)(),
      <.span(^.className := Seq(BulmaStyles.Element.icon, BulmaStyles.Element.isLeft))(
        <.i(^.className := Seq(FontAwesomeStyles.search, SearchInputStyles.redSearchIcon))()
      )
    )
  }

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, State](
        getInitialState = (_) => State("", js.Array()),
        render = (self) => {

          def onChange(event: FocusEvent, parameters: OnChangeExtraParameters): Unit = {
            self.setState(_.copy(value = parameters.newValue))
          }

          def onBlur(event: FocusEvent, selectedObject: js.Object): Unit = {}

          <.div(^.className := Seq(BulmaStyles.Element.hasIconsLeft, BulmaStyles.Element.control))(
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
            <.span(^.className := Seq(BulmaStyles.Element.icon, BulmaStyles.Element.isLeft))(
              <.i(^.className := Seq(FontAwesomeStyles.search, SearchInputStyles.redSearchIcon))()
            ),
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

  val container: StyleA = style(border(none), verticalAlign.middle, padding(20.px), width(100.%%))
  val containerOpen: StyleA = style()
  val input: StyleA =
    style(
      height(40.px),
      minWidth(200.px),
      width(100.%%),
      borderRadius(40.px),
      border(1.px, solid, c"#CCC"),
      backgroundColor(c"#F7F7F7"),
      paddingLeft(35.px),
      paddingRight(20.px),
      minWidth(300.px),
      maxWidth(2000.px)
    )
  val inputFocused: StyleA = style(borderColor(c"#3898EC"), outline(none))
  val inputOpen: StyleA = style()
  val suggestionsContainer: StyleA = style()
  val suggestionsContainerOpen: StyleA = style()
  val suggestionsList: StyleA = style()
  val suggestion: StyleA = style()
  val suggestionFirst: StyleA = style()
  val suggestionHighlighted: StyleA = style()
  val sectionContainer: StyleA = style()
  val sectionContainerFirst: StyleA = style()
  val sectionContainerTitle: StyleA = style()

  val redSearchIcon: StyleA = style(color.red, marginTop(55.px), marginLeft(60.px))

}
