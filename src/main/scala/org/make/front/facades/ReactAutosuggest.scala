package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.{Attribute, AttributeSpec}
import org.scalajs.dom.raw.{Event, FocusEvent}
import scala.scalajs.js.JSConverters._
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
@JSImport("react-autosuggest", "Autosuggest")
object NativeAutosuggest extends ReactClass

object Autosuggest {

  type onSuggestionsFetchRequested = js.Function1[String, Unit]
  type onSuggestionsClearRequested = js.Function0[Unit]
  type getSuggestionValue = js.Function1[js.Dictionary[String], String]
  type renderSuggestion = js.Function3[js.Dictionary[String], String, String, ReactElement]

  implicit class AutosuggestVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Autosuggest: ReactClassElementSpec = elements(NativeAutosuggest)
  }

  case class onSuggestionsFetchRequestedAttribute(name: String) extends AttributeSpec {
    def :=(onSuggestionsFetchRequested: (String) => Unit): Attribute[onSuggestionsFetchRequested] =
      Attribute(name = name, value = onSuggestionsFetchRequested, AS_IS)
  }

  case class onSuggestionsClearRequestedAttribute(name: String) extends AttributeSpec {
    def :=(onSuggestionsClearRequested: () => Unit): Attribute[onSuggestionsClearRequested] =
      Attribute(name = name, value = onSuggestionsClearRequested, AS_IS)
  }

  case class getSuggestionValueAttribute(name: String) extends AttributeSpec {
    def :=(getSuggestionValue: (js.Dictionary[String]) => String): Attribute[getSuggestionValue] =
      Attribute(name = name, value = getSuggestionValue, AS_IS)
  }

  case class renderSuggestionAttribute(name: String) extends AttributeSpec {
    def :=(renderSuggestion: js.Function3[js.Dictionary[String], String, String, ReactElement]): Attribute[renderSuggestion] =
      Attribute(name = name, value = renderSuggestion, AS_IS)
  }

  case class inputPropsAttribute(name: String) extends AttributeSpec {
    def :=(inputProps: InputProps): Attribute[InputProps] =
      Attribute(name = name, value = inputProps, AS_IS)
  }

  implicit class AutosuggestVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val suggestions = NativeArrayAttribute("suggestions")
    lazy val onSuggestionsFetchRequested = onSuggestionsFetchRequestedAttribute("onSuggestionsFetchRequested")
    lazy val onSuggestionsClearRequested = onSuggestionsClearRequestedAttribute("onSuggestionsClearRequested")
    lazy val getSuggestionValue = getSuggestionValueAttribute("getSuggestionValue")
    lazy val renderSuggestion = renderSuggestionAttribute("renderSuggestion")
    lazy val inputProps = inputPropsAttribute("inputProps")
  }
}

@js.native
trait InputProps extends js.Object {
  def value: String          // usually comes from the application state
  def onChange: js.Function2[Event, String, Unit] // called every time the input value changes
  def onBlur: js.Function1[FocusEvent, _]
  def `type`: String
  def placeholder: String
}

object InputProps {
  def apply(
             value: String,
             onchange: (Event, String) => Unit,
             onblur: (FocusEvent, js.Object) => Unit,
             inputType: String,
             placeholder: String
           ): InputProps = {
    js.Dynamic
      .literal(
        value = value,
        onChange = onchange,
        onBlur = onblur,
        `type` = inputType,
        placeholder = placeholder
      )
      .asInstanceOf[InputProps]
  }
}
