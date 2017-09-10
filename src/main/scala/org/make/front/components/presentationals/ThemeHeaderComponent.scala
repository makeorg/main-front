package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.containers.ProposalSubmitContainerComponent
import org.make.front.facades._
import org.make.front.models.{GradientColor, Theme, ThemeId}
import org.make.front.styles.{LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object ThemeHeaderComponent {

  case class ThemeHeaderProps(maybeTheme: Option[Theme])

  // @toDo: get it from a config (API call)
  val proposalFieldPrefix = "Il faut "
  val proposalFieldPlaceHolder = "Il faut une proposition réaliste et respectueuse de tous"

  lazy val reactClass: ReactClass = React.createClass[ThemeHeaderProps, Unit](render = (self) => {
    val theme: Theme = self.props.wrapped.maybeTheme.getOrElse(Theme(ThemeId("asdf"), "-", "", 0, 0, "#FFF"))
    val gradient: GradientColor = theme.gradient.getOrElse(GradientColor("#FFF", "#FFF"))

    <.header(
      ^.className := Seq(ThemeHeaderStyles.wrapper, ThemeHeaderStyles.gradientBackground(gradient.from, gradient.to))
    )(
      <.div(^.className := ThemeHeaderStyles.innerWrapper)(
        <.img(^.className := ThemeHeaderStyles.illustration, ^.src := imageShutterstock.toString)(),
        <.div(^.className := LayoutRulesStyles.centeredRow)(
          <.div(^.className := LayoutRulesStyles.col)(
            <.h1(^.className := Seq(TextStyles.veryBigTitle, ThemeHeaderStyles.title))(theme.title),
            <.div(^.className := ThemeHeaderStyles.proposalFormWrapper)(
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
      ),
      <.style()(ThemeHeaderStyles.render[String])
    )
  })
}

object ThemeHeaderStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(
      position.relative,
      display.table,
      width(100.%%),
      height(500.pxToEm()),
      height :=! s"calc(100% - ${200.pxToEm().value})",
      backgroundColor(ThemeStyles.BackgroundColor.black), //TODO:gradient
      overflow.hidden
    )

  def gradientBackground(from: String, to: String): StyleA =
    style(background := s"linear-gradient(130deg, $from, $to)")

  val innerWrapper: StyleA =
    style(
      position.relative,
      display.tableCell,
      verticalAlign.middle,
      paddingTop((60 + 50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((60 + 80).pxToEm())),
      paddingBottom(ThemeStyles.Spacing.larger),
      textAlign.center
    )

  val illustration: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      height.auto,
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, -50%)",
      opacity(0.5),
      filter := s"grayscale(100%)",
      mixBlendMode := "multiply"
    )

  val title: StyleA =
    style(color(ThemeStyles.TextColor.white), textShadow := s"1px 1px 1px rgb(0, 0, 0)")

  val proposalFormWrapper: StyleA =
    style(marginTop(ThemeStyles.Spacing.smaller))

}
