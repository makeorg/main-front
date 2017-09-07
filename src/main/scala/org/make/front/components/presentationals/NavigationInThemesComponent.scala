package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.NumberFormat
import org.make.front.models.Theme
import org.make.front.styles.MakeStyles
import org.make.front.components.LayoutStyleSheet
import org.make.front.components.TextStyleSheet
import org.make.front.facades.Unescape.unescape

import scalacss.DevDefaults._

object NavigationInThemesComponent {

  case class WrappedProps(themes: Seq[Theme])

  lazy val reactClass: ReactClass = React.createClass[WrappedProps, Unit](render = self => {

    val listTheme = self.props.wrapped.themes.map {
      theme =>
        <.li(
          ^.key := theme.slug,
          ^.className := Seq(
            NavigationInThemesStyles.themeItem,
            LayoutStyleSheet.col,
            LayoutStyleSheet.colHalfBeyondSmall,
            LayoutStyleSheet.colThirdBeyondLarge
          )
        )(
          <.Link(^.to := s"/theme/${theme.slug}", ^.className := NavigationInThemesStyles.themeLink)(
            <.div(^.className := NavigationInThemesStyles.themeItemContentWrapper(theme.color))(
              <.h3(^.className := TextStyleSheet.smallerTitle)(theme.title),
              <.p(^.className := Seq(NavigationInThemesStyles.actionsCounter, TextStyleSheet.smallText))(
                unescape(
                  I18n.t(
                    "content.theme.actionsCount",
                    Replacements(("actions", NumberFormat.formatToKilo(theme.actionsCount)))
                  )
                )
              ),
              <.p(^.className := Seq(NavigationInThemesStyles.propositionsCounter, TextStyleSheet.smallText))(
                unescape(
                  I18n.t(
                    "content.theme.proposalsCount",
                    Replacements(("proposals", NumberFormat.formatToKilo(theme.proposalsCount)))
                  )
                )
              )
            )
          )
        )
    }

    <.section(^.className := NavigationInThemesStyles.wrapper)(
      <.div(^.className := LayoutStyleSheet.centeredRow)(
        <.header(^.className := Seq(LayoutStyleSheet.col))(
          <.h2(^.className := Seq(NavigationInThemesStyles.title, TextStyleSheet.mediumTitle))(
            unescape(I18n.t("content.footer.title"))
          )
        ),
        <.ul(^.className := Seq(NavigationInThemesStyles.themesList))(listTheme),
        <.style()(NavigationInThemesStyles.render[String])
      )
    )
  })
}

object NavigationInThemesStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA = style(
    backgroundColor(MakeStyles.BackgroundColor.blackTransparent),
    padding :=! s"${MakeStyles.Spacing.medium.value} 0 ${MakeStyles.Spacing.small.value}"
  )

  val title: StyleA = style(marginBottom(MakeStyles.Spacing.small))

  val themesList: StyleA = style(display.flex, flexFlow := s"row wrap")

  val themeLink: StyleA = style()

  val themeItem: StyleA =
    style(paddingBottom(MakeStyles.Spacing.small))

  def themeItemContentWrapper(color: String): StyleA =
    style(height(100.%%), paddingLeft :=! MakeStyles.Spacing.smaller, borderLeft :=! s"5px solid $color")

  val actionsCounter: StyleA = style(
    display.inlineBlock,
    MakeStyles.MediaQueries.belowMedium(display.none),
    marginRight(MakeStyles.Spacing.small),
    verticalAlign.baseline,
    color(MakeStyles.TextColor.lighter)
  )

  val propositionsCounter: StyleA = style(actionsCounter)

}
