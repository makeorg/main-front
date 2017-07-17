package org.make.front

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.AppComponent
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  override def main(): Unit = {
    ReactDOM.render(
      <.BrowserRouter()(
        <(AppComponent()).empty
      ),
      dom.document.getElementById("make-app")
    )
  }
}
