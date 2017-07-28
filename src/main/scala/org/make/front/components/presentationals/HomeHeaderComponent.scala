package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.styles.MakeStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object HomeHeaderComponent {
  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div(^.className := HomeHeaderStyles.container.htmlClass)(
        <.img(
          ^.className := HomeHeaderStyles.photo.htmlClass,
          ^.src := "homeIllustration.jpeg",
          ^.srcset := "homeIllustration-p-500.jpeg 500w, homeIllustration-p-800.jpeg 800w, homeIllustration.jpg 1440w"
        )(),
        <.div(^.className := HomeHeaderStyles.title.htmlClass)(
          <.button(^.className := HomeHeaderStyles.buttonSeeMore.htmlClass)("EN SAVOIR +")
        ),
        <.style()(HomeHeaderStyles.render[String])
    )
  )
}

object HomeHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val container: StyleA = style(position.relative)

  val photo: StyleA = style(position.relative, zIndex(1), width(100.%%))

  val title: StyleA =
    style(position.absolute, left(0 px), right(0 px), bottom(0 px), zIndex(5), paddingBottom(30 px), textAlign.center)

  val buttonSeeMore: StyleA = style(
    display.inlineBlock,
    marginTop(20 px),
    marginRight.auto,
    marginLeft.auto,
    borderRadius(30 px),
    backgroundColor(MakeStyles.Color.pink),
    boxShadow := "1px 1px 1px 0 rgba(0, 0, 0, .5)",
    color.white,
    textAlign.center,
    padding(12 px, 20 px, 10 px),
    textDecoration := "none",
    border := "none"
  )

  val buttonSeeMoreBig: StyleA =
    style(width(210 px), paddingTop(16 px), paddingBottom(14 px), fontSize(18 px), marginBottom(40 px))
}
