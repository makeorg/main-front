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
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{PoliticalAction => PoliticalActionModel}
import org.make.front.styles._
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.front.Main.CssSettings._

import scala.scalajs.js

object PoliticalAction {

  case class PoliticalActionProps(politicalAction: PoliticalActionModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[PoliticalActionProps, Unit](
        displayName = "PoliticalAction",
        render = (self) => {
          val politicalAction = self.props.wrapped.politicalAction

          <.article(^.className := TableLayoutStyles.wrapper)(
            <.p(^.className := js.Array(TableLayoutStyles.cell, PoliticalActionStyles.imageWrapper))(
              <.img(
                ^.className := PoliticalActionStyles.image,
                ^.src := politicalAction.imageUrl,
                ^.alt := politicalAction.imageTitle.getOrElse(""),
                ^("data-pin-no-hover") := "true"
              )()
            ),
            <.div(^.className := js.Array(TableLayoutStyles.cell, PoliticalActionStyles.contentWrapper))(
              politicalAction.introduction.map { introduction =>
                <.p(^.className := js.Array(TextStyles.smallerText, PoliticalActionStyles.info))(unescape(introduction))
              },
              politicalAction.date.map { date =>
                <.p(^.className := js.Array(TextStyles.smallerText, PoliticalActionStyles.info))(
                  <.i(^.className := js.Array(PoliticalActionStyles.infoIcon, FontAwesomeStyles.calendarOpen))(),
                  date
                )
              },
              politicalAction.location.map { location =>
                <.p(^.className := js.Array(TextStyles.smallerText, PoliticalActionStyles.info))(
                  <.i(^.className := js.Array(PoliticalActionStyles.infoIcon, FontAwesomeStyles.mapMarker))(),
                  location
                )
              },
              <.p(^.className := js.Array(TextStyles.boldText, TextStyles.mediumText, PoliticalActionStyles.text))(
                unescape(politicalAction.text),
                <.br()(),
                politicalAction.links.map(
                  links => <.span(^.dangerouslySetInnerHTML := links, ^.className := PoliticalActionStyles.links)()
                )
              )
            ),
            <.style()(PoliticalActionStyles.render[String])
          )
        }
      )
}

object PoliticalActionStyles extends StyleSheet.Inline {
  import dsl._

  val imageWrapper: StyleA =
    style(
      width(70.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondMedium(verticalAlign.middle, width(120.pxToEm())),
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      textAlign.right
    )

  val contentWrapper: StyleA =
    style(
      padding(
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        `0`
      ),
      ThemeStyles.MediaQueries
        .beyondMedium(paddingLeft(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val image: StyleA = style(display.inline.important, maxHeight(ThemeStyles.SpacingValue.evenLarger.pxToEm()))

  val info: StyleA = style(
    display.inlineBlock,
    verticalAlign.top,
    color(ThemeStyles.TextColor.light),
    marginRight(ThemeStyles.SpacingValue.small.pxToEm())
  )

  val infoIcon: StyleA = style(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val text: StyleA = style(marginTop(5.pxToEm()))

  val seeMore: StyleA = style(color(ThemeStyles.ThemeColor.primary))
  val links: StyleA = style(unsafeChild("a") {
    color(ThemeStyles.ThemeColor.primary)
  })

}
