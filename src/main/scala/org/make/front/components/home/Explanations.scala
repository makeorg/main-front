package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object Explanations {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "Explanations",
        render = { _ =>
          val openTarget: () => Unit = () => {
            TrackingService.track("click-button-whoweare", TrackingContext(TrackingLocation.showcaseHomepage))
            scalajs.js.Dynamic.global.window.open(I18n.t("home.explanations.article-2.see-more-link"), "_blank")
          }
          <.section(^.className := ExplanationsStyles.wrapper)(
            <.div(^.className := Seq(LayoutRulesStyles.centeredRowWithCols))(
              <.article(
                ^.className := Seq(ExplanationsStyles.article, ColRulesStyles.col, ColRulesStyles.colHalfBeyondMedium)
              )(
                <.h3(^.className := Seq(ExplanationsStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                  unescape(I18n.t("home.explanations.article-1.intro"))
                ),
                <.h2(^.className := TextStyles.mediumTitle)(unescape(I18n.t("home.explanations.article-1.title"))),
                <.ul()(
                  <.li(^.className := ExplanationsStyles.item)(
                    <.span(^.className := Seq(ExplanationsStyles.icon, FontAwesomeStyles.thumbsUp))(),
                    <.p(
                      ^.className := TextStyles.mediumText,
                      ^.dangerouslySetInnerHTML := I18n.t("home.explanations.article-1.item-1")
                    )()
                  ),
                  <.li(^.className := ExplanationsStyles.item)(
                    <.span(^.className := Seq(ExplanationsStyles.icon, FontAwesomeStyles.lightbulbTransparent))(),
                    <.p(
                      ^.className := TextStyles.mediumText,
                      ^.dangerouslySetInnerHTML := I18n.t("home.explanations.article-1.item-2")
                    )()
                  ),
                  <.li(^.className := ExplanationsStyles.item)(
                    <.span(^.className := Seq(ExplanationsStyles.icon, FontAwesomeStyles.group))(),
                    <.p(
                      ^.className := TextStyles.mediumText,
                      ^.dangerouslySetInnerHTML := I18n.t("home.explanations.article-1.item-3")
                    )()
                  )
                ) /*,
            <.p(^.className := IntroStyles.ctaWrapper)(
              <.a(^.href := I18n.t("home.explanations.article-1.see-more-link"), ^.className := Seq(CTAStyles.basic, CTAStyles.negative, CTAStyles.basicOnA))(
                unescape(I18n.t("home.explanations.article-1.see-more"))
              )
            )*/
              ),
              <.article(
                ^.className := Seq(
                  ExplanationsStyles.article,
                  ExplanationsStyles.secondArticle,
                  ColRulesStyles.col,
                  ColRulesStyles.colHalfBeyondMedium
                )
              )(
                <.h3(^.className := Seq(ExplanationsStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                  unescape(I18n.t("home.explanations.article-2.intro"))
                ),
                <.h2(^.className := TextStyles.mediumTitle)(unescape(I18n.t("home.explanations.article-2.title"))),
                <.p(^.className := Seq(ExplanationsStyles.paragraph, TextStyles.mediumText))(
                  unescape(I18n.t("home.explanations.article-2.text"))
                ),
                <.p(^.className := WelcomeStyles.ctaWrapper)(
                  <.button(
                    ^.onClick := openTarget,
                    ^.className := Seq(CTAStyles.basic, CTAStyles.negative, CTAStyles.basicOnButton)
                  )(unescape(I18n.t("home.explanations.article-2.see-more")))
                )
              )
            ),
            <.style()(ExplanationsStyles.render[String])
          )
        }
      )
}

object ExplanationsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      backgroundColor(ThemeStyles.ThemeColor.primary),
      color(ThemeStyles.TextColor.white)
    )

  val article: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(
        paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.small.pxToEm())
      )
    )

  val secondArticle: StyleA =
    style(
      ThemeStyles.MediaQueries.belowMedium(
        (&.before)(
          content := "''",
          display.block,
          height(1.px),
          width(100.%%),
          marginTop(20.pxToEm()),
          marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
          backgroundColor(rgba(255, 255, 255, 0.6))
        )
      ),
      ThemeStyles.MediaQueries.beyondMedium(
        borderLeft(1.px, solid, rgba(255, 255, 255, 0.6)),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val intro: StyleA =
    style(marginBottom(5.pxToEm(15)), ThemeStyles.MediaQueries.beyondSmall(marginBottom(5.pxToEm(18))))

  val item: StyleA = style(
    position.relative,
    display.inlineBlock,
    width(100.%%),
    paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm()),
    margin(ThemeStyles.SpacingValue.smaller.pxToEm(), `0`),
    opacity(0.7)
  )

  val icon: StyleA =
    style(
      position.absolute,
      left(`0`),
      top(3.pxToEm(15)),
      fontSize(15.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(top(3.pxToEm(18)), fontSize(18.pxToEm()))
    )

  val paragraph: StyleA =
    style(display.inlineBlock, width(100.%%), margin(ThemeStyles.SpacingValue.smaller.pxToEm(), `0`), opacity(0.7))

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()))

}
