package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.facades.{imageLogoMake, I18n}
import org.make.front.components.LayoutStyleSheet
import org.make.front.components.TextStyleSheet
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.{FontAwesomeStyles, MakeStyles}

import scalacss.internal.Length
import scalacss.DevDefaults._

object FooterComponent {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](render = self => {
    <.footer(^.className := FooterStyles.wrapper)(
      <.div(^.className := LayoutStyleSheet.centeredRow)(
        <.div(^.className := LayoutStyleSheet.col)(
          <.div(^.className := FooterStyles.innerWrapper)(
            <.p(^.className := FooterStyles.logoWrapper)(
              <.a(^.href := "/")(<.img(^.className := FooterStyles.logo, ^.src := imageLogoMake.toString)())
            ),
            <.div(^.className := FooterStyles.menuWrapper)(
              <.ul(^.className := FooterStyles.menu)(
                <.li(^.className := Seq(FooterStyles.menuItem, FooterStyles.emphasizedMenuItem))(
                  <.p(^.className := Seq(TextStyleSheet.title, TextStyleSheet.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(
                      <.i(^.className := Seq(FooterStyles.menuItemIcon, FontAwesomeStyles.bullhorn))(),
                      unescape(I18n.t("content.footer.recruitment"))
                    )
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyleSheet.title, TextStyleSheet.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.jobs"))
                  )
                ),
                <.li(^.className := Seq(FooterStyles.menuItem, LayoutStyleSheet.hideBeyondMedium))(
                  <.p(^.className := Seq(TextStyleSheet.title, TextStyleSheet.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(
                      unescape(I18n.t("content.footer.presentation"))
                    )
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyleSheet.title, TextStyleSheet.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.press"))
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyleSheet.title, TextStyleSheet.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.terms"))
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyleSheet.title, TextStyleSheet.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.contact"))
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyleSheet.title, TextStyleSheet.smallText))(
                    <.a(^.href := "/", ^.className := FooterStyles.menuItemLink)(I18n.t("content.footer.faq"))
                  )
                ),
                <.li(^.className := FooterStyles.menuItem)(
                  <.p(^.className := Seq(TextStyleSheet.title, TextStyleSheet.smallText))(
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
    style(backgroundColor(MakeStyles.BackgroundColor.white), boxShadow := s"0 -2px 4px 0 rgba(0,0,0,0.50)")

  val innerWrapper: StyleA =
    style(display.table, width(100.%%), height(MakeStyles.mainNavDefaultHeight))

  val logoWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val menuWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val logo: StyleA =
    style(width(100.%%), maxWidth(60.pxToEm()))

  val menu: StyleA =
    style(
      textAlign.right,
      margin :=! s"${MakeStyles.Spacing.medium.value} 0",
      MakeStyles.MediaQueries.beyondMedium(margin :=! s"0 -${MakeStyles.Spacing.small.value}")
    )

  val menuItem: StyleA =
    style(
      margin :=! s"${MakeStyles.Spacing.small.value} 0",
      MakeStyles.MediaQueries
        .beyondMedium(display.inlineBlock, verticalAlign.baseline, margin :=! s"${MakeStyles.Spacing.small.value}")
    )

  val menuItemLink: StyleA =
    style(color :=! inherit, transition := "color .2s ease-in-out", (&.hover)(color :=! MakeStyles.ThemeColor.primary))

  val emphasizedMenuItem: StyleA =
    style(color(MakeStyles.ThemeColor.primary))

  val menuItemIcon: StyleA =
    style(marginRight(MakeStyles.Spacing.smaller), verticalAlign.baseline)

}
