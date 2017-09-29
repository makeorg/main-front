package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.models.{Sequence => SequenceModel}

import scalacss.DevDefaults._
import scalacss.internal.Length

object Sequence {

  final case class SequenceProps(sequence: SequenceModel)

  lazy val reactClass: ReactClass =
    React.createClass[SequenceProps, Unit](getInitialState = { self =>
      }, render = { self =>
      <.div()("Sequence")
    })
}

object SequenceStyles extends StyleSheet.Inline {
  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }
}
