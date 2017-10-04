package org.make.front.components.spinner

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object Spinner {

  val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "Spinner",
        render = (self) => {
          <.p(^.className := SpinnerStyles.wrapper)(
            <("svg")(
              ^("xmlns") := "http://www.w3.org/2000/svg",
              ^("x") := "0",
              ^("y") := "0",
              ^("width") := "66",
              ^("height") := "66",
              ^("viewBox") := "0 0 66 66"
            )(
              <("linearGradient")(^("id") := "gradient")(
                <("stop")(^("offset") := "0", ^.style := Map("stop-color" -> "#000000", "stop-opacity" -> 0))(),
                <("stop")(^("offset") := "1", ^.style := Map("stop-color" -> "#000000", "stop-opacity" -> 1))()
              ),
              <("path")(
                ^("fill") := "url(#gradient)",
                ^("d") := "M0,33C0,14.8,14.8,0,33,0s33,14.8,33,33S51.2,66,33,66S0,51.2,0,33z M6,33c0,14.9,12.1,27,27,27s27-12.1,27-27 S47.9,6,33,6S6,18.1,6,33z"
              )(
                <("animateTransform")(
                  ^("attributeType") := "xml",
                  ^("attributeName") := "transform",
                  ^("type") := "rotate",
                  ^("from") := "0 33 33",
                  ^("to") := "360 33 33",
                  ^("dur") := "0.5s",
                  ^("repeatCount") := "indefinite"
                )()
              )
            ),
            <.style()(SpinnerStyles.render[String])
          )
        }
      )
}

object SpinnerStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA = style(textAlign.center, opacity(0.3))

}
