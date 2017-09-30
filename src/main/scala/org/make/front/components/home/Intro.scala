package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{mainIntroIll, I18n}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object Intro {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    displayName = "Intro",
    render = (_) =>
      <.section(^.className := IntroStyles.wrapper)(
        <.div(^.className := IntroStyles.innerWrapper)(
          <.div(^.className := LayoutRulesStyles.centeredRow)(
            <.div(^.className := LayoutRulesStyles.col)(
              <.div(^.className := IntroStyles.labelWrapper)(
                <.p(^.className := TextStyles.label)(unescape(I18n.t("content.homepage.baseline")))
              ),
              <.h2(^.className := Seq(IntroStyles.title, TextStyles.veryBigText, TextStyles.boldText))(
                unescape(I18n.t("content.homepage.title"))
              ),
              <.h3(^.className := Seq(TextStyles.mediumText, IntroStyles.subTitle))(
                unescape(I18n.t("content.homepage.subTitle"))
              ),
              <.p(^.className := IntroStyles.ctaWrapper)(
                <.a(^.href := "/", ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA))(
                  unescape(I18n.t("content.homepage.textSeeMore"))
                )
              ),
              <.style()(IntroStyles.render[String])
            )
          )
        )
    )
  )
}

object IntroStyles extends StyleSheet.Inline {

  import dsl._

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
      paddingTop((ThemeStyles.SpacingValue.larger + 50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((ThemeStyles.SpacingValue.larger + 80).pxToEm())),
      paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
      textAlign.center,
      (&.before)(
        content := "''",
        position.absolute,
        top(`0`),
        left(`0`),
        height(100.%%),
        width(100.%%),
        backgroundImage := s"url('${mainIntroIll.toString}')",
        backgroundSize := s"cover",
        backgroundPosition := s"50%",
        backgroundRepeat := s"no-repeat",
        opacity(0.5)
      )
    )

  val labelWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val title: StyleA =
    style(color(ThemeStyles.TextColor.white), textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)")

  val subTitle: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      color(ThemeStyles.TextColor.white),
      textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)"
    )

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

}
