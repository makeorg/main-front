package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.facades.Autosuggest._
import org.make.front.facades._
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.raw.FocusEvent

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.{Dictionary, Dynamic, URIUtils}
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object SearchInputComponent {
  type Self = React.Self[Unit, State]

  case class State(value: String, suggestions: js.Array[js.Object])

  val autoSuggestTheme: Dictionary[String] =
    Map[String, String](
      "container" -> SearchInputStyles.container.htmlClass,
      "containerOpen" -> SearchInputStyles.containerOpen.htmlClass,
      "input" -> Seq(MakeStyles.Form.inputText.htmlClass, SearchInputStyles.input.htmlClass).mkString(" "),
      "inputOpen" -> SearchInputStyles.inputOpen.htmlClass,
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

              <.form(
                ^.className := Seq(BulmaStyles.Element.hasIconsLeft, BulmaStyles.Element.control),
                ^.onSubmit := onSubmit
              )(
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
                <.span(^.className := Seq(BulmaStyles.Element.icon, BulmaStyles.Element.isLeft))(
                  <.i(^.className := Seq(FontAwesomeStyles.search, SearchInputStyles.redSearchIcon))()
                ),
                <.style()(SearchInputStyles.render[String])
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

object SearchInputStyles extends StyleSheet.Inline {
  import dsl._

  val container: StyleA = style(border(none), verticalAlign.middle, padding(2.rem), width(100.%%))
  val containerOpen: StyleA = style()
  val input: StyleA =
    style(width(100.%%), minWidth(30.rem), maxWidth(200.rem))
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

  val redSearchIcon: StyleA = style(color.red, marginTop(5.5F.rem), marginLeft(6.rem))

}
