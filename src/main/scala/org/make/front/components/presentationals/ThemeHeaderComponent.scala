package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.facades._
import org.make.front.models.{GradientColor, Theme, ThemeId}
import org.make.front.styles.{BulmaStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ThemeHeaderComponent {

  case class ThemeHeaderProps(maybeTheme: Option[Theme])

  lazy val reactClass: ReactClass = React.createClass[ThemeHeaderProps, Unit](render = (self) => {
    val theme: Theme = self.props.wrapped.maybeTheme.getOrElse(Theme(ThemeId("asdf"), "-", "", 0, 0, "#FFF"))
    val gradientColor: GradientColor = theme.gradient.getOrElse(GradientColor("#FFF", "#FFF"))
    val imageOuterStyle = ThemeHeaderStyles.imageOuter(gradientColor.from, gradientColor.to).htmlClass

    <.div(^.className := BulmaStyles.Layout.hero)(
      <.style()(ThemeHeaderStyles.render[String]),
      <.div(^.className := imageOuterStyle)(
        <.img(
          ^.className := ThemeHeaderStyles.imageInner,
          ^.src := imageShutterstock.toString,
          ^.srcset := s"${imageShutterstock2.toString} 2x, ${imageShutterstock3.toString} 3x"
        )(),
        <.h2(^.className := ThemeHeaderStyles.heroTitle)(theme.title)
      )
    )
  })
}

object ThemeHeaderStyles extends StyleSheet.Inline {

  import dsl._

  def imageOuter(from: String, to: String): StyleA =
    style(MakeStyles.gradientBackground(from, to), height(30.3.rem), position.relative)

  val imageInner: StyleA =
    style(
      display.block,
      mixBlendMode := "multiply",
      opacity(0.5),
      margin(0.rem),
      objectFit.cover,
      width(144.rem),
      height(30.3.rem)
    )
  val heroTitle: StyleA = style(MakeStyles.heroTitle, position.absolute, top(9.rem), width(100.%%))

}
