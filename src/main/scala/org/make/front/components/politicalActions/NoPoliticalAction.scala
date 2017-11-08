package org.make.front.components.politicalActions

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{cone, I18n}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import org.make.front.Main.CssSettings._

object NoPoliticalAction {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "NoPoliticalAction",
        render = (self) => {

          <.article(^.className := NoPoliticalActionStyles.wrapper)(
            <.p(^.className := NoPoliticalActionStyles.imageWrapper)(
              <.img(
                ^.className := NoPoliticalActionStyles.image,
                ^.src := cone.toString,
                ^("data-pin-no-hover") := "true"
              )()
            ),
            <.div(^.className := NoPoliticalActionStyles.contentWrapper)(
              <.p(^.className := Seq(TextStyles.boldText, TextStyles.mediumText, NoPoliticalActionStyles.text))(
                unescape(I18n.t("no-political-action.intro"))
              ),
              <.p(^.className := Seq(TextStyles.boldText, TextStyles.mediumText, NoPoliticalActionStyles.text))(
                unescape(I18n.t("no-political-action.text"))
              )
            ),
            <.style()(NoPoliticalActionStyles.render[String])
          )

        }
      )
}

object NoPoliticalActionStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA = style(display.table, width(100.%%))

  val imageWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.top,
      width(70.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondMedium(verticalAlign.middle, width(120.pxToEm())),
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      textAlign.right
    )

  val contentWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      padding(
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        `0`
      ),
      ThemeStyles.MediaQueries
        .beyondMedium(paddingLeft(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val image: StyleA = style(maxWidth(60.pxToEm()))

  val info: StyleA = style(
    display.inlineBlock,
    verticalAlign.top,
    color(ThemeStyles.TextColor.light),
    marginRight(ThemeStyles.SpacingValue.small.pxToEm())
  )

  val infoIcon: StyleA = style(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val text: StyleA = style(color(ThemeStyles.TextColor.lighter))

  val seeMore: StyleA = style(color(ThemeStyles.ThemeColor.primary))

}
