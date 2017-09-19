package org.make.front.components.PoliticalActions

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.components.politicalActions.PoliticalAction.PoliticalActionProps
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{PoliticalAction => PoliticalActionModel}
import org.make.front.styles._
import org.scalajs.dom

import scalacss.DevDefaults._
import scalacss.internal.Length

object PoliticalActionsList {

  final case class PoliticalActionsListProps(politicalActions: Seq[PoliticalActionModel])

  final case class PoliticalActionsListState(translationOffset: Int)

  def onClickLeft(self: Self[PoliticalActionsListProps, PoliticalActionsListState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent =>
      {
        val width = dom.document.getElementById("slideshow").clientWidth
        val oldTranslationOffset = self.state.translationOffset
        if (oldTranslationOffset != 0)
          self.setState(_.copy(translationOffset = oldTranslationOffset + width))
      }
  }

  def onClickRight(self: Self[PoliticalActionsListProps, PoliticalActionsListState]): (MouseSyntheticEvent) => Unit = {
    _: MouseSyntheticEvent =>
      {
        val width = dom.document.getElementById("slideshow").clientWidth
        val oldTranslationOffset = self.state.translationOffset
        if (oldTranslationOffset != -width * (self.props.wrapped.politicalActions.length - 1))
          self.setState(_.copy(translationOffset = oldTranslationOffset - width))
      }
  }

  lazy val reactClass: ReactClass = React.createClass[PoliticalActionsListProps, PoliticalActionsListState](
    getInitialState = (_) => PoliticalActionsListState(translationOffset = 0),
    render = (self) =>
      <.section(^.className := PoliticalActionsListStyles.wrapper)(
        <.div(^.className := LayoutRulesStyles.centeredRow)(
          <.header(^.className := LayoutRulesStyles.col)(
            <.h2(^.className := TextStyles.bigTitle)(
              unescape(
                I18n.t(
                  "content.theme.actionsCount",
                  Replacements(("actions", self.props.wrapped.politicalActions.length.toString))
                )
              )
            )
          ),
          <.div(^.className := LayoutRulesStyles.col)(
            <.div(^.className := PoliticalActionsListStyles.slideshowWrapper)(
              <.div(^.className := PoliticalActionsListStyles.slideshowContentWrapper)(
                <.ul(^.className := Seq(PoliticalActionsListStyles.slideshow), ^.id := "slideshow")(
                  self.props.wrapped.politicalActions.map { politicalAction =>
                    <.li(^.className := Seq(PoliticalActionsListStyles.slide))(
                      <.PoliticalActionComponent(^.wrapped := PoliticalActionProps(politicalAction))()
                    )
                  }
                )
              ),
              <.nav(
                ^.className := Seq(PoliticalActionsListStyles.slideshowNav, RWDHideRulesStyles.showBlockBeyondMedium)
              )(
                <.button(
                  ^.className := Seq(FontAwesomeStyles.angleLeft, PoliticalActionsListStyles.slideshowArrow),
                  ^.onClick := onClickLeft(self)
                )(),
                <.button(
                  ^.className := Seq(FontAwesomeStyles.angleRight, PoliticalActionsListStyles.slideshowArrow),
                  ^.onClick := onClickRight(self)
                )()
              )
            )
          )
        ),
        <.style()(PoliticalActionsListStyles.render[String])
    )
  )

}

object PoliticalActionsListStyles extends StyleSheet.Inline {
  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0"
    )

  val slideshow: StyleA =
    style()

  val slideshowWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(
        display.table,
        width(100.%%),
        backgroundColor(ThemeStyles.BackgroundColor.white),
        boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
      )
    )

  val slideshowContentWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondMedium(display.tableCell, width(100.%%)))

  val slideshowNav: StyleA =
    style(ThemeStyles.MediaQueries.beyondMedium(display.tableCell, verticalAlign.middle, whiteSpace.nowrap))

  val slideshowArrow: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(
        display.inlineBlock,
        verticalAlign.middle,
        margin(15.pxToEm(54)),
        fontSize(54.pxToEm()),
        color(ThemeStyles.TextColor.base),
        opacity(0.1),
        transition := "opacity .2s ease-in-out",
        (&.hover)(opacity(1))
      )
    )

  val slide: StyleA =
    style(
      ThemeStyles.MediaQueries
        .belowMedium(backgroundColor(ThemeStyles.BackgroundColor.white), boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)")
    )

}
