package org.make.front.components.Home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.presentationals._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{mainIntroIll, I18n}
import org.make.front.styles.{CTAStyles, LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object MainIntroComponent {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.section(^.className := MainIntroStyles.wrapper)(
        <.div(^.className := MainIntroStyles.innerWrapper)(
          <.div(^.className := LayoutRulesStyles.centeredRow)(
            <.div(^.className := LayoutRulesStyles.col)(
              <.p(^.className := Seq(MainIntroStyles.label, TextStyles.label))(
                unescape(I18n.t("content.homepage.baseline"))
              ),
              <.h2(^.className := Seq(MainIntroStyles.title, TextStyles.veryBigText, TextStyles.boldText))(
                unescape(I18n.t("content.homepage.title"))
              ),
              <.h3(^.className := Seq(MainIntroStyles.subTitle))(unescape(I18n.t("content.homepage.subTitle"))),
              <.p()(
                <.a(^.href := "/", ^.className := Seq(MainIntroStyles.cta, CTAStyles.basic))(
                  unescape(I18n.t("content.homepage.textSeeMore"))
                )
              ),
              <.style()(MainIntroStyles.render[String])
            )
          )
        )
    )
  )
}

object MainIntroStyles extends StyleSheet.Inline {

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
      backgroundColor(ThemeStyles.BackgroundColor.black)
    )

  val innerWrapper: StyleA =
    style(
      position.relative,
      display.tableCell,
      verticalAlign.middle,
      paddingTop((60 + 50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((60 + 80).pxToEm())),
      paddingBottom(ThemeStyles.Spacing.larger),
      textAlign.center,
      (&.before)(
        content := "''",
        position.absolute,
        top(0.%%),
        left(0.%%),
        height(100.%%),
        width(100.%%),
        backgroundImage := s"url('${mainIntroIll.toString}')",
        backgroundSize := s"cover",
        backgroundPosition := s"50%",
        backgroundRepeat := s"no-repeat",
        opacity(0.5)
      )
    )

  val label: StyleA =
    style(marginBottom(ThemeStyles.Spacing.small))

  val title: StyleA =
    style(color(ThemeStyles.TextColor.white), textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)")

  val subTitle: StyleA =
    style(
      marginTop(ThemeStyles.Spacing.small),
      color(ThemeStyles.TextColor.white),
      textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)"
    )

  val cta: StyleA =
    style(marginTop(ThemeStyles.Spacing.small))

}
