package org.make.front.components.politicalActions

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.politicalActions.PoliticalAction.PoliticalActionProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements, Slider}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{PoliticalAction => PoliticalActionModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLElement

import scalacss.DevDefaults._

object PoliticalActionsList {

  final case class PoliticalActionsListProps(politicalActions: Seq[PoliticalActionModel])

  final case class PoliticalActionsListState(politicalActions: Seq[PoliticalActionModel])

  lazy val reactClass: ReactClass = React.createClass[PoliticalActionsListProps, PoliticalActionsListState](
    displayName = "PoliticalActionsList",
    getInitialState = (self) => PoliticalActionsListState(self.props.wrapped.politicalActions),
    componentWillReceiveProps = { (self, props) =>
      self.setState(PoliticalActionsListState(props.wrapped.politicalActions))
    },
    render = { (self) =>
      var previousButton: Option[HTMLElement] = None
      var nextButton: Option[HTMLElement] = None

      var slider: Option[Slider] = None
      val size = self.props.wrapped.politicalActions.size

      updateArrowsVisibility(0)

      def updateArrowsVisibility(currentSlide: Int): Unit = {
        if (currentSlide == 0) {
          previousButton.foreach { button =>
            button.setAttribute("disabled", "false")
          }
        } else {
          previousButton.foreach { button =>
            button.removeAttribute("disabled")
            button.onclick = (_) => previous()
          }
        }

        if (currentSlide + 1 >= size) {
          nextButton.foreach { button =>
            button.setAttribute("disabled", "false")
          }
        } else {
          nextButton.foreach { button =>
            button.removeAttribute("disabled")
            button.onclick = (_) => next()
          }
        }
      }

      def next: () => Unit = { () =>
        slider.foreach(_.slickNext())
      }

      def previous: () => Unit = { () =>
        slider.foreach(_.slickPrev())
      }

      <.section(^.className := PoliticalActionsListStyles.wrapper)(
        <.div(^.className := RowRulesStyles.centeredRow)(
          <.header(^.className := ColRulesStyles.col)(
            <.h2(^.className := TextStyles.bigTitle)(
              unescape(
                I18n.t(
                  "content.theme.matrix.introOfActions",
                  Replacements(("actions", self.props.wrapped.politicalActions.length.toString))
                )
              )
            )
          ),
          <.div(^.className := ColRulesStyles.col)(
            <.div(^.className := PoliticalActionsListStyles.slideshowWrapper)(
              <.div(^.className := PoliticalActionsListStyles.slideshowContentWrapper)(
                <.div(^.className := Seq(PoliticalActionsListStyles.slideshow))(
                  if (self.state.politicalActions.isEmpty) {
                    <.NoPoliticalActionComponent.empty
                  } else {
                    <.Slider(^.ref := ((s: HTMLElement) => {
                      slider = Option(s.asInstanceOf[Slider])
                      /*TODO : avoid this non solution*/
                      slider.foreach(_.slickGoTo(0))
                      slider
                    }), ^.infinite := false, ^.arrows := false, ^.afterChange := updateArrowsVisibility)(
                      self.state.politicalActions.map { politicalAction =>
                        <.div(^.className := Seq(PoliticalActionsListStyles.slideWrapper))(
                          <.div(^.className := Seq(PoliticalActionsListStyles.slide))(
                            <.PoliticalActionComponent(^.wrapped := PoliticalActionProps(politicalAction))()
                          )
                        )
                      }
                    )
                  }
                )
              ),
              <.nav(^.className := Seq(PoliticalActionsListStyles.slideshowNav))(
                <.button(
                  ^.className := Seq(
                    FontAwesomeStyles.fa,
                    FontAwesomeStyles.angleLeft,
                    PoliticalActionsListStyles.slideshowArrow
                  ),
                  ^.ref := ((e: HTMLElement) => { previousButton = Some(e) }),
                  ^.disabled := true,
                  ^.onClick := previous
                )(),
                <.button(
                  ^.className := Seq(
                    FontAwesomeStyles.fa,
                    FontAwesomeStyles.angleRight,
                    PoliticalActionsListStyles.slideshowArrow
                  ),
                  ^.ref := ((e: HTMLElement) => { nextButton = Some(e) }),
                  ^.disabled := size < 2,
                  ^.onClick := next
                )()
              )
            )
          )
        ),
        <.style()(PoliticalActionsListStyles.render[String])
      )
    }
  )

}

object PoliticalActionsListStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0", overflow.hidden)

  val slideshow: StyleA =
    style(
      unsafeChild(".slick-list")(
        ThemeStyles.MediaQueries
          .belowMedium(overflow.visible)
      ),
      unsafeChild(".slick-slide")(
        ThemeStyles.MediaQueries
          .belowMedium(height.auto, minHeight.inherit)
      ),
      unsafeChild(".slick-track")(
        ThemeStyles.MediaQueries
          .belowMedium(display.flex)
      )
    )

  val slideshowWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(
        display.table,
        tableLayout.fixed,
        width(100.%%),
        backgroundColor(ThemeStyles.BackgroundColor.white),
        boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
      )
    )

  val slideshowContentWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondMedium(display.tableCell, width(100.%%)))

  val slideshowNav: StyleA =
    style(
      display.none,
      ThemeStyles.MediaQueries
        .beyondMedium(display.tableCell, verticalAlign.middle, whiteSpace.nowrap, width(100.pxToEm()))
    )

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
        (&.hover)(opacity(1)),
        (&.disabled)(opacity :=! "0!important")
      )
    )

  val slideWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries
        .belowMedium(padding :=! s"0 ${ThemeStyles.SpacingValue.small.pxToEm().value} 0 0"),
      ThemeStyles.MediaQueries
        .belowSmall(padding :=! s"0 ${ThemeStyles.SpacingValue.smaller.pxToEm().value} 0 0")
    )

  val slide: StyleA =
    style(
      ThemeStyles.MediaQueries
        .belowMedium(
          height(100.%%),
          backgroundColor(ThemeStyles.BackgroundColor.white),
          boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
        )
    )

}
