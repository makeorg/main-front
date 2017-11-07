package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._

object Intro {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    displayName = "Intro",
    render = (_) =>
      <.section(^.className := IntroStyles.wrapper)(
        <.div(^.className := IntroStyles.innerWrapper)(
          <.img(
            ^.className := IntroStyles.illustration,
            ^.src := home.toString,
            ^("srcset") := homeSmall.toString + " 400w, " + homeSmall2x.toString + " 800w, " + homeMedium.toString + " 840w, " + homeMedium2x.toString + " 1680w, " + home.toString + " 1350w, " + home2x.toString + " 2700w",
            ^.alt := "Make.org",
            ^("data-pin-no-hover") := "true"
          )(),
          <.div(^.className := Seq(IntroStyles.innerSubWrapper, RowRulesStyles.centeredRow))(
            <.div(^.className := ColRulesStyles.col)(
              <.div(^.className := IntroStyles.labelWrapper)(
                <.p(^.className := TextStyles.label)(unescape(I18n.t("home.intro.baseline")))
              ),
              <.h2(^.className := Seq(IntroStyles.title, TextStyles.veryBigText, TextStyles.boldText))(
                unescape(I18n.t("home.intro.title"))
              ),
              <.h3(^.className := Seq(TextStyles.mediumText, IntroStyles.subTitle))(
                unescape(I18n.t("home.intro.subtitle"))
              ),
              <.p(^.className := IntroStyles.ctaWrapper)(
                <.a(
                  ^.href := I18n.t("home.intro.see-more-link"),
                  ^.target := "_blank",
                  ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA)
                )(unescape(I18n.t("home.intro.see-more")))
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
      height(440.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.black)
    )

  val innerWrapper: StyleA =
    style(
      position.relative,
      display.tableCell,
      verticalAlign.middle,
      padding :=! s"${ThemeStyles.SpacingValue.larger.pxToEm().value} 0",
      textAlign.center,
      overflow.hidden
    )

  val illustration: StyleA =
    style(
      position.absolute,
      top(`0`),
      left(50.%%),
      height.auto,
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, 0%)",
      opacity(0.5)
    )

  val innerSubWrapper: StyleA =
    style(position.relative, zIndex(1))

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
