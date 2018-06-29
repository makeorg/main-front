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

package org.make.front.components.authenticate.recoverPassword

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.core.validation.{ConstraintError, EmailConstraint}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object RecoverPassword {

  case class RecoverPasswordProps(handleSubmit: (String) => Future[_])

  case class RecoverPasswordState(email: String, errorMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[RecoverPasswordProps, RecoverPasswordState](displayName = "RecoverPassword", getInitialState = {
      _ =>
        RecoverPasswordState(email = "", errorMessage = "")
    }, render = {
      self =>
        val updateEmail =
          (e: FormSyntheticEvent[HTMLInputElement]) => {
            val newEmail = e.target.value
            self.setState(_.copy(email = newEmail))
          }

        val handleSubmit = (e: SyntheticEvent) => {
          e.preventDefault()
          val errors: js.Array[ConstraintError] =
            EmailConstraint
              .validate(Some(self.state.email), Map("invalid" -> "authenticate.inputs.email.format-error-message"))

          if (errors.isEmpty) {
            self.setState(self.state.copy(errorMessage = ""))
            self.props.wrapped.handleSubmit(self.state.email).onComplete {
              case Success(_) =>
              case Failure(_) =>
                self
                  .setState(
                    self.state
                      .copy(
                        errorMessage = unescape(I18n.t("authenticate.recover-password.error-message.mail-not-found"))
                      )
                  )
              /*TODO : specify error message from API*/
            }
          } else {
            self.setState(self.state.copy(errorMessage = unescape(I18n.t(errors.head.message))))
          }
        }

        val emailInputWrapperClasses = js
          .Array(
            InputStyles.wrapper.htmlClass,
            InputStyles.withIcon.htmlClass,
            RecoverPasswordStyles.emailInputWithIconWrapper.htmlClass,
            if (self.state.errorMessage != "") {
              InputStyles.withError.htmlClass
            }
          )
          .mkString(" ")

        <.div()(
          <.div(^.className := RecoverPasswordStyles.introWrapper)(
            <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("authenticate.recover-password.title")))
          ),
          <.p(^.className := js.Array(RecoverPasswordStyles.text, TextStyles.smallText))(
            unescape(I18n.t("authenticate.recover-password.info"))
          ),
          <.form(^.onSubmit := handleSubmit, ^.novalidate := true)(
            <.label(^.className := emailInputWrapperClasses)(
              <.input(
                ^.`type`.email,
                ^.required := true,
                ^.placeholder := I18n.t("authenticate.inputs.email.placeholder"),
                ^.onChange := updateEmail,
                ^.value := self.state.email
              )()
            ),
            if (self.state.errorMessage != "") {
              <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errorMessage))
            },
            <.div(^.className := RecoverPasswordStyles.submitButtonWrapper)(
              <.button(^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton), ^.`type`.submit)(
                <.i(^.className := js.Array(FontAwesomeStyles.paperPlaneTransparent))(),
                unescape("&nbsp;" + I18n.t("authenticate.recover-password.send-cta"))
              )
            )
          ),
          <.style()(RecoverPasswordStyles.render[String])
        )
    })
}

object RecoverPasswordStyles extends StyleSheet.Inline {
  import dsl._

  val introWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

  val text: StyleA =
    style(textAlign.center, margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`), color(ThemeStyles.TextColor.lighter))

  val emailInputWithIconWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), &.before(content := "'\\f003'"))

  val submitButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)
}
