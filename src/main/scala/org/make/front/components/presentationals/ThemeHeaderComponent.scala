package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.containers.ProposalSubmitContainerComponent
import org.make.front.facades._
import org.make.front.models.{GradientColor, Theme, ThemeId}
import org.make.front.styles.{BulmaStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ThemeHeaderComponent {

  case class ThemeHeaderProps(maybeTheme: Option[Theme])

  // @toDo: get it from a config (API call)
  val proposalFieldPrefix = "Il faut "
  val proposalFieldPlaceHolder = "Il faut une proposition réaliste et respectueuse de tous"

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
        <.div(^.className := ThemeHeaderStyles.contentContainer)(
          <.h2(^.className := ThemeHeaderStyles.heroTitle)(theme.title),
          <.div(^.className := ThemeHeaderStyles.proposalSubmit)(
            <.ProposalSubmitContainerComponent(
              ^.wrapped := ProposalSubmitContainerComponent
                .ProposalSubmitContainerProps(
                  proposalPrefix = proposalFieldPrefix,
                  proposalPlaceHolder = proposalFieldPlaceHolder,
                  theme = theme
                )
            )()
          )
        )
      )
    )
  })
}

object ThemeHeaderStyles extends StyleSheet.Inline {

  import dsl._

  def imageOuter(from: String, to: String): StyleA =
    style(
      MakeStyles.gradientBackground(from, to),
      height(30.3.rem),
      position.relative,
      (media.all.maxWidth(800.px))(height(18.85F.rem))
    )

  val imageInner: StyleA =
    style(
      display.block,
      mixBlendMode := "multiply",
      opacity(0.5),
      margin(0.rem),
      objectFit.cover,
      width(144.rem),
      height(30.3.rem),
      (media.all.maxWidth(800.px))(height(18.85F.rem))
    )

  val heroTitle: StyleA = style(
    paddingBottom(0.7.rem),
    MakeStyles.TextStyles.veryBigTitle,
    lineHeight(6.rem),
    color(c"#FFF"),
    BulmaStyles.ResponsiveHelpers.block,
    textAlign.center,
    textShadow := "0.1rem 0.1rem 0.1rem #000",
    (media.all.maxWidth(800.px))(fontSize(3.rem), lineHeight(3.rem))
  )

  val contentContainer: StyleA =
    style(
      display.flex,
      flexDirection.column,
      alignContent.center,
      alignItems.center,
      justifyContent.center,
      position.absolute,
      top(0.rem),
      width(100.%%),
      height(100.%%),
      (media.all.maxWidth(800.px))(height(18.85F.rem))
    )

  val proposalSubmit: StyleA =
    style(width(80.%%))

}
