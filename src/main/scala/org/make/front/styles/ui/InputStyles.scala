package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{Length}

object InputStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  /** TODO: pseudo class to customise placeholder */
  /*def pseudoElement(value: String) = Pseudo.Custom(value, PseudoElement)
  def pseudoClass(value: String) = Pseudo.Custom(value, PseudoClass)

  val webkitPlaceholder = pseudoElement("::-webkit-input-placeholder")
  val mozPlaceholder = pseudoElement("::-moz-placeholder")
  val msPlaceholder = pseudoClass(":-ms-input-placeholder")

  def placeholder(styles: ToStyle*) = styleS(webkitPlaceholder(styles: _*), mozPlaceholder(styles: _*), msPlaceholder(styles: _*))*/

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
      fontFamily.inherit
    )

  val withIcon: StyleA =
    style(paddingLeft(40.pxToEm(16)))

  val withIconWrapper: StyleA =
    style(
      position.relative,
      (&.before)(
        position.absolute,
        top(0.%%),
        left(15.pxToEm(20)),
        ThemeStyles.Font.fontAwesome,
        fontSize(20.pxToEm()),
        lineHeight(40.pxToEm(20)),
        color(ThemeStyles.ThemeColor.primary)
      )
    )

}
