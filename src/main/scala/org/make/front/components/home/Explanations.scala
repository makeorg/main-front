package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object Explanations {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    displayName = "Explanations",
    render = (_) =>
      <.section(^.className := ExplanationsStyles.wrapper)(
        <.div(^.className := Seq(RowRulesStyles.centeredRow))(
          <.article(
            ^.className := Seq(ExplanationsStyles.article, ColRulesStyles.col, ColRulesStyles.colHalfBeyondMedium)
          )(
            <.h3(^.className := Seq(ExplanationsStyles.intro, TextStyles.mediumText, TextStyles.intro))(
              "la politique ne suffit plus, faisons bouger les lignes ensemble"
            ),
            <.h2(^.className := TextStyles.mediumTitle)("Votez, proposez, agissons"),
            <.ul()(
              <.li(^.className := ExplanationsStyles.item)(
                <.span(^.className := Seq(ExplanationsStyles.icon, FontAwesomeStyles.fa, FontAwesomeStyles.thumbsUp))(),
                <.p(
                  ^.className := TextStyles.mediumText,
                  ^.dangerouslySetInnerHTML := "<strong>Votez</strong> pour les propositions que vous défendez"
                )()
              ),
              <.li(^.className := ExplanationsStyles.item)(
                <.span(
                  ^.className := Seq(
                    ExplanationsStyles.icon,
                    FontAwesomeStyles.fa,
                    FontAwesomeStyles.lightbulbTransparent
                  )
                )(),
                <.p(
                  ^.className := TextStyles.mediumText,
                  ^.dangerouslySetInnerHTML := "<strong>Proposez</strong> vos idées sur des thèmes qui vous inspirent"
                )()
              ),
              <.li(^.className := ExplanationsStyles.item)(
                <.span(^.className := Seq(ExplanationsStyles.icon, FontAwesomeStyles.fa, FontAwesomeStyles.group))(),
                <.p(
                  ^.className := TextStyles.mediumText,
                  ^.dangerouslySetInnerHTML := "Et demain, <strong>initiez des actions</strong> concrètes sur le terrain"
                )()
              )
            ) /*,
            <.p(^.className := IntroStyles.ctaWrapper)(
              <.a(^.href := "/", ^.className := Seq(CTAStyles.basic, CTAStyles.negative, CTAStyles.basicOnA))(
                "En savoir +"
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
              "qui sommes-nous ?"
            ),
            <.h2(^.className := TextStyles.mediumTitle)("Make.org, neutre & indépendant"),
            <.p(^.className := Seq(ExplanationsStyles.paragraph, TextStyles.mediumText))(
              "Make.org est une initiative civique, Européenne et indépendante."
            ),
            <.p(^.className := IntroStyles.ctaWrapper)(
              <.a(
                ^.href := "https://about.make.org/qui-sommes-nous",
                ^.target := "_blank",
                ^.className := Seq(CTAStyles.basic, CTAStyles.negative, CTAStyles.basicOnA)
              )("En savoir +")
            )
          )
        ),
        <.style()(ExplanationsStyles.render[String])
    )
  )
}

object ExplanationsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0",
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
          backgroundColor :=! "rgba(255, 255, 255, 0.6)"
        )
      ),
      ThemeStyles.MediaQueries.beyondMedium(
        borderLeft :=! s"1px solid rgba(255, 255, 255, 0.6)",
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
    margin :=! s"${ThemeStyles.SpacingValue.smaller.pxToEm().value} 0",
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
    style(
      display.inlineBlock,
      width(100.%%),
      margin :=! s"${ThemeStyles.SpacingValue.smaller.pxToEm().value} 0",
      opacity(0.7)
    )

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()))

}
