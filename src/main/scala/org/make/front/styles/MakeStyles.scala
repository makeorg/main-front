package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{FontFace, ValueT}

object MakeStyles extends StyleSheet.Inline {
  import dsl._

    //todo: implement h1
    val title1: StyleA = style()
    val title2: StyleA = style(
      fontSize(3.4.rem),
      fontFamily(Font.tradeGothicLTStd),
      fontWeight.bold
    )
    //todo: implement h3
    val title3: StyleA = style()

    // colors
    object Color {
      val black: ValueT[ValueT.Color] = rgb(0, 0, 0)
      val pink: ValueT[ValueT.Color] = c"#ed1844"
    }

    // font
    object Font {
      val tradeGothicLTStd: FontFace[String] = fontFace("TradeGothicLTStd")(
        _.src("url(TradeGothicLTStd.woff)")
      )
    }

    // background
    object Background {
      val footer: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
    }
}



