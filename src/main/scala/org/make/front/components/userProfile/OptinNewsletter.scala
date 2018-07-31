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

package org.make.front.components.userProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.front.facades.I18n
import org.scalajs.dom.raw.HTMLInputElement

object OptinNewsletter {
  final case class OptinNewsletterProps(optInNewsletter: Boolean,
                                        handleOnSubmit: (Self[OptinNewsletterProps, OptinNewsletterState])
                                          => (FormSyntheticEvent[HTMLInputElement]) => Unit)
  final case class OptinNewsletterState(isChecked: Boolean = false, message: String = "")

  val reactClass: ReactClass =
    React
      .createClass[OptinNewsletterProps, OptinNewsletterState](
        displayName = "OptinNewsletter",
        getInitialState = self => {
          OptinNewsletterState()
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(_.copy(isChecked = props.wrapped.optInNewsletter))
        },
        render = self => {
          <.div()(
            <.h2()(s"${I18n.t("user-profile.optin-newsletter.title")}"),
            <.div()(
              self.state.message
            ),
            <.form(^.onSubmit := self.props.wrapped.handleOnSubmit(self))(
              <.input(
                ^.`type`.checkbox,
                ^.checked := self.state.isChecked,
                ^.onChange := ((event: FormSyntheticEvent[HTMLInputElement]) => {
                  val checked = event.target.checked
                  self.setState(_.copy(isChecked = checked))

                })
              )(),
            s"${I18n.t("user-profile.optin-newsletter.description")}",
              <.button(
              )(s"${I18n.t("user-profile.optin-newsletter.cta")}")
            )
          )
        }
      )
}
