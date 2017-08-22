package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.AppComponentStyles
import org.make.front.facades.Translate.TranslateVirtualDOMElements

import scalacss.DevDefaults._

object ProposalMatrixComponent {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    render = (self) =>
      <.div()(
        <.h2(^.className := AppComponentStyles.title2)(
          <.Translate(
            ^.value := "content.theme.matrix.title"
          )()
        ),
        <.TagFilterComponent()()
      )
  )
}
