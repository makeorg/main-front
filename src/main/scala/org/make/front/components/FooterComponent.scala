package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Predef._
import org.make.front.helpers.NumberFormat
import org.make.front.models.Theme
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}

import scalacss.DevDefaults._

object FooterComponent {

  case class WrappedProps(themes: Seq[Theme])

  def apply(props: Props[WrappedProps]): ReactElement = {
    <.div(^.className := BulmaStyles.Helpers.isFullWidth)(
      <.div(^.className := FooterStyles.themeWrapper)(
        <.div(^.className := BulmaStyles.Layout.container)(
          <.h2(^.className := FooterStyles.themeWrapperTitle)("Tous les thèmes"),
          ThemeList(props.wrapped.themes)
        )
      ),
      <.div(^.className := FooterStyles.linksContainer)(
        <.div(^.className := BulmaStyles.Layout.container)(
          <.a(^.className := FooterStyles.footerLogo, ^.href := "/")(
            <.img(^.src := "http://phantom.make.org/images/logoMake.svg")()
          ),
          <.ul(^.className := Seq(BulmaStyles.Helpers.isPulledRight, FooterStyles.links))(
            <.li(^.className := FooterStyles.firstLink)(
              <.span(^.className := BulmaStyles.Element.icon)(<.i(^.className := FontAwesomeStyles.bullhorn)()),
              "Devenez Maker!"
            ),
            <.li()("jobs"),
            <.li()("Espace presse"),
            <.li()("conditions d'utilisation"),
            <.li()("contact"),
            <.li()("f.a.q."),
            <.li()("sitemap")
          )
        )
      ),
      <.style()(FooterStyles.render[String])
    )
  }
}

object ThemeList {
  // Use a function to render
  def apply(themes: Seq[Theme]): ReactElement =
    <.ul(^.className := Seq(BulmaStyles.Grid.Columns.columnsMultiLines))(
      themes.map(
        theme =>
          <.li(
            ^.key := theme.slug,
            ^.className := Seq(
              BulmaStyles.Grid.Columns.is4,
              BulmaStyles.Grid.Columns.column,
              FooterStyles.theme(theme.color)
            )
          )(
            <.span(
              ^.className := Seq(
                BulmaStyles.Helpers.isClearfix,
                BulmaStyles.Helpers.isFullWidth,
                FooterStyles.themeTitle
              )
            )(<.Link(^.to := s"/theme/${theme.slug}")(theme.title)),
            <.span(^.className := Seq(BulmaStyles.Helpers.isPulledLeft, FooterStyles.themeDescription))(
              s"${NumberFormat.formatToKilo(theme.actionsCount)} actions en cours"
            ),
            <.span(^.className := Seq(BulmaStyles.Helpers.isPulledLeft, FooterStyles.themeDescription))(
              s"${NumberFormat.formatToKilo(theme.proposalsCount)} propositions"
            )
        )
      )
    )
}

object FooterStyles extends StyleSheet.Inline {
  import dsl._

  val container: StyleA = style(width(100.%%))

  val themeWrapper: StyleA =
    style(backgroundColor(MakeStyles.Background.footer), padding(2.4.rem, 2.7.rem), MakeStyles.Font.tradeGothicLTStd)

  val themeWrapperTitle: StyleA = style(MakeStyles.title2)

  def theme(color: String): StyleA = style(margin(1.rem, 0.rem), borderLeft :=! s"0.4rem solid $color")

  val themeTitle: StyleA = style(
    unsafeChild("a")(fontSize(2.rem), lineHeight(1.4), fontWeight.bold, color(MakeStyles.Color.black))
  )

  val themeDescription: StyleA = style(color(rgba(0, 0, 0, 0.3)), marginRight(10.px))

  val linksContainer: StyleA =
    style(height(71.px), backgroundColor(c"#ffffff"), boxShadow := "0 0 4px 0 rgba(0, 0, 0, 0.3)", padding(20.px, 0.px))

  val footerLogo: StyleA = style(unsafeChild("img")(width(60.px)))

  val links: StyleA = style(
    unsafeChild("li")(
      float.left,
      fontSize(1.6.rem),
      margin(0.px, 7.px),
      fontWeight.bold,
      position.relative,
      unsafeChild("span.icon")(position.absolute, top(4.px), left(-30.px))
    )
  )

  val firstLink: StyleA = style(color(MakeStyles.Color.pink))
}
