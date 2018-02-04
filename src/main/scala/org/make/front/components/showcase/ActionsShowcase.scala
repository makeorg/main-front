package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, RWDHideRulesStyles, TextStyles}
import org.make.front.styles.utils._

object ActionsShowcase {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "ActionsShowcase",
        render = { _ =>
          def actionTile() = <.article()(
            <.div(^.className := ActionTileStyles.illWrapper)(
              <.img(
                ^.src := "https://placekitten.com/720/400",
                ^.alt := "Un million d'euros pour financer vos projets innovants"
              )()
            ),
            <.div(^.className := ActionTileStyles.contentWrapper)(
              <.div(^.className := ActionTileStyles.labelWrapper)(
                <.div(^.className := ActionTileStyles.labelInnerWrapper)(
                  <.p(^.className := Seq(ActionTileStyles.label, TextStyles.label))("action en cours")
                )
              ),
              <.div(^.className := ActionTileStyles.excerptWrapper)(
                <.p(^.className := TextStyles.mediumText)(
                  "Un million d'euros pour financer vos projets innovants",
                  <.br()(),
                  <.a(^.href := "#", ^.className := TextStyles.boldText)("En savoir +")
                )
              )
            )
          )

          <.section(^.className := ActionsShowcaseStyles.wrapper)(
            <.header(^.className := LayoutRulesStyles.centeredRow)(
              <.h2(^.className := TextStyles.mediumTitle)("Agir avec Make.org")
            ),
            <.div(
              ^.className := Seq(
                RWDHideRulesStyles.hideBeyondMedium,
                LayoutRulesStyles.centeredRowWithCols,
                ActionsShowcaseStyles.slideshow
              )
            )(
              <.Slider(^.infinite := false, ^.arrows := false)(
                Range(0, 3).map(
                  item =>
                    <.div(
                      ^.className :=
                        Seq(ColRulesStyles.col, ActionTileStyles.wrapper)
                    )(actionTile())
                )
              )
            ),
            <.div(
              ^.className := Seq(
                RWDHideRulesStyles.showBlockBeyondMedium,
                RWDHideRulesStyles.hideBeyondLarge,
                LayoutRulesStyles.centeredRowWithCols,
                ActionsShowcaseStyles.slideshow
              )
            )(
              <.Slider(^.infinite := false, ^.arrows := false, ^.slidesToShow := 2, ^.slidesToScroll := 2)(
                Range(0, 3).map(
                  item =>
                    <.div(
                      ^.className := Seq(
                        ActionTileStyles.wrapper,
                        ColRulesStyles.col,
                        ColRulesStyles.colHalfBeyondMedium
                      )
                    )(actionTile())
                )
              )
            ),
            <.div(^.className := RWDHideRulesStyles.showBlockBeyondLarge)(
              <.ul(^.className := LayoutRulesStyles.centeredRowWithCols)(
                Range(0, 3).map(
                  item =>
                    <.li(
                      ^.className := Seq(
                        ActionTileStyles.wrapper,
                        ColRulesStyles.col,
                        ColRulesStyles.colThirdBeyondLarge
                      )
                    )(actionTile())
                )
              )
            ),
            <.style()(ActionsShowcaseStyles.render[String], ActionTileStyles.render[String])
          )
        }
      )

}

object ActionsShowcaseStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(
        padding(
          ThemeStyles.SpacingValue.larger.pxToEm(),
          `0`,
          (ThemeStyles.SpacingValue.larger - ThemeStyles.SpacingValue.small).pxToEm()
        )
      ),
      overflow.hidden
    )

  val slideshow: StyleA =
    style(
      width(95.%%),
      unsafeChild(".slick-list")(overflow.visible),
      unsafeChild(".slick-slide")(height.auto, minHeight.inherit),
      unsafeChild(".slick-track")(display.flex)
    )
}

object ActionTileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val illWrapper: StyleA =
    style(
      position.relative,
      padding(((50 * 100) / 180).%%, 50.%%),
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      overflow.hidden,
      unsafeChild("img")(
        position.absolute,
        top(50.%%),
        left(50.%%),
        height.auto,
        maxHeight.none,
        minHeight(100.%%),
        width(100.%%),
        maxWidth.none,
        minWidth(100.%%),
        transform := s"translate(-50%, -50%)"
      )
    )

  val contentWrapper: StyleA =
    style(position.relative)

  val labelWrapper: StyleA =
    style(
      position.absolute,
      left(`0`),
      bottom(100.%%),
      width(100.%%),
      padding(`0`, (ThemeStyles.SpacingValue.largerMedium / 2).pxToEm()),
      overflow.hidden
    )

  val labelInnerWrapper: StyleA =
    style(
      position.relative,
      padding(`0`, (ThemeStyles.SpacingValue.largerMedium / 2).pxToEm()),
      textAlign.center,
      (&.before)(
        content := "''",
        position.absolute,
        left(`0`),
        bottom(`0`),
        display.block,
        height(50.%%),
        width(100.%%),
        backgroundColor(ThemeStyles.TextColor.white),
        boxShadow := "0 0px 4px 0 rgba(0,0,0,0.5)"
      )
    )
  val label: StyleA =
    style(position.relative)

  val excerptWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.smaller.pxToEm(), ThemeStyles.SpacingValue.largerMedium.pxToEm(), `0`),
      unsafeChild("a")(color(ThemeStyles.ThemeColor.primary))
    )
}
