package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.StringAttributeSpec
import org.make.front.facades.Localize.{DateLocalizeOptions, NumberLocalizeOptions}
import org.make.front.facades.Replacements.Replacements

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

/**
  * Facade for react-i18nify
  *
  * you can use it that way:
  *
  *  <.Translate(^.value := "hello", ^.dangerousHtml := true)()
  *
  *  or, for instance,
  *
     <.span()(I18n.t("hello")),
  *
  */
@js.native
@JSImport("react-i18nify", "I18n")
object I18n extends js.Object {
  type LocalizeOptions = DateLocalizeOptions | NumberLocalizeOptions

  def setLocale(locale: String, rerenderComponents: Boolean = true): Unit = js.native
  def setLocaleGetter(locale: js.Function0[String]): Unit = js.native
  def setTranslations(translations: js.Any, rerenderComponents: Boolean = true): Unit = js.native
  def setTranslationsGetter(translations: js.Function0[js.Object]): Unit = js.native
  def setHandleMissingTranslation(function: js.Function2[String, Replacements, String]): Unit = js.native
  def t(key: String, replacements: Replacements = Replacements()): String = js.native
  def l(key: String, options: LocalizeOptions): String = js.native
  def l(date: js.Date, options: DateLocalizeOptions): String = js.native
  def forceComponentsUpdate(): Unit = js.native

}

object Replacements {
  type Replacements = js.Dictionary[String]

  def apply(replacements: (String, String)*): Replacements = {
    Map(replacements: _*).toJSDictionary
  }
}

@js.native
@JSImport("react-i18nify", "Translate")
object NativeTranslate extends ReactClass

object Translate {

  implicit class TranslateVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Translate: ReactClassElementSpec = elements(NativeTranslate)
  }

  implicit class TranslateVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val value = StringAttributeSpec("value")
    lazy val count = NativeIntAttribute("count")
    lazy val dangerousHtml = NativeBooleanAttribute("dangerousHTML")
  }

}

@js.native
@JSImport("react-i18nify", "Localize")
object NativeLocalize extends ReactClass

object Localize {

  implicit class LocalizeVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Localize: ReactClassElementSpec = elements(NativeTranslate)
  }

  implicit class LocalizeVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val value = StringAttributeSpec("value")
    lazy val dateFormat = StringAttributeSpec("dateFormat")
    lazy val options = NativeIntAttribute("options")
    lazy val dangerousHtml = NativeBooleanAttribute("dangerousHTML")
  }

  @js.native
  trait DateLocalizeOptions extends js.Object {
    def dateFormat: String
  }

  object DateLocalizeOptions {
    def apply(key: String): DateLocalizeOptions = {
      js.Dynamic.literal(dateFormat = key).asInstanceOf[DateLocalizeOptions]
    }
  }

  // @see https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/NumberFormat
  @js.native
  trait NumberLocalizeOptions extends js.Object {
    def localeMatcher: js.UndefOr[String]
    def style: js.UndefOr[String]
    def currency: js.UndefOr[String]
    def currencyDisplay: js.UndefOr[String]
    def useGrouping: js.UndefOr[Boolean]
    def minimumIntegerDigits: js.UndefOr[Int]
    def minimumFractionDigits: js.UndefOr[Int]
    def maximumFractionDigits: js.UndefOr[Int]
    def minimumSignificantDigits: js.UndefOr[Int]
    def maximumSignificantDigits: js.UndefOr[Int]
  }

  object NumberLocalizeOptions {
    def apply(localeMatcher: Option[String] = None,
              style: Option[String] = None,
              currency: Option[String] = None,
              currencyDisplay: Option[String] = None,
              useGrouping: Option[Boolean] = None,
              minimumIntegerDigits: Option[Int] = None,
              minimumFractionDigits: Option[Int] = None,
              maximumFractionDigits: Option[Int] = None,
              minimumSignificantDigits: Option[Int] = None,
              maximumSignificantDigits: Option[Int] = None): NumberLocalizeOptions = {

      js.Dynamic
        .literal(
          localeMatcher = localeMatcher.orUndefined,
          style = style.orUndefined,
          currency = currency.orUndefined,
          currencyDisplay = currencyDisplay.orUndefined,
          useGrouping = useGrouping.orUndefined,
          minimumFractionDigits = minimumFractionDigits.orUndefined,
          maximumFractionDigits = maximumFractionDigits.orUndefined,
          minimumIntegerDigits = minimumIntegerDigits.orUndefined,
          minimumSignificantDigits = minimumSignificantDigits.orUndefined,
          maximumSignificantDigits = maximumSignificantDigits.orUndefined
        )
        .asInstanceOf[NumberLocalizeOptions]

    }
  }

}
