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
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.facades.I18n
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.{window}

import scala.scalajs.js

object DeleteAccount {

  val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "OptinNewsletter",
        render = self => {

          def mailTo: () => Unit = { () =>
            window.location.assign("mailto:contact@make.org?subject=Suppression%20de%20mon%20compte%20Make.org")
          }

          <.div(^.className := EditingUserProfileStyles.wrapper)(
            <.div(^.className := EditingUserProfileStyles.sep)(),
            <.div(^.className := EditingUserProfileStyles.sep)(),
            <.h2(^.className := EditingUserProfileStyles.title)(s"${I18n.t("user-profile.delete-account.title")}"),
            <.p(^.className := js.Array(EditingUserProfileStyles.buttonGroup, EditingUserProfileStyles.label))(
              s"${I18n.t("user-profile.delete-account.description")}"
            ),
            <.div(^.className := EditingUserProfileStyles.buttonGroup)(
              <.button(
                ^.className := js
                  .Array(CTAStyles.basic, CTAStyles.basicOnButton, EditingUserProfileStyles.submitGreyButton),
                ^.onClick := mailTo
              )(
                <.i(^.className := js.Array(FontAwesomeStyles.trashAlt, EditingUserProfileStyles.submitButtonIcon))(),
                <.span()(s"${I18n.t("user-profile.delete-account.cta")}")
              )
            ),
            <.style()(EditingUserProfileStyles.render[String])
          )
        }
      )
}
