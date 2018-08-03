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

package org.make.front.components.userProfile.editingUserProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.styles.base.RWDHideRulesStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

object OptinNewsletter {
  final case class OptinNewsletterProps(
    optInNewsletter: Boolean,
    handleOnSubmit: (Self[OptinNewsletterProps, OptinNewsletterState]) => (FormSyntheticEvent[HTMLInputElement]) => Unit
  )
  final case class OptinNewsletterState(isChecked: Boolean = false, message: String = "", isEdited: Boolean)

  val reactClass: ReactClass =
    React
      .createClass[OptinNewsletterProps, OptinNewsletterState](
        displayName = "OptinNewsletter",
        getInitialState = self => {
          OptinNewsletterState(isChecked = self.props.wrapped.optInNewsletter, isEdited = false)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(_.copy(isChecked = props.wrapped.optInNewsletter, isEdited = false))
        },
        render = self => {

          def toggleCheckbox: () => Unit = { () =>
            self.setState(_.copy(!self.state.isChecked, message = "", isEdited = true))
          }

          def disableButtonState: () => Unit = { () =>
            self.setState(_.copy(isEdited = false))
          }

          <.div(^.className := EditingUserProfileStyles.wrapper)(
            <.div(^.className := EditingUserProfileStyles.sep)(),
            <.h2(^.className := EditingUserProfileStyles.title)(s"${I18n.t("user-profile.optin-newsletter.title")}"),
            <.form(^.onSubmit := self.props.wrapped.handleOnSubmit(self))(
              <.input(
                ^.`type`.checkbox,
                ^.checked := self.state.isChecked,
                ^.id := s"optinNewsletter",
                ^.value := s"newsletter",
                ^.className := RWDHideRulesStyles.hide
              )(),
              <.label(
                ^.className := EditingUserProfileStyles.customCheckboxLabel,
                ^.`for` := s"optinNewsletter",
                ^.onClick := toggleCheckbox
              )(<.span(^.className := EditingUserProfileStyles.customCheckboxIconWrapper)(if (self.state.isChecked) {
                <.i(^.className := js.Array(FontAwesomeStyles.check, EditingUserProfileStyles.customCheckedIcon))()
              }), <.span(^.className := EditingUserProfileStyles.label)(s"${I18n
                .t("user-profile.optin-newsletter.description")}")),
              <.div(^.className := js.Array(EditingUserProfileStyles.buttonGroup, EditingUserProfileStyles.success))(
                self.state.message
              ),
              <.div(^.className := EditingUserProfileStyles.buttonGroup)(
                <.button(
                  ^.className := js
                    .Array(
                      CTAStyles.basic,
                      CTAStyles.basicOnButton,
                      EditingUserProfileStyles.submitButton(self.state.isEdited)
                    ),
                  ^.`type` := s"submit",
                  ^.onClick := disableButtonState
                )(
                  <.i(^.className := js.Array(FontAwesomeStyles.save, EditingUserProfileStyles.submitButtonIcon))(),
                  <.span()(s"${I18n.t("user-profile.form.cta")}")
                )
              )
            ),
            <.style()(EditingUserProfileStyles.render[String])
          )
        }
      )
}
