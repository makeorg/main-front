package org.make.front.styles.ui

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import scalacss.internal.Dsl
import scalacss.internal.DslBase.ToStyle
import scalacss.internal.PseudoType.{Class, Element}

object InputStyles extends StyleSheet.Inline {

  import dsl._

  def pseudoElement(value: String) = Pseudo.Custom(value, Element)

  def pseudoClass(value: String) = Pseudo.Custom(value, Class)

  val webkitPlaceholder: Pseudo.Custom = pseudoElement("::-webkit-input-placeholder")
  val mozPlaceholder: Pseudo.Custom = pseudoElement("::-moz-placeholder")
  val msPlaceholder: Pseudo.Custom = pseudoClass(":-ms-input-placeholder")

  def placeholder(styles: ToStyle*): Dsl.StyleS =
    styleS(webkitPlaceholder(styles: _*), mozPlaceholder(styles: _*), msPlaceholder(styles: _*))

  val wrapper: StyleA =
    style(
      display.block,
      minHeight(30.pxToEm()),
      width(100.%%),
      padding :=! s"0 ${20.pxToEm().value}",
      lineHeight(0),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxSizing.borderBox,
      border :=! s"1px solid ${ThemeStyles.BorderColor.lighter.value}",
      borderRadius(20.pxToEm()),
      overflow.hidden,
      transition := "color .2s ease-in-out, background .2s ease-in-out, border .2s ease-in-out",
      ThemeStyles.MediaQueries
        .beyondSmall(minHeight(40.pxToEm()), padding :=! s"0 ${20.pxToEm().value}", borderRadius(20.pxToEm())),
      unsafeChild("input")(
        height(28.pxToEm(13)),
        width(100.%%),
        padding(`0`),
        fontSize(13.pxToEm()),
        ThemeStyles.MediaQueries.beyondSmall(minHeight(38.pxToEm(16)), fontSize(16.pxToEm())),
        lineHeight.normal,
        ThemeStyles.Font.circularStdBook,
        border.none,
        background := "none",
        placeholder(color(ThemeStyles.TextColor.lighter))
      ),
      unsafeChild("textarea")(
        height(16.pxToEm(13)),
        width(100.%%),
        boxSizing.contentBox,
        padding(`0`),
        paddingTop(6.pxToEm(13)),
        paddingBottom(6.pxToEm(13)),
        fontSize(13.pxToEm()),
        ThemeStyles.MediaQueries
          .beyondSmall(
            height(20.pxToEm(16)),
            paddingTop(9.pxToEm(16)),
            paddingBottom(9.pxToEm(16)),
            fontSize(16.pxToEm())
          ),
        ThemeStyles.Font.circularStdBook,
        border.none,
        background := "none",
        resize.none
      )
    )

  val withIcon: StyleA =
    style(
      position.relative,
      paddingLeft(30.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingLeft(50.pxToEm())),
      (&.before)(
        position.absolute,
        top(`0`),
        left(`0`),
        lineHeight(28.pxToEm(13)),
        width(30.pxToEm(13)),
        fontSize(13.pxToEm()),
        ThemeStyles.Font.fontAwesome,
        textAlign.center,
        color(ThemeStyles.ThemeColor.primary),
        ThemeStyles.MediaQueries.beyondSmall(fontSize(20.pxToEm()), lineHeight(38.pxToEm(20)), width(50.pxToEm(20)))
      )
    )

  val bigger: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(
        minHeight(50.pxToEm()),
        padding :=! s"0 ${25.pxToEm().value}",
        borderRadius(25.pxToEm()),
        unsafeChild("input")(minHeight(48.pxToEm(18)), fontSize(18.pxToEm())),
        unsafeChild("textarea")(
          height(30.pxToEm(18)),
          paddingTop(13.pxToEm(18)),
          paddingBottom(13.pxToEm(18)),
          fontSize(18.pxToEm())
        )
      )
    )

  val biggerWithIcon: StyleA =
    style(
      ThemeStyles.MediaQueries
        .beyondMedium(bigger, paddingLeft(70.pxToEm()), (&.before)(fontSize(28.pxToEm()), lineHeight(48.pxToEm(28))))
    )

  val withError: StyleA =
    style(
      color(ThemeStyles.TextColor.danger),
      borderColor(ThemeStyles.BorderColor.danger),
      backgroundColor :=! s"${ThemeStyles.BackgroundColor.danger.value} !important"
    )

  val errorMessage: StyleA =
    style(
      display.block,
      marginTop(0.3.em),
      TextStyles.smallerText,
      ThemeStyles.Font.circularStdBook,
      textAlign.left,
      color(ThemeStyles.TextColor.danger)
    )
}
