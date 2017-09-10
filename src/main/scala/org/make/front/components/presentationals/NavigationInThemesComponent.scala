package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.NumberFormat
import org.make.front.models.Theme
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.{LayoutRulesStyles, TextStyles, ThemeStyles}

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
            LayoutRulesStyles.col,
            LayoutRulesStyles.colHalfBeyondSmall,
            LayoutRulesStyles.colThirdBeyondLarge
          )
        )(
          <.Link(^.to := s"/theme/${theme.slug}", ^.className := NavigationInThemesStyles.themeLink)(
            <.div(^.className := NavigationInThemesStyles.themeItemContentWrapper(theme.color))(
              <.h3(^.className := TextStyles.smallerTitle)(theme.title),
              <.p(^.className := Seq(NavigationInThemesStyles.actionsCounter, TextStyles.smallText))(
                unescape(
                  I18n.t(
                    "content.theme.actionsCount",
                    Replacements(("actions", NumberFormat.formatToKilo(theme.actionsCount)))
                  )
                )
              ),
              <.p(^.className := Seq(NavigationInThemesStyles.propositionsCounter, TextStyles.smallText))(
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

    <.nav(^.className := NavigationInThemesStyles.wrapper)(
      <.div(^.className := LayoutRulesStyles.centeredRow)(
        <.div(^.className := Seq(LayoutRulesStyles.col))(
          <.h2(^.className := Seq(NavigationInThemesStyles.title, TextStyles.mediumTitle))(
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
    backgroundColor(ThemeStyles.BackgroundColor.blackTransparent),
    padding :=! s"${ThemeStyles.Spacing.medium.value} 0 ${ThemeStyles.Spacing.small.value}"
  )

  val title: StyleA = style(marginBottom(ThemeStyles.Spacing.small))

  val themesList: StyleA = style(display.flex, flexFlow := s"row wrap")

  val themeLink: StyleA = style()

  val themeItem: StyleA =
    style(paddingBottom(ThemeStyles.Spacing.small))

  def themeItemContentWrapper(color: String): StyleA =
    style(height(100.%%), paddingLeft :=! ThemeStyles.Spacing.smaller, borderLeft :=! s"5px solid $color")

  val actionsCounter: StyleA = style(
    display.inlineBlock,
    ThemeStyles.MediaQueries.belowMedium(display.none),
    marginRight(ThemeStyles.Spacing.small),
    verticalAlign.baseline,
    color(ThemeStyles.TextColor.lighter)
  )

  val propositionsCounter: StyleA = style(actionsCounter)

}
