package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.helpers.NumberFormat
import org.make.front.styles.{BulmaStyles, MakeStyles}
import org.make.front.Predef._

import scalacss.DevDefaults._

case class Theme(slug: String, title: String, actionsCount: Int, proposalsCount: Int, color: String)

object FooterComponent {

  case class State(themes: Seq[Theme])

  type self = React.Self[Unit, State]

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, State](
    getInitialState = (self) => State(
      themes = Seq(
        Theme("democratie-vie-politique", "DÉMOCRATIE / VIE POLITIQUE", 3, 5600, "#e21d60"),
        Theme("developpement-durable-energie", "DÉVELOPPEMENT DURABLE / ENERGIE", 3, 5600, "#82b745"),
        Theme("sante-alimentation", "SANTÉ / ALIMENTATION", 3, 5600, "#26a69a"),
        Theme("education", "EDUCATION", 3, 5600, "#673ab7"),
        Theme("economie-emploi-travail", "ECONOMIE / EMPLOI / TRAVAIL", 3, 5600, "#0e74c4"),
        Theme("securite-justice", "SÉCURITÉ / JUSTICE", 3, 5600, "#8e23a0"),
        Theme("logement", "LOGEMENT", 3, 5600, "#ff9800"),
        Theme("vivre-ensemble-solidarites", "VIVRE ENSEMBLE / SOLIDARITÉS", 3, 5600, "#ffeb3b"),
        Theme("agriculture-ruralite", "AGRICULTURE / RURALITÉ", 3, 5600, "#2e7d32"),
        Theme("europe-monde", "EUROPE / MONDE", 3, 5600, "#283593"),
        Theme("transports-deplacement", "TRANSPORTS / DÉPLACEMENT", 3, 5600, "#ff5722"),
        Theme("numerique-culture", "NUMÉRIQUE / CULTURE", 3, 5600, "#03a8f2")
      )
    ),
    render = (self) =>
      <.div(^.className := BulmaStyles.Helpers.isFullWidth)(
        <.div(^.className := FooterStyles.themeWrapper)(
          <.div(^.className := BulmaStyles.Layout.container)(
            <.h2(^.className := MakeStyles.title2)("Tous les thèmes"),
            ThemeList(self.state.themes)
          )
        ),
        <.div(^.className := FooterStyles.linksContainer)(
          <.div(^.className := BulmaStyles.Layout.container)(
            <.a(^.className := FooterStyles.footerLogo, ^.href := "/")(
              <.img(^.src := "http://phantom.make.org/images/logoMake.svg")()
            ),
            <.ul(^.className := Seq(BulmaStyles.Helpers.isPulledRight, FooterStyles.links))(
              <.li(^.className := FooterStyles.firstLink)(
                <.span(^.className := "icon")(
                  <.i(^.className := "fa fa-home")()
                ),
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
        <.style()(
          FooterStyles.render[String]
        )
      )
  )
}

object ThemeList {
  // Use a function to render
  def apply(themes: Seq[Theme]): ReactElement =
    <.ul(^.className := Seq(BulmaStyles.Grid.Columns.columnsMultiLines))(
      themes.map(
        theme => <.li(
          ^.key := theme.slug,
          ^.className := Seq(BulmaStyles.Grid.Columns.is4, BulmaStyles.Grid.Columns.column, FooterStyles.theme),
          ^.style := Map("borderLeft" -> s"0.2rem solid ${theme.color}")
        )(
          <.span(^.className := Seq(BulmaStyles.Helpers.isClearfix, BulmaStyles.Helpers.isFullWidth, FooterStyles.themeTitle))(
            <.a(^.href := "#")(theme.title)
          ),
          <.span(
            ^.className := Seq(BulmaStyles.Helpers.isPulledLeft, FooterStyles.themeDescription)
          )(
            s"${NumberFormat.formatToKilo(theme.actionsCount)} actions en cours"
          ),
          <.span(
            ^.className := Seq(BulmaStyles.Helpers.isPulledLeft, FooterStyles.themeDescription)
          )(
            s"${NumberFormat.formatToKilo(theme.proposalsCount)} propositions"
          )
        )
      )
    )
}

object FooterStyles extends StyleSheet.Inline {
  import dsl._

  val container: StyleA = style(
    width(100.%%)
  )

  val themeWrapper: StyleA = style(
    backgroundColor(MakeStyles.Background.footer),
    padding(2.4.rem, 2.7.rem)
  )

  val theme: StyleA = style(
    margin(1.rem, 0.rem)
  )

  val themeTitle: StyleA = style(
    unsafeChild("a")(
      fontSize(2.rem),
      lineHeight(1.4),
      fontWeight.bold,
      color(MakeStyles.Color.black)
    )
  )

  val themeDescription: StyleA = style(
    color(rgba(0, 0, 0, 0.3)),
    marginRight(10.px)
  )

  val linksContainer: StyleA = style(
    height(71.px),
    backgroundColor(c"#ffffff"),
    boxShadow := "0 0 4px 0 rgba(0, 0, 0, 0.3)",
    padding(20.px, 0.px)
  )

  val footerLogo: StyleA = style(
    unsafeChild("img")(
      width(60.px)
    )
  )

  val links: StyleA = style(
    unsafeChild("li")(
      float.left,
      fontSize(1.6.rem),
      margin(0.px, 7.px),
      fontWeight.bold
    )
  )

  val firstLink: StyleA = style (
    color(MakeStyles.Color.pink)
  )
}
