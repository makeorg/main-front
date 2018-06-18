package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scala.scalajs.js


object ConsultationFooter {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "ConsultationFooter",
        render = _ => {
          <.div(^.className := ConsultationFooterStyles.wrapper)(
            <.a(
              ^.className := js.Array(
                TextStyles.smallerText,
                ConsultationFooterStyles.link),
              ^.href := I18n.t(s"main-footer.menu.item-2.link"),
              ^.target := "_blank")(
              unescape(I18n.t(s"main-footer.menu.item-2.label"))
            ),
            " · ",
            <.a(
              ^.className := js.Array(
                TextStyles.smallerText,
                ConsultationFooterStyles.link),
              ^.href := I18n.t(s"main-footer.menu.item-4.link"),
              ^.target := "_blank")(
              unescape(I18n.t(s"main-footer.menu.item-4.label"))
            ),
            " · ",
            <.a(
              ^.className := js.Array(
                TextStyles.smallerText,
                ConsultationFooterStyles.link),
              ^.href := I18n.t(s"main-footer.menu.item-5.link"),
              ^.target := "_blank")(
              unescape(I18n.t(s"main-footer.menu.item-5.label"))
            ),
            " · ",
            <.a(
              ^.className := js.Array(
                TextStyles.smallerText,
                ConsultationFooterStyles.link),
              ^.href := I18n.t(s"main-footer.menu.item-6.link"),
              ^.target := "_blank")(
              unescape(I18n.t(s"main-footer.menu.item-6.label"))
            ),
            " · ",
            <.a(
              ^.className := js.Array(
                TextStyles.smallerText,
                ConsultationFooterStyles.link),
              ^.href := I18n.t(s"main-footer.menu.item-7.link"),
              ^.target := "_blank")(
              unescape(I18n.t(s"main-footer.menu.item-7.label"))
            ),
            <.style()(ConsultationFooterStyles.render[String])
          )
        }
      )
}


object ConsultationFooterStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      color(ThemeStyles.TextColor.lighter)
    )

  val link: StyleA =
    style(
      color(ThemeStyles.TextColor.lighter)
    )
}