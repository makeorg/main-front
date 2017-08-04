package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.{Attribute, AttributeSpec}
import org.scalajs.dom.raw.FocusEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * Facade for react-autosuggest
  *
  * you can use it that way:
  *
  * <.Autosuggest(
  *   ^.suggestions := Seq(
  *     Map(
  *       title ->  "Ma proposition 1",
  *       content -> "the content of my proposition 1"
  *     ),
  *     Map(
  *       title ->  "Ma proposition 2",
  *       content -> "the content of my proposition 2
  *     )
  *   )
  *   ^.onSuggestionsFetchRequested := (value: String))()
  *
  */
@js.native
@JSImport("react-autosuggest", "default")
object NativeAutosuggest extends ReactClass

object Autosuggest {

  type OnSuggestionsFetchRequested = js.Function1[OnSuggestionFetchRequestedExtraParameters, Unit]
  type OnSuggestionsClearRequested = js.Function0[Unit]
  type GetSuggestionValue = js.Function1[js.Object, String]
  type RenderSuggestion = js.Function2[js.Object, OnRenderSuggestionExtraParameters, ReactElement]
  type RenderInput = js.Function1[InputProps, ReactElement]

  implicit class AutosuggestVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Autosuggest: ReactClassElementSpec = elements(NativeAutosuggest)
  }

  case class OnSuggestionsFetchRequestedAttribute(name: String) extends AttributeSpec {
    def :=(
      onSuggestionsFetchRequested: (OnSuggestionFetchRequestedExtraParameters) => Unit
    ): Attribute[OnSuggestionsFetchRequested] =
      Attribute(name = name, value = onSuggestionsFetchRequested, AS_IS)
  }

  case class OnSuggestionsClearRequestedAttribute(name: String) extends AttributeSpec {
    def :=(onSuggestionsClearRequested: () => Unit): Attribute[OnSuggestionsClearRequested] =
      Attribute(name = name, value = onSuggestionsClearRequested, AS_IS)
  }

  case class GetSuggestionValueAttribute(name: String) extends AttributeSpec {
    def :=(getSuggestionValue: (js.Object) => String): Attribute[GetSuggestionValue] =
      Attribute(name = name, value = getSuggestionValue, AS_IS)
  }

  case class RenderSuggestionAttribute(name: String) extends AttributeSpec {
    def :=(
      renderSuggestion: js.Function2[js.Object, OnRenderSuggestionExtraParameters, ReactElement]
    ): Attribute[RenderSuggestion] =
      Attribute(name = name, value = renderSuggestion, AS_IS)
  }

  case class InputPropsAttribute(name: String) extends AttributeSpec {
    def :=(inputProps: InputProps): Attribute[InputProps] =
      Attribute(name = name, value = inputProps, AS_IS)
  }

  case class ThemeAttribute(name: String) extends AttributeSpec {
    def :=(theme: js.Dictionary[String]): Attribute[js.Dictionary[String]] =
      Attribute(name = name, value = theme, AS_IS)
  }

  case class RenderInputComponent(name: String) extends AttributeSpec {
    def :=(value: RenderInput): Attribute[RenderInput] = Attribute(name = name, value = value, AS_IS)
  }

  implicit class AutosuggestVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val suggestions: NativeArrayAttribute = NativeArrayAttribute("suggestions")
    lazy val onSuggestionsFetchRequested: OnSuggestionsFetchRequestedAttribute = OnSuggestionsFetchRequestedAttribute(
      "onSuggestionsFetchRequested"
    )
    lazy val onSuggestionsClearRequested: OnSuggestionsClearRequestedAttribute = OnSuggestionsClearRequestedAttribute(
      "onSuggestionsClearRequested"
    )
    lazy val getSuggestionValue: GetSuggestionValueAttribute = GetSuggestionValueAttribute("getSuggestionValue")
    lazy val renderSuggestion: RenderSuggestionAttribute = RenderSuggestionAttribute("renderSuggestion")
    lazy val inputProps: InputPropsAttribute = InputPropsAttribute("inputProps")
    lazy val theme: ThemeAttribute = ThemeAttribute("theme")
    lazy val renderInputComponent: RenderInputComponent = RenderInputComponent("renderInputComponent")
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
            onBlur: (FocusEvent, js.Object)                 => Unit,
            inputType: String,
            placeholder: String): InputProps = {
    js.Dynamic
      .literal(value = value, onChange = onChange, onBlur = onBlur, `type` = inputType, placeholder = placeholder)
      .asInstanceOf[InputProps]
  }
}
