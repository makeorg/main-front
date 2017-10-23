package org.make.front.components.mainFooter

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{logoMake, I18n}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RWDHideRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.DevDefaults._

object MainFooter {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "MainFooter",
        render = self => {

          <.footer(^.className := MainFooterStyles.wrapper)(
            <.div(^.className := RowRulesStyles.centeredRow)(
              <.div(^.className := ColRulesStyles.col)(
                <.div(^.className := MainFooterStyles.innerWrapper)(
                  <.p(^.className := MainFooterStyles.logoWrapper)(
                    <.a(^.href := "/")(
                      <.img(
                        ^.className := MainFooterStyles.logo,
                        ^.src := logoMake.toString,
                        ^("data-pin-no-hover") := "true"
                      )()
                    )
                  ),
                  <.div(^.className := MainFooterStyles.menuWrapper)(
                    <.ul(^.className := MainFooterStyles.menu)(
                      /*<.li(^.className := Seq(MainFooterStyles.menuItem, MainFooterStyles.emphasizedMenuItem))(
                        <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                          <.a(
                            ^.href := I18n.t("main-footer.menu.item-1.link"),
                            ^.className := MainFooterStyles.menuItemLink
                          )(
                            <.i(
                              ^.className := Seq(
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
                              ^.className := Seq(
                                TextStyles.title.htmlClass,
                                TextStyles.smallText.htmlClass,
                                if (item == 3) {
                                  RWDHideRulesStyles.hideBeyondMedium.htmlClass
                                }
                              ).mkString(" ")
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
              )
            ),
            <.style()(MainFooterStyles.render[String])
          )
        }
      )
}

object MainFooterStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: adjust shadow
  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.white), boxShadow := s"0 -2px 4px 0 rgba(0,0,0,0.50)")

  val innerWrapper: StyleA =
    style(display.table, width(100.%%), height(ThemeStyles.mainNavDefaultHeight))

  val logoWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val menuWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val logo: StyleA =
    style(width(100.%%), maxWidth(60.pxToEm()))

  val menu: StyleA =
    style(
      textAlign.right,
      margin :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0",
      ThemeStyles.MediaQueries.beyondMedium(margin :=! s"0 -${ThemeStyles.SpacingValue.small.pxToEm().value}")
    )

  val menuItem: StyleA =
    style(
      margin :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      ThemeStyles.MediaQueries
        .beyondMedium(
          display.inlineBlock,
          verticalAlign.baseline,
          margin :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value}"
        )
    )

  val menuItemLink: StyleA =
    style(color :=! inherit, transition := "color .2s ease-in-out", (&.hover)(color :=! ThemeStyles.ThemeColor.primary))

  val emphasizedMenuItem: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val menuItemIcon: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()), verticalAlign.baseline)

}
