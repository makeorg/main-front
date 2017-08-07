package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.facades.{homeIllustration, I18n}
import org.make.front.styles.{BulmaStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object HomeHeaderComponent {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div(^.className := HomeHeaderStyles.container.htmlClass)(
        <.div(^.className := HomeHeaderStyles.titleContainer.htmlClass)(
          <.div(
            ^.className := Seq(BulmaStyles.Element.button, BulmaStyles.Syntax.isDark, HomeHeaderStyles.buttonLaUne)
          )(I18n.t("content.homepage.baseline")),
          <.h2(^.className := HomeHeaderStyles.titleText.htmlClass)(I18n.t("content.homepage.title")),
          <.h5(^.className := HomeHeaderStyles.subTitleText.htmlClass)(I18n.t("content.homepage.subTitle")),
          <.button(^.className := HomeHeaderStyles.buttonSeeMore.htmlClass)(I18n.t("content.homepage.textSeeMore"))
        ),
        <.style()(HomeHeaderStyles.render[String])
    )
  )
}

object HomeHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val container: StyleA =
    style(
      position.relative,
      paddingTop(15.rem),
      paddingBottom(15.rem),
      backgroundImage := s"url('${homeIllustration.toString}')",
      backgroundSize := "cover"
    )

  val titleContainer: StyleA = style(zIndex(5), textAlign.center)

  val titleText: StyleA = style(color :=! MakeStyles.Color.white, fontSize(5.rem), fontWeight.bold)

  val subTitleText: StyleA = style(color :=! MakeStyles.Color.white, fontSize(1.5.rem))

  val buttonLaUne: StyleA = style(textTransform.uppercase, fontSize(1.5.rem), borderRadius :=! "0", fontWeight.bold)

  val buttonSeeMore: StyleA = style(
    display.inlineBlock,
    marginTop(2.rem),
    marginRight.auto,
    marginLeft.auto,
    borderRadius(10.rem),
    backgroundColor(MakeStyles.Color.pink),
    boxShadow := "0.1rem 0.1rem 0.1rem 0 rgba(0, 0, 0, .5)",
    padding(1.2.rem, 2.rem, 1.2.rem),
    border :=! "none",
    textDecoration := "none",
    textTransform.uppercase,
    fontWeight.bold,
    fontSize(1.3.rem),
    color.white,
    textAlign.center
  )

  val buttonSeeMoreBig: StyleA =
    style(width(210 px), paddingTop(16 px), paddingBottom(14 px), fontSize(18 px), marginBottom(40 px))
}
