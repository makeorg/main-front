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

package org.make.front.components.consultation.partners

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.TooltipStyles

import scala.scalajs.js

object PartnersItems {

  case class PartnersItemProps(partnerName: String, partnerAvatar: String, founder: Boolean)

  case class PartnersItemState(tooltipState: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[PartnersItemProps, PartnersItemState](
        displayName = "CulturePartnersItems",
        getInitialState = { self =>
          PartnersItemState(tooltipState = false)
        },
        render = self => {

          def tooltipOn() = (e: SyntheticEvent) => {
            e.preventDefault()
            self.setState(_.copy(tooltipState = true))
          }

          def tooltipOff() = (e: SyntheticEvent) => {
            e.preventDefault()
            self.setState(_.copy(tooltipState = false))
          }

          <.li(^.className := PartnersStyles.item, ^.onMouseOver := tooltipOn, ^.onMouseOut := tooltipOff)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := self.props.wrapped.partnerAvatar, ^.alt := self.props.wrapped.partnerName)()
            ),
            <.div(
              ^.className := js.Array(
                TooltipStyles.topPositioned,
                PartnersStyles.tooltip,
                TextStyles.smallerText,
                PartnersStyles.tooltipTrigger(self.state.tooltipState)
              )
            )(<.p()(self.props.wrapped.partnerName), if (self.props.wrapped.founder) {
              <.p()(<.p()(" - "), <.p()(unescape(I18n.t("operation.presentation.founder"))))
            }),
            <.style()(PartnersStyles.render[String])
          )

        }
      )

}
