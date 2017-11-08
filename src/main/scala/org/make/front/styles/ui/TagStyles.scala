package org.make.front.styles.ui

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._

object TagStyles extends StyleSheet.Inline {

  import dsl._

  val basic: StyleA =
    style(
      position.relative,
      display.block,
      paddingLeft(9.pxToEm(11)),
      paddingRight(5.pxToEm(11)),
      marginLeft(5.pxToEm(11)),
      ThemeStyles.Font.circularStdBook,
      fontSize(11.pxToEm()),
      lineHeight(18.pxToEm(11)),
      whiteSpace.nowrap,
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.BackgroundColor.blackTransparent),
      userSelect := "none",
      (&.before)(
        content := "''",
        position.absolute,
        top(`0`),
        right(100.%%),
        width(`0`),
        height(`0`),
        borderTop(9.pxToEm(11), solid, transparent),
        borderBottom(9.pxToEm(11), solid, transparent),
        borderRight(5.pxToEm(11), solid, ThemeStyles.BackgroundColor.blackTransparent)
      ),
      (&.after)(
        content := "''",
        position.absolute,
        top(50.%%),
        left(`0`),
        width(4.pxToEm(11)),
        height(4.pxToEm(11)),
        marginTop(-2.pxToEm(11)),
        borderRadius(50.%%),
        backgroundColor(ThemeStyles.TextColor.white)
      ),
      ThemeStyles.MediaQueries.beyondSmall(
        paddingLeft(11.pxToEm(13)),
        paddingRight(5.pxToEm(13)),
        marginLeft(8.pxToEm(13)),
        fontSize(13.pxToEm()),
        lineHeight(24.pxToEm(13)),
        (&.before)(borderTopWidth(12.pxToEm(13)), borderBottomWidth(12.pxToEm(13)), borderRightWidth(8.pxToEm(13))),
        (&.after)(width(6.pxToEm(13)), height(6.pxToEm(13)), marginTop(-3.pxToEm(13)))
      )
    )

  val activated: StyleA = style(
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.BackgroundColor.black),
    (&.before)(borderRightColor(ThemeStyles.BackgroundColor.black))
  )

}
