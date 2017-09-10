package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.facades.{logoMake, I18n}
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.{FontAwesomeStyles, LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.internal.Length
import scalacss.DevDefaults._

object FooterComponent {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](render = self => {
    <.footer(^.className := FooterStyles.wrapper)(
      <.div(^.className := LayoutRulesStyles.centeredRow)(
        <.div(^.className := LayoutRulesStyles.col)(
          <.div(^.className := FooterStyles.innerWrapper)(
            <.p(^.className := FooterStyles.logoWrapper)(
              <.a(^.href := "/")(<.img(^.className := FooterStyles.logo, ^.src := logoMake.toString)())
            ),
            <.div(^.className := FooterStyles.menuWrapper)(
              <.ul(^.className := FooterStyles.menu)(
                <.li(^.className := Seq(FooterStyles.menuItem, FooterStyles.emphasizedMenuItem))(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(
                      <.i(^.className := Seq(FooterStyles.menuItemIcon, FontAwesomeStyles.bullhorn))(),
                      unescape(I18n.t("content.footer.recruitment"))
                    )
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.jobs"))
                  )
                ),
                <.li(^.className := Seq(FooterStyles.menuItem, LayoutRulesStyles.hideBeyondMedium))(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(
                      unescape(I18n.t("content.footer.presentation"))
                    )
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.press"))
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.terms"))
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.contact"))
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.faq"))
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyles.title, TextStyles.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.sitemap"))
                  )
                )
              )
            )
          )
        )
      ),
      <.style()(FooterStyles.render[String])
    )
  })
}

object FooterStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
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
      margin :=! s"${ThemeStyles.Spacing.medium.value} 0",
      ThemeStyles.MediaQueries.beyondMedium(margin :=! s"0 -${ThemeStyles.Spacing.small.value}")
    )

  val menuItem: StyleA =
    style(
      margin :=! s"${ThemeStyles.Spacing.small.value} 0",
      ThemeStyles.MediaQueries
        .beyondMedium(display.inlineBlock, verticalAlign.baseline, margin :=! s"${ThemeStyles.Spacing.small.value}")
    )

  val menuItemLink: StyleA =
    style(color :=! inherit, transition := "color .2s ease-in-out", (&.hover)(color :=! ThemeStyles.ThemeColor.primary))

  val emphasizedMenuItem: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val menuItemIcon: StyleA =
    style(marginRight(ThemeStyles.Spacing.smaller), verticalAlign.baseline)

}
