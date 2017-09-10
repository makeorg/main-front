package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.{LayoutStyleSheet, TextStyleSheet}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{logoMake, I18n}
import org.make.front.styles.MakeStyles

import scalacss.DevDefaults._
import scalacss.internal.{Length, StyleA}
import scalacss.internal.mutable.StyleSheet

object HeaderComponent {
  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    render = (self) =>
      <.header(^.className := HeaderStyles.wrapper)(
        <.div(^.className := LayoutStyleSheet.centeredRow)(
          <.div(^.className := LayoutStyleSheet.col)(
            <.div(^.className := HeaderStyles.innerWrapper)(
              //TODO: h1 if homepage else p
              <.h1(^.className := HeaderStyles.logoWrapper)(
                <.a(^.href := "/")(
                  <.img(^.className := HeaderStyles.logo, ^.src := logoMake.toString, ^.title := "Make.org")()
                )
              ),
              <.div(^.className := HeaderStyles.searchWrapper)(<.SearchInputComponent()()),
              <.div(^.className := HeaderStyles.menusWrapper)(
                <.div(^.className := HeaderStyles.menusInnerWrapper)(
                  <.nav(^.className := Seq(HeaderStyles.menuWrapper, LayoutStyleSheet.showInlineBlockBeyondMedium))(
                    <.ul(^.className := HeaderStyles.menu)(
                      <.li(^.className := HeaderStyles.menuItem)(
                        <.p(^.className := Seq(TextStyleSheet.title, TextStyleSheet.smallText))(
                          <.a(^.href := "/", ^.className := HeaderStyles.menuItemLink)(
                            unescape(I18n.t("content.header.presentation"))
                          )
                        )
                      )
                    )
                  ),
                  <.UserNavContainerComponent.empty
                )
              )
            )
          )
        ),
        <.style()(HeaderStyles.render[String])
    )
  )
}

object HeaderStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(
      position.fixed,
      top(0.em),
      left(0.em),
      width(100.%%),
      zIndex(10),
      backgroundColor(MakeStyles.BackgroundColor.white),
      boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)"
    )

  val innerWrapper: StyleA =
    style(
      display.table,
      width(100.%%),
      height(50.pxToEm()),
      MakeStyles.MediaQueries.beyondSmall(height(MakeStyles.mainNavDefaultHeight))
    )

  val logoWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val searchWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val menusWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle, textAlign.right)

  val menusInnerWrapper: StyleA =
    style(margin :=! s"0 -${MakeStyles.Spacing.small.value}")

  val menuWrapper: StyleA =
    style(display.inlineBlock)

  val logo: StyleA = style(width(100.%%), maxWidth(60.pxToEm()))

  val menu: StyleA =
    style()

  val menuItem: StyleA =
    style(display.inlineBlock, verticalAlign.baseline, margin :=! s"0 ${MakeStyles.Spacing.small.value}")

  val menuItemLink: StyleA =
    style(color :=! MakeStyles.ThemeColor.primary)

}
