package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalajs.dom.raw.FocusEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * Facade for react-autosuggest
  *
  * you can use it that way:
  *
  * <.Autosuggest(
  *   ^.suggestions := js.Array(
  *     ProposalSuggestion(
  *       id = "id-1",
  *       title = "tile - proposal 1",
  *       content = "content - proposal 1",
  *       tags = ["tag-1", "tag-2"]
  *     ),
  *     ProposalSuggestion(
  *       id = "id-2",
  *       title = "tile - proposal 2",
  *       content = "content - proposal 2",
  *       tags = ["tag-3", "tag-4"]
  *     )
  *   ).toJSArray,
  *   ^.onSuggestionsFetchRequested := (value: String))()
  *
  */
@js.native
@JSImport("react-autosuggest", "default")
object NativeAutosuggest extends ReactClass

object Autosuggest {

  implicit class AutosuggestVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Autosuggest: ReactClassElementSpec = elements(NativeAutosuggest)
  }

  implicit class AutosuggestVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val suggestions: NativeArrayAttribute = NativeArrayAttribute("suggestions")
    lazy val onSuggestionsFetchRequested: NativeFunction1Attribute[OnSuggestionFetchRequestedExtraParameters, Unit] =
      NativeFunction1Attribute("onSuggestionsFetchRequested")
    lazy val onSuggestionsClearRequested: NativeFunction0Attribute[Unit] =
      NativeFunction0Attribute[Unit]("onSuggestionsClearRequested")
    lazy val getSuggestionValue: NativeFunction1Attribute[js.Object, String] = NativeFunction1Attribute(
      "getSuggestionValue"
    )
    lazy val renderSuggestion: NativeFunction2Attribute[js.Object, OnRenderSuggestionExtraParameters, ReactElement] =
      NativeFunction2Attribute("renderSuggestion")
    lazy val inputProps: NativeJsObjectAttributeSpec[InputProps] = NativeJsObjectAttributeSpec("inputProps")
    lazy val theme: NativeJsObjectAttributeSpec[js.Dictionary[String]] = NativeJsObjectAttributeSpec("theme")
    lazy val renderInputComponent: NativeFunction1Attribute[InputProps, ReactElement] = NativeFunction1Attribute(
      "renderInputComponent"
    )
  }
}

@js.native
trait InputProps extends js.Object {
  def value: String // usually comes from the application state
  def onChange: js.Function2[FocusEvent, OnChangeExtraParameters, Unit] // called every time the input value changes
  def onBlur: js.Function1[FocusEvent, _]
  def `type`: String
  def placeholder: String
}

@js.native
trait OnChangeExtraParameters extends js.Object {
  def method: String
  def newValue: String
}

@js.native
trait OnSuggestionFetchRequestedExtraParameters extends js.Object {
  def value: String
  def reason: String
}

@js.native
trait OnRenderSuggestionExtraParameters extends js.Object {
  def isHighlighted: Boolean
  def query: String
}

object InputProps {
  def apply(value: String,
            onChange: (FocusEvent, OnChangeExtraParameters) => Unit,
            inputType: String,
            placeholder: String): InputProps = {
    js.Dynamic
      .literal(value = value, onChange = onChange, `type` = inputType, placeholder = placeholder)
      .asInstanceOf[InputProps]
  }
}
