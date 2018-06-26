/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
import org.make.front.styles.base._
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLElement
import org.make.front.Main.CssSettings._

import scala.scalajs.js

object PoliticalActionsList {

  final case class PoliticalActionsListProps(politicalActions: js.Array[PoliticalActionModel])

  final case class PoliticalActionsListState(politicalActions: js.Array[PoliticalActionModel])

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
        <.div(^.className := LayoutRulesStyles.centeredRow)(
          <.h2(^.className := TextStyles.mediumTitle)(
            unescape(
              I18n.t(
                "political-actions.intro",
                Replacements(
                  ("actions", self.props.wrapped.politicalActions.size.toString),
                  ("count", self.props.wrapped.politicalActions.size.toString)
                )
              )
            )
          ),
          if (self.state.politicalActions.isEmpty) {
            <.div(^.className := PoliticalActionsListStyles.noPoliticalActionsWrapper)(
              <.NoPoliticalActionComponent.empty
            )
          } else {
            <.div(
              ^.className := js
                .Array(TableLayoutBeyondMediumStyles.wrapper, PoliticalActionsListStyles.slideshowWrapper)
            )(
              <.div(
                ^.className := js
                  .Array(TableLayoutBeyondMediumStyles.cell, PoliticalActionsListStyles.slideshowContentWrapper)
              )(
                <.div(^.className := PoliticalActionsListStyles.slideshow)(
                  <.Slider(^.ref := ((slideshow: HTMLElement) => {
                    slider = Option(slideshow.asInstanceOf[Slider])
                    /*TODO : avoid this non solution*/
                    slider.foreach(_.slickGoTo(0))
                    slider
                  }), ^.infinite := false, ^.arrows := false, ^.afterChange := updateArrowsVisibility)(
                    self.state.politicalActions.map { politicalAction =>
                      <.div(^.className := PoliticalActionsListStyles.slideWrapper)(
                        <.div(^.className := PoliticalActionsListStyles.slide)(
                          <.PoliticalActionComponent(^.wrapped := PoliticalActionProps(politicalAction))()
                        )
                      )
                    }.toSeq
                  )
                )
              ),
              <.div(
                ^.className := js
                  .Array(TableLayoutBeyondMediumStyles.cellVerticalAlignMiddle, PoliticalActionsListStyles.slideshowNav)
              )(
                <.button(
                  ^.className := js.Array(FontAwesomeStyles.angleLeft, PoliticalActionsListStyles.slideshowArrow),
                  ^.ref := ((e: HTMLElement) => { previousButton = Some(e) }),
                  ^.disabled := true,
                  ^.onClick := previous
                )(),
                <.button(
                  ^.className := js.Array(FontAwesomeStyles.angleRight, PoliticalActionsListStyles.slideshowArrow),
                  ^.ref := ((e: HTMLElement) => { nextButton = Some(e) }),
                  ^.disabled := size < 2,
                  ^.onClick := next
                )()
              )
            )
          }
        ),
        <.style()(PoliticalActionsListStyles.render[String])
      )
    }
  )
}

object PoliticalActionsListStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`), overflow.hidden)

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
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(
        tableLayout.fixed,
        backgroundColor(ThemeStyles.BackgroundColor.white),
        boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
      )
    )

  val noPoliticalActionsWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val slideshowContentWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondMedium(width(100.%%)))

  val slideshowNav: StyleA =
    style(
      ThemeStyles.MediaQueries.belowMedium(display.none),
      ThemeStyles.MediaQueries
        .beyondMedium(whiteSpace.nowrap, width(100.pxToEm()))
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
        &.hover(opacity(1)),
        &.disabled(opacity(0).important)
      )
    )

  val slideWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries
        .belowMedium(padding(`0`, ThemeStyles.SpacingValue.small.pxToEm(), `0`, `0`)),
      ThemeStyles.MediaQueries
        .belowSmall(padding(`0`, ThemeStyles.SpacingValue.smaller.pxToEm(), `0`, `0`))
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
