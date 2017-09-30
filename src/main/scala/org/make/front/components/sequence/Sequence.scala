package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.models.{Sequence => SequenceModel}

import scalacss.DevDefaults._

object Sequence {

  final case class SequenceProps(sequence: SequenceModel)

  lazy val reactClass: ReactClass =
    React.createClass[SequenceProps, Unit](displayName = "Sequence", getInitialState = { self =>
      }, render = { self =>
      <.div()()
    })
}

object SequenceStyles extends StyleSheet.Inline {
  import dsl._

}
