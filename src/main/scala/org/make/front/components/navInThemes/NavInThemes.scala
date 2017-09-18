package org.make.front.components.navInThemes

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.presentationals._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.NumberFormat
import org.make.front.models.{Theme => ThemeModel}
import org.make.front.styles.{LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length

object NavInThemes {

  case class WrappedProps(themes: Seq[ThemeModel])

  lazy val reactClass: ReactClass = React.createClass[WrappedProps, Unit](render = self => {

    val listTheme = self.props.wrapped.themes.map {
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
            <.div(^.className := NavInThemesStyles.themeItemContentWrapper(theme.color))(
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
        <.style()(NavInThemesStyles.render[String])
      )
    )
  })
}

object NavInThemesStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA = style(
    backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
    padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0 ${ThemeStyles.SpacingValue.small.pxToEm().value}"
  )

  val titleWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val themesList: StyleA = style(display.flex, flexFlow := s"row wrap")

  val themeLink: StyleA = style()

  val themeItem: StyleA =
    style(paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  def themeItemContentWrapper(color: String): StyleA =
    style(height(100.%%), paddingLeft :=! ThemeStyles.SpacingValue.smaller.pxToEm(), borderLeft :=! s"5px solid $color")

  val actionsCounter: StyleA = style(
    display.inlineBlock,
    ThemeStyles.MediaQueries.belowMedium(display.none),
    marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
    verticalAlign.baseline,
    color(ThemeStyles.TextColor.lighter)
  )

  val propositionsCounter: StyleA = style(actionsCounter)

}
