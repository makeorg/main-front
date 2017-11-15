package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._

object IntroVff {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    displayName = "IntroVff",
    render = (_) =>
      <.section(^.className := Seq(TableLayoutStyles.wrapper, VffIntroStyles.wrapper))(
        <.div(^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, VffIntroStyles.innerWrapper))(
          <.img(
            ^.className := VffIntroStyles.illustration,
            ^.src := vffhome.toString,
            ^("srcset") := vffhomeSmall.toString + " 400w, " + vffhomeSmall2x.toString + " 800w, " + vffhomeMedium.toString + " 840w, " + vffhomeMedium2x.toString + " 1680w, " + vffhome.toString + " 1350w, " + vffhome2x.toString + " 2700w",
            ^.alt := "Make.org",
            ^("data-pin-no-hover") := "true"
          )(),
          <.div(^.className := Seq(VffIntroStyles.innerSubWrapper, LayoutRulesStyles.centeredRow))(
            <.div(^.className := VffIntroStyles.labelWrapper)(
              <.p(^.className := TextStyles.label)(unescape(I18n.t("vffhome.intro.baseline")))
            ),
            <.h2(^.className := Seq(VffIntroStyles.title, TextStyles.veryBigText, TextStyles.boldText))(
              unescape(I18n.t("vffhome.intro.title"))
            ),
            <.h3(^.className := Seq(TextStyles.mediumText, VffIntroStyles.subTitle))(
              unescape(I18n.t("vffhome.intro.subtitle"))
            ),
            <.p(^.className := VffIntroStyles.ctaWrapper)(
              <.a(
                ^.href := I18n.t("vffhome.intro.see-more-link"),
                ^.target := "_blank",
                ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA)
              )(unescape(I18n.t("vffhome.intro.see-more")))
            ),
            <.style()(VffIntroStyles.render[String])
          )
        )
    )
  )
}

object VffIntroStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, height(440.pxToEm()), backgroundColor(ThemeStyles.BackgroundColor.black))

  val innerWrapper: StyleA =
    style(position.relative, padding(112.pxToEm(), `0`, `0`, `0`), textAlign.center, overflow.hidden)

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
      transform := s"translate(-50%, -22%)",
      opacity(0.7)
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
