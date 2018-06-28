package org.make.front.components.consultation.partners

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.ui.TooltipStyles
import org.make.front.styles.utils._
import scalacss.internal.{Attr, ValueT}

object PartnersStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      &.after(
        Attr.real("content") := "''", clear.both, display.table
      )
    )

  val BorderColor: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.5)

  val item: StyleA =
    style(
      position.relative,
      float.left,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      marginRight(15.pxToPercent(330)),
    )

  val avatar: StyleA =
    style(
      width(60.pxToEm()),
      height(60.pxToEm()),
      borderRadius(60.pxToEm()),
      border(1.pxToEm(), solid, BorderColor),
      overflow.hidden,
      ThemeStyles.MediaQueries.beyondLargeMedium(
        width(50.pxToEm()),
        height(50.pxToEm()),
        borderRadius(50.pxToEm())
      )
    )

  val tooltip: StyleA =
    style(
      TooltipStyles.topPositioned,
      minWidth(200.pxToEm()),
      transition := "opacity .25s ease-in-out",
    )


  val tooltipTrigger: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(
          opacity(1),
          zIndex(1)
        )
      } else styleS(
        opacity(0),
        zIndex(-1)
      )
  )

}
