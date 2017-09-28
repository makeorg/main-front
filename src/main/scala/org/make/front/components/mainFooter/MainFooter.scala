package org.make.front.components.mainFooter

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{logoMake, I18n}
import org.make.front.styles._

import scalacss.DevDefaults._
import scalacss.internal.Length

object MainFooter {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](render = self => {
    <.footer(^.className := MainFooterStyles.wrapper)(
      <.div(^.className := LayoutRulesStyles.centeredRow)(
        <.div(^.className := LayoutRulesStyles.col)(
          <.div(^.className := MainFooterStyles.innerWrapper)(
            <.p(^.className := MainFooterStyles.logoWrapper)(
              <.a(^.href := "/")(<.img(^.className := MainFooterStyles.logo, ^.src := logoMake.toString)())
            ),
            <.div(^.className := MainFooterStyles.menuWrapper)(
              <.ul(^.className := MainFooterStyles.menu)(
                <.li(^.className := Seq(MainFooterStyles.menuItem, MainFooterStyles.emphasizedMenuItem))(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := MainFooterStyles.menuItemLink)(
                      <.i(
                        ^.className := Seq(
                          MainFooterStyles.menuItemIcon,
                          FontAwesomeStyles.fa,
                          FontAwesomeStyles.bullhorn
                        )
                      )(),
                      unescape(I18n.t("content.footer.recruitment"))
                    )
                  )
                ),
                <.li(^.className := MainFooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := MainFooterStyles.menuItemLink)(I18n.t("content.footer.jobs"))
                  )
                ),
                <.li(^.className := Seq(MainFooterStyles.menuItem, RWDHideRulesStyles.hideBeyondMedium))(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := MainFooterStyles.menuItemLink)(
                      unescape(I18n.t("content.footer.presentation"))
                    )
                  )
                ),
                <.li(^.className := MainFooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := MainFooterStyles.menuItemLink)(I18n.t("content.footer.press"))
                  )
                ),
                <.li(^.className := MainFooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := MainFooterStyles.menuItemLink)(I18n.t("content.footer.terms"))
                  )
                ),
                <.li(^.className := MainFooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := MainFooterStyles.menuItemLink)(I18n.t("content.footer.contact"))
                  )
                ),
                <.li(^.className := MainFooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := MainFooterStyles.menuItemLink)(I18n.t("content.footer.faq"))
                  )
                ),
                <.li(^.className := MainFooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := MainFooterStyles.menuItemLink)(I18n.t("content.footer.sitemap"))
                  )
                )
              )
            )
          )
        )
      ),
      <.style()(MainFooterStyles.render[String])
    )
  })
}

object MainFooterStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

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
