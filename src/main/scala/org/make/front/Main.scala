package org.make.front

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.AppComponent
import org.make.front.facades.I18n
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {

  val CssSettings = scalacss.devOrProdDefaults;

  override def main(): Unit = {
    Translations.loadTranslations()
    I18n.setLocale("fr")

    ReactDOM.render(<.BrowserRouter()(<(AppComponent()).empty), dom.document.getElementById("make-app"))
  }
}
