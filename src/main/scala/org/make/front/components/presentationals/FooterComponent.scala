package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.{imageLogoMake, I18n}
import org.make.front.helpers.NumberFormat
import org.make.front.models.Theme
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}

import scalacss.DevDefaults._

object FooterComponent {

  case class WrappedProps(themes: Seq[Theme])

  lazy val reactClass: ReactClass = React.createClass[WrappedProps, Unit](render = self => {
    <.div(^.className := BulmaStyles.Helpers.isFullWidth)(
      <.div(^.className := FooterStyles.themeWrapper)(
        <.div(^.className := FooterStyles.container)(
          <.h2(^.className := FooterStyles.themeWrapperTitle)(I18n.t("content.footer.title")),
          ThemeList(self.props.wrapped.themes)
        )
      ),
      <.div(^.className := FooterStyles.linksContainer)(
        <.div(^.className := BulmaStyles.Layout.container)(
          <.a(^.className := FooterStyles.footerLogo, ^.href := "/")(<.img(^.src := imageLogoMake.toString)()),
          <.ul(^.className := Seq(BulmaStyles.Helpers.isPulledRight, FooterStyles.links))(
            <.li(^.className := FooterStyles.firstLink)(
              <.span(^.className := BulmaStyles.Element.icon)(<.i(^.className := FontAwesomeStyles.bullhorn)()),
              I18n.t("content.footer.recruitment")
            ),
            <.li()(I18n.t("content.footer.jobs")),
            <.li()(I18n.t("content.footer.press")),
            <.li()(I18n.t("content.footer.terms")),
            <.li()(I18n.t("content.footer.contact")),
            <.li()(I18n.t("content.footer.faq")),
            <.li()(I18n.t("content.footer.sitemap"))
          )
        )
      ),
      <.style()(FooterStyles.render[String])
    )
  })
}

object ThemeList {
  // Use a function to render
  def apply(themes: Seq[Theme]): ReactElement =
    <.ul(^.className := Seq(BulmaStyles.Grid.Columns.columnsMultiLines))(
      themes.map(
        theme =>
          <.li(
            ^.key := theme.slug,
            ^.className := Seq(BulmaStyles.Grid.Columns.is4, BulmaStyles.Grid.Columns.column, FooterStyles.theme)
          )(
            <.span(^.className := Seq(FooterStyles.themeInner(theme.color)))(
              <.span(
                ^.className := Seq(
                  BulmaStyles.Helpers.isClearfix,
                  BulmaStyles.Helpers.isFullWidth,
                  FooterStyles.themeTitle
                )
              )(<.Link(^.to := s"/theme/${theme.slug}")(theme.title)),
              <.div(^.className := BulmaStyles.Helpers.isClearfix)(
                <.Translate(
                  ^.className := Seq(BulmaStyles.Helpers.isPulledLeft, FooterStyles.themeDescription),
                  ^.value := "content.theme.actionsCount",
                  ^("actions") := NumberFormat.formatToKilo(theme.actionsCount)
                )(),
                <.Translate(
                  ^.className := Seq(BulmaStyles.Helpers.isPulledLeft, FooterStyles.themeDescription),
                  ^.value := "content.theme.proposalsCount",
                  ^("proposals") := NumberFormat.formatToKilo(theme.proposalsCount)
                )()
              )
            )
        )
      )
    )
}

object FooterStyles extends StyleSheet.Inline {
  import dsl._

  val themeWrapper: StyleA =
    style(
      backgroundColor(MakeStyles.Background.footer),
      padding(3.2F.rem, 0.rem, 1.8F.rem, 0.rem),
      MakeStyles.Font.tradeGothicLTStd
    )

  val themeWrapperTitle: StyleA = style(MakeStyles.title2, lineHeight(3.4F.rem), marginBottom(2.2F.rem))

  val theme: StyleA =
    style(padding(0.rem), margin(0.rem, 0.rem, 1.5F.rem, 0.rem))

  def themeInner(color: String): StyleA =
    style(
      borderLeft :=! s"0.5rem solid $color",
      width(100.%%),
      height(100.%%),
      display.block,
      marginLeft(0.75F.rem),
      paddingLeft(0.9F.rem)
    )

  val themeTitle: StyleA = style(
    unsafeChild("a")(fontSize(2.rem), lineHeight(1.2), fontWeight.bold, color(MakeStyles.Color.black))
  )

  val themeDescription: StyleA =
    style(
      color(rgba(0, 0, 0, 0.3)),
      marginRight(10.px),
      fontSize(1.6F.rem),
      lineHeight(1.6F.rem),
      MakeStyles.Font.circularStdBook
    )

  val linksContainer: StyleA =
    style(
      height(71.px),
      backgroundColor(c"#ffffff"),
      boxShadow := "0 0 4px 0 rgba(0, 0, 0, 0.3)",
      padding(20.px, 0.px)
    )

  val container: StyleA = style(maxWidth(114.rem), marginRight.auto, marginLeft.auto, width(100.%%), height(100.%%))

  val footerLogo: StyleA = style(unsafeChild("img")(width(60.px)))

  val links: StyleA = style(
    unsafeChild("li")(
      float.left,
      fontSize(1.6F.rem),
      margin(0.px, 7.px),
      fontWeight.bold,
      position.relative,
      unsafeChild("span.icon")(position.absolute, top(4.px), left(-30.px))
    )
  )

  val firstLink: StyleA = style(color(MakeStyles.Color.pink))
}
