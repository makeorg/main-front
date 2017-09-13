package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.DslBase.ToStyle
import scalacss.internal.Length
import scalacss.internal.PseudoType.{Class, Element}

object InputStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  /** TODO: pseudo class to customise placeholder */
  def pseudoElement(value: String) = Pseudo.Custom(value, Element)

  def pseudoClass(value: String) = Pseudo.Custom(value, Class)

  val webkitPlaceholder = pseudoElement("::-webkit-input-placeholder")
  val mozPlaceholder = pseudoElement("::-moz-placeholder")
  val msPlaceholder = pseudoClass(":-ms-input-placeholder")

  def placeholder(styles: ToStyle*) =
    styleS(webkitPlaceholder(styles: _*), mozPlaceholder(styles: _*), msPlaceholder(styles: _*))

  val basic: StyleA =
    style(
      height(40.pxToEm(16)),
      width(100.%%),
      padding :=! s"0 ${20.pxToEm(16).value}",
      boxSizing.borderBox,
      borderRadius(20.pxToEm(16)),
      border :=! s"1px solid ${ThemeStyles.BorderColor.light.value}",
      fontSize(16.pxToEm()),
      lineHeight.normal,
      fontFamily.inherit,
      placeholder(color(ThemeStyles.TextColor.lighter))
    )

  val withIcon: StyleA =
    style(paddingLeft(50.pxToEm(16)))

  val withIconWrapper: StyleA =
    style(
      position.relative,
      display.block,
      (&.before)(
        position.absolute,
        top(0.%%),
        left(0.%%),
        width(50.pxToEm(20)),
        ThemeStyles.Font.fontAwesome,
        fontSize(20.pxToEm()),
        lineHeight(40.pxToEm(20)),
        textAlign.center,
        color(ThemeStyles.ThemeColor.primary)
      )
    )

  val biggerBeyondMedium: StyleA = style(
    ThemeStyles.MediaQueries.beyondSmall(
      fontSize(18.pxToEm()),
      height(50.pxToEm(18)),
      padding :=! s"0 ${25.pxToEm(18).value}",
      borderRadius(25.pxToEm(18))
    )
  )

  val withIconBiggerBeyondMedium: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(paddingLeft(70.pxToEm(18))))

  val withIconBiggerBeyondMediumWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries
        .beyondSmall((&.before)(fontSize(28.pxToEm()), width(70.pxToEm(28)), lineHeight(50.pxToEm(28))))
    )

}
