package org.make.front.components.navInThemes

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.NumberFormat
import org.make.front.models.{Theme => ThemeModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet.Inline

object NavInThemes {

  case class WrappedProps(themes: Seq[ThemeModel])

  lazy val reactClass: ReactClass =
    React.createClass[WrappedProps, Unit](displayName = "NavInThemes", render = self => {

      val themes: Seq[ThemeModel] = self.props.wrapped.themes
      val colors: Map[Int, String] = themes.map(theme => theme.order -> theme.color).toMap

      object DynamicThemesStylesheet extends Inline {
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
              LayoutRulesStyles.col,
              LayoutRulesStyles.colHalfBeyondSmall,
              LayoutRulesStyles.colThirdBeyondLarge
            )
          )(
            <.Link(^.to := s"/theme/${theme.slug}", ^.className := NavInThemesStyles.themeLink)(
              <.div(
                ^.className := Seq(
                  NavInThemesStyles.themeItemContentWrapper,
                  DynamicThemesStylesheet.themesColor(theme.order)
                )
              )(
                <.h3(^.className := TextStyles.smallerTitle)(theme.title),
                <.p(^.className := Seq(NavInThemesStyles.actionsCounter, TextStyles.smallText))(
                  unescape(
                    I18n.t(
                      "content.theme.actionsCount",
                      Replacements(("actions", NumberFormat.formatToKilo(theme.actionsCount)))
                    )
                  )
                ),
                <.p(^.className := Seq(NavInThemesStyles.propositionsCounter, TextStyles.smallText))(
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

      <.nav(^.className := NavInThemesStyles.wrapper)(
        <.div(^.className := LayoutRulesStyles.centeredRow)(
          <.div(^.className := Seq(NavInThemesStyles.titleWrapper, LayoutRulesStyles.col))(
            <.h2(^.className := Seq(TextStyles.mediumTitle))(unescape(I18n.t("content.footer.title")))
          ),
          <.ul(^.className := Seq(NavInThemesStyles.themesList))(listTheme),
          <.style()(NavInThemesStyles.render[String]),
          <.style()(DynamicThemesStylesheet.render[String])
        )
      )
    })
}

object NavInThemesStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA = style(
    backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
    padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0 ${ThemeStyles.SpacingValue.small.pxToEm().value}"
  )

  val titleWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val themesList: StyleA = style(display.flex, flexFlow := s"row wrap")

  val themeLink: StyleA = style()

  val themeItem: StyleA =
    style(paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val themeItemContentWrapper: StyleA =
    style(height(100.%%), paddingLeft :=! ThemeStyles.SpacingValue.smaller.pxToEm(), borderLeft :=! s"5px solid")

  val actionsCounter: StyleA = style(
    display.inlineBlock,
    ThemeStyles.MediaQueries.belowMedium(display.none),
    marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
    verticalAlign.baseline,
    color(ThemeStyles.TextColor.lighter)
  )

  val propositionsCounter: StyleA = style(actionsCounter)

}
