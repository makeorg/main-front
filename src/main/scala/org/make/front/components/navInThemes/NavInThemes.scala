package org.make.front.components.navInThemes

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.NumberFormat
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TextStyles}
import org.make.front.styles.utils._

object NavInThemes {

  case class WrappedProps(themes: Seq[TranslatedThemeModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[WrappedProps, Unit](
        displayName = "NavInThemes",
        render = self => {

          val themes: Seq[TranslatedThemeModel] = self.props.wrapped.themes
          val colors: Map[Int, String] = themes.map(theme => theme.order -> theme.color).toMap

          object DynamicNavInThemesStyles extends StyleSheet.Inline {
            import dsl._

            val themesColor: (Int) => StyleA = styleF.int(0 to themes.size) { index =>
              styleS(borderColor :=! colors.getOrElse(index, "black"))
            }
          }

          val listTheme = themes.map {
            theme =>
              <.li(
                ^.key := theme.slug,
                ^.className := Seq(
                  NavInThemesStyles.themeItem,
                  ColRulesStyles.col,
                  ColRulesStyles.colHalfBeyondSmall,
                  ColRulesStyles.colThirdBeyondLarge
                )
              )(
                <.Link(^.to := s"/theme/${theme.slug}", ^.className := NavInThemesStyles.themeLink)(
                  <.div(
                    ^.className := Seq(
                      NavInThemesStyles.themeItemContentWrapper,
                      DynamicNavInThemesStyles.themesColor(theme.order)
                    )
                  )(
                    <.h3(^.className := TextStyles.smallerTitle)(theme.title),
                    <.p(^.className := Seq(NavInThemesStyles.actionsCounter, TextStyles.smallText))(
                      unescape(
                        I18n.t(
                          "nav-in-themes.total-of-actions",
                          Replacements(("total", NumberFormat.formatToKilo(theme.actionsCount)))
                        )
                      )
                    ),
                    <.p(^.className := Seq(NavInThemesStyles.propositionsCounter, TextStyles.smallText))(
                      unescape(
                        I18n.t(
                          "nav-in-themes.total-of-proposals",
                          Replacements(("total", NumberFormat.formatToKilo(theme.proposalsCount)))
                        )
                      )
                    )
                  )
                )
              )
          }

          <.nav(^.className := NavInThemesStyles.wrapper)(
            <.div(^.className := Seq(NavInThemesStyles.titleWrapper, LayoutRulesStyles.centeredRow))(
              <.h2(^.className := Seq(TextStyles.mediumTitle))(unescape(I18n.t("nav-in-themes.title")))
            ),
            if (themes.nonEmpty) {
              <.ul(^.className := Seq(NavInThemesStyles.themesList, LayoutRulesStyles.centeredRowWithCols))(listTheme)
            } else {
              <.p(^.className := Seq(NavInThemesStyles.spinnerWrapper, LayoutRulesStyles.centeredRow))(
                <.SpinnerComponent.empty
              )
            },
            <.style()(NavInThemesStyles.render[String], DynamicNavInThemesStyles.render[String])
          )
        }
      )
}

object NavInThemesStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA = style(
    backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
    padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`, ThemeStyles.SpacingValue.small.pxToEm())
  )

  val titleWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val themesList: StyleA = style(display.flex, flexFlow := s"row wrap")

  val themeLink: StyleA = style(color(ThemeStyles.TextColor.base))

  val themeItem: StyleA =
    style(paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val themeItemContentWrapper: StyleA =
    style(height(100.%%), paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm()), borderLeft(5.px, solid))

  val actionsCounter: StyleA = style(
    display.inlineBlock,
    ThemeStyles.MediaQueries.belowMedium(display.none),
    marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
    verticalAlign.baseline,
    color(ThemeStyles.TextColor.lighter)
  )

  val propositionsCounter: StyleA = style(actionsCounter)

  val spinnerWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

}
