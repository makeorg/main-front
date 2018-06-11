package org.make.front.styles.ui

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.utils._

object AccordionStyles extends StyleSheet.Inline {

  import dsl._

  val collapseTrigger: StyleA =
    style(
      width(100.%%),
    )

  val collapseIcon: StyleA =
    style(
      float.right,
      transition := "all .25s ease-in-out"
    )

  val collapseIconToggle: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(
          transform := s"rotate(0deg)"
        )
      } else styleS(
        transform := s"rotate(90deg)"
      )
  )

  val collapseWrapper: StyleA =
    style(
      overflow.hidden,
      transition := "opacity .25s ease-in-out",
    )

  val collapseWrapperToggle: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(
          height(`0`),
          opacity(0)
        )
      } else styleS(
        height.auto,
        opacity(1)
      )
  )
}