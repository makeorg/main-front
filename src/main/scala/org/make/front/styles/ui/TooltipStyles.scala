package org.make.front.styles.ui

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._

import scalacss.internal.StyleA

object TooltipStyles extends StyleSheet.Inline {

  import dsl._

  val base: StyleA = style(
    position.absolute,
    zIndex(1),
    width(100.%%),
    right(50.%%),
    transform := "translateX(50%)",
    padding(10.pxToEm()),
    textAlign.center,
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.BackgroundColor.darkGrey),
    (&.after)(
      content := "''",
      position.absolute,
      right(50.%%),
      transform := "translateX(50%)",
      borderRight :=! s"${5.pxToEm().value} solid transparent",
      borderLeft :=! s"${5.pxToEm().value} solid transparent"
    )
  )

  val bottomPositioned: StyleA =
    style(
      base,
      top(100.%%),
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      (&.after)(
        bottom(100.%%),
        borderBottom :=! s"${5.pxToEm().value} solid ${ThemeStyles.BackgroundColor.darkGrey.value}"
      )
    )

  val topPositioned: StyleA =
    style(
      base,
      bottom(100.%%),
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      (&.after)(top(100.%%), borderTop :=! s"${5.pxToEm().value} solid ${ThemeStyles.BackgroundColor.darkGrey.value}")
    )

}
