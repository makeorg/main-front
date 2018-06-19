package org.make.front.components.mainFooter

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{logoMake, I18n}
import org.make.front.styles._
import org.make.front.styles.base._
import org.make.front.styles.utils._

import scala.scalajs.js

object MainFooter {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "MainFooter",
        shouldComponentUpdate = (_, _, _) => true,
        render = self => {

          <.footer(^.className := MainFooterStyles.wrapper)(
            <.div(^.className := LayoutRulesStyles.centeredRow)(
              <.div(^.className := js.Array(TableLayoutStyles.wrapper, MainFooterStyles.innerWrapper))(
                <.p(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                  <.img(
                    ^.className := MainFooterStyles.logo,
                    ^.src := logoMake.toString,
                    ^.alt := "Make.org",
                    ^("data-pin-no-hover") := "true"
                  )()
                ),
                <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                  <.ul(^.className := MainFooterStyles.menu)(
                    /*<.li(^.className := js.Array(MainFooterStyles.menuItem, MainFooterStyles.emphasizedMenuItem))(
                        <.p(^.className := js.Array(TextStyles.title, TextStyles.smallText))(
                          <.a(
                            ^.href := I18n.t("main-footer.menu.item-1.link"),
                            ^.className := MainFooterStyles.menuItemLink
                          )(
                            <.i(
                              ^.className := js.Array(
                                MainFooterStyles.menuItemIcon,
                                FontAwesomeStyles.bullhorn
                              )
                            )(),
                            unescape(I18n.t("main-footer.menu.item-1.label"))
                          )
                        )
                      ),*/
                    Range(2, 9).map(
                      item =>
                        <.li(^.className := MainFooterStyles.menuItem)(
                          <.p(
                            ^.className := js
                              .Array(TextStyles.title.htmlClass, TextStyles.smallText.htmlClass, if (item == 3) {
                                RWDRulesMediumStyles.hideBeyondMedium.htmlClass
                              })
                              .mkString(" ")
                          )(
                            <.a(
                              ^.href := I18n.t(s"main-footer.menu.item-$item.link"),
                              ^.target := "_blank",
                              ^.className := MainFooterStyles.menuItemLink
                            )(unescape(I18n.t(s"main-footer.menu.item-$item.label")))
                          )
                      )
                    )
                  )
                )
              )
            ),
            <.style()(MainFooterStyles.render[String])
          )
        }
      )
}

object MainFooterStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.white), boxShadow := s"0 -2px 4px 0 rgba(0,0,0,0.50)")

  val innerWrapper: StyleA =
    style(height(ThemeStyles.mainNavDefaultHeight))

  val logo: StyleA =
    style(width(100.%%), maxWidth(60.pxToEm()))

  val menu: StyleA =
    style(
      textAlign.right,
      margin(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondMedium(margin(`0`, (ThemeStyles.SpacingValue.small.pxToEm() * -1), `0`, `0`))
    )

  val menuItem: StyleA =
    style(
      margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      ThemeStyles.MediaQueries
        .beyondMedium(display.inlineBlock, verticalAlign.baseline, margin(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val menuItemLink: StyleA =
    style(color.inherit, transition := "color .2s ease-in-out", &.hover(color(ThemeStyles.ThemeColor.primary)))

  val emphasizedMenuItem: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val menuItemIcon: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()), verticalAlign.baseline)

}
