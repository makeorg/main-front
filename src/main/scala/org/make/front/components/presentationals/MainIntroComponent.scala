package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.{LayoutStyleSheet, TextStyleSheet}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.I18n
import org.make.front.styles.MakeStyles

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object MainIntroComponent {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.section(^.className := MainIntroStyles.wrapper)(
        <.div(^.className := MainIntroStyles.innerWrapper)(
          <.div(^.className := LayoutStyleSheet.centeredRow)(
            <.div(^.className := LayoutStyleSheet.col)(
              <.p()(unescape(I18n.t("content.homepage.baseline"))),
              <.h2(^.className := Seq(TextStyleSheet.veryBigText, TextStyleSheet.boldText))(
                unescape(I18n.t("content.homepage.title"))
              ),
              <.h3()(unescape(I18n.t("content.homepage.subTitle"))),
              <.p()(<.a(^.href := "/")(unescape(I18n.t("content.homepage.textSeeMore")))),
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
      display.table,
      width(100.%%),
      height(500.pxToEm()),
      height :=! s"calc(100.%% - ${300.pxToEm()})",
      backgroundColor(MakeStyles.BackgroundColor.grey)
    )

  val innerWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      paddingTop(MakeStyles.Spacing.larger),
      paddingBottom(MakeStyles.Spacing.larger),
      textAlign.center
    )
}
