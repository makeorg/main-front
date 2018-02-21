package org.make.front

import org.make.front.facades.I18n.setTranslations
import org.make.front.facades.{translationsEnGB, translationsFrFR, translationsItIT}

import scala.scalajs.js

object Translations {

  def loadTranslations(): Unit = {
    setTranslations(
      js.Dictionary[js.Object]("fr" -> translationsFrFR, "en" -> translationsEnGB, "it" -> translationsItIT)
    )
  }
}
