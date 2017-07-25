package org.make.front

import org.make.front.facades.I18n.setTranslations

import scala.scalajs.js.JSON
import scala.scalajs.js

object Translations {

  def loadTranslations(): Unit = {
    setTranslations(JSON.parse(translations).asInstanceOf[js.Object])
  }

  val translations: String =
    """
      |{
      |  "fr": {
      |    "hello": "Bonjour, le Monde <br />"
      |  },
      |  "en": {
      |    "hello": "Hello, World"
      |  }
      |}
    """.stripMargin

}
