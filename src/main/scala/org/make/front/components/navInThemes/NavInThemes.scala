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

import scala.scalajs.js

object NavInThemes {

  case class NavInThemesProps(themes: js.Array[TranslatedThemeModel], shouldDisplayTheme: Boolean, country: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[NavInThemesProps, Unit](
        displayName = "NavInThemes",
        render = self => {

          val themes: js.Array[TranslatedThemeModel] = self.props.wrapped.themes
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
                ^.className := js.Array(
                  NavInThemesStyles.themeItem,
                  ColRulesStyles.col,
                  ColRulesStyles.colHalfBeyondSmall,
                  ColRulesStyles.colThirdBeyondLarge
                )
              )(
                <.Link(
                  ^.to := s"/${self.props.wrapped.country}/theme/${theme.slug}",
                  ^.className := NavInThemesStyles.themeLink
                )(
                  <.div(
                    ^.className := js.Array(
                      NavInThemesStyles.themeItemContentWrapper,
                      DynamicNavInThemesStyles.themesColor(theme.order)
                    )
                  )(
                    <.h3(^.className := TextStyles.smallerTitle)(theme.title),
                    <.p(^.className := js.Array(NavInThemesStyles.actionsCounter, TextStyles.smallText))(
                      unescape(
                        I18n.t(
                          "nav-in-themes.total-of-actions",
                          Replacements(
                            ("total", NumberFormat.formatToKilo(theme.actionsCount)),
                            ("count", theme.actionsCount.toString)
                          )
                        )
                      )
                    ),
                    <.p(^.className := js.Array(NavInThemesStyles.propositionsCounter, TextStyles.smallText))(
                      unescape(
                        I18n.t(
                          "nav-in-themes.total-of-proposals",
                          Replacements(
                            ("total", NumberFormat.formatToKilo(theme.proposalsCount)),
                            ("count", theme.proposalsCount.toString)
                          )
                        )
                      )
                    )
                  )
                )
              )
          }.toSeq

          <.nav(^.className := NavInThemesStyles.wrapper)(if (self.props.wrapped.shouldDisplayTheme) {
            js.Array(
                <.div(^.className := js.Array(NavInThemesStyles.titleWrapper, LayoutRulesStyles.centeredRow))(
                  <.h2(^.className := js.Array(TextStyles.mediumTitle))(unescape(I18n.t("nav-in-themes.title")))
                ),
                if (themes.nonEmpty) {
                  <.ul(^.className := js.Array(NavInThemesStyles.themesList, LayoutRulesStyles.centeredRowWithCols))(
                    listTheme
                  )
                } else {
                  <.p(^.className := js.Array(NavInThemesStyles.spinnerWrapper, LayoutRulesStyles.centeredRow))(
                    <.SpinnerComponent.empty
                  )
                },
                <.style()(NavInThemesStyles.render[String], DynamicNavInThemesStyles.render[String])
              )
              .toSeq
          })
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
