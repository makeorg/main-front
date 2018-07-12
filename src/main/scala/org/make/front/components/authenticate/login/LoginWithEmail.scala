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

package org.make.front.components.authenticate.login

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.client.UnauthorizedHttpException
import org.make.core.validation.NotBlankConstraint
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object LoginWithEmail {

  case class LoginWithEmailProps(note: String,
                                 trackingContext: TrackingContext,
                                 trackingParameters: Map[String, String],
                                 trackingInternalOnlyParameters: Map[String, String],
                                 connectUser: (String, String) => Future[_])
  case class LoginWithEmailState(email: String,
                                 emailErrorMessage: Option[String],
                                 password: String,
                                 passwordErrorMessage: Option[String])

  object LoginWithEmailState {
    val empty: LoginWithEmailState = LoginWithEmailState("", None, "", None)
  }

  val reactClass: ReactClass =
    React
      .createClass[LoginWithEmailProps, LoginWithEmailState](
        displayName = "LoginWithEmail",
        componentDidMount = { self =>
          TrackingService
            .track(
              eventName = "display-signin-form",
              trackingContext = self.props.wrapped.trackingContext,
              parameters = self.props.wrapped.trackingParameters,
              internalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters
            )
        },
        getInitialState = (_) => LoginWithEmailState.empty,
        render = { self =>
          val props = self.props.wrapped

          val updateEmail: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
            val newEmail = event.target.value
            self.setState(_.copy(email = newEmail))
          }

          val updatePassword: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
            val newPassword = event.target.value
            self.setState(_.copy(password = newPassword))
          }

          def validate(): Boolean = {
            val errorEmailMessages: Option[String] = NotBlankConstraint
              .validate(
                Some(self.state.email),
                Map("notBlank" -> unescape(I18n.t("authenticate.inputs.email.format-error-message")))
              )
              .map(_.message)
              .toList match {
              case head :: _ => Some(head)
              case Nil       => None
            }
            val errorPasswordMessages: Option[String] = NotBlankConstraint
              .validate(
                Some(self.state.password),
                Map("notBlank" -> unescape(I18n.t("authenticate.inputs.password.empty-field-error-message")))
              )
              .map(_.message)
              .toList match {
              case head :: _ => Some(head)
              case Nil       => None
            }

            self.setState(_.copy(emailErrorMessage = errorEmailMessages, passwordErrorMessage = errorPasswordMessages))

            errorEmailMessages.isEmpty && errorPasswordMessages.isEmpty
          }

          val handleSubmit: (FormSyntheticEvent[HTMLInputElement]) => Unit = {
            event =>
              event.preventDefault()

              if (validate()) {
                props.connectUser(self.state.email, self.state.password).onComplete {
                  case Success(_) =>
                    self.setState(LoginWithEmailState.empty)
                  case Failure(e) =>
                    e match {
                      case UnauthorizedHttpException =>
                        self
                          .setState(_.copy(emailErrorMessage = Some(unescape(I18n.t("authenticate.no-account-found")))))
                      case _ =>
                        self
                          .setState(_.copy(emailErrorMessage = Some(unescape(I18n.t("authenticate.error-message")))))
                    }

                }
              }
          }
          val loginWithEmailInputWrapperClasses = js
            .Array(
              InputStyles.wrapper.htmlClass,
              InputStyles.withIcon.htmlClass,
              LoginWithEmailStyles.emailInputWithIconWrapper.htmlClass,
              if (self.state.emailErrorMessage.isDefined) {
                InputStyles.withError.htmlClass
              }
            )
            .mkString(" ")

          val updatePasswordInputWrapperClasses = js
            .Array(
              InputStyles.wrapper.htmlClass,
              InputStyles.withIcon.htmlClass,
              LoginWithEmailStyles.passwordInputWithIconWrapper.htmlClass,
              if (self.state.passwordErrorMessage.isDefined) {
                InputStyles.withError.htmlClass
              }
            )
            .mkString(" ")

          <.form(^.onSubmit := handleSubmit, ^.novalidate := true)(
            <.label(^.className := loginWithEmailInputWrapperClasses)(
              <.input(
                ^.`type`.email,
                ^.required := true,
                ^.placeholder := I18n.t("authenticate.inputs.email.placeholder"),
                ^.onChange := updateEmail
              )()
            ),
            if (self.state.emailErrorMessage.isDefined) {
              <.p(^.className := InputStyles.errorMessage)(self.state.emailErrorMessage.get)
            },
            <.label(^.className := updatePasswordInputWrapperClasses)(
              <.input(
                ^.`type`.password,
                ^.required := true,
                ^.placeholder := I18n.t("authenticate.inputs.password.placeholder"),
                ^.onChange := updatePassword
              )()
            ),
            if (self.state.passwordErrorMessage.isDefined) {
              <.p(^.className := InputStyles.errorMessage)(self.state.passwordErrorMessage.get)
            },
            if (props.note != "") {
              <.p(^.className := js.Array(LoginWithEmailStyles.note, TextStyles.smallerText))(props.note)
            },
            <.div(^.className := LoginWithEmailStyles.submitButtonWrapper)(
              <.button(^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton), ^.`type`.submit)(
                <.i(^.className := js.Array(FontAwesomeStyles.thumbsUp))(),
                unescape("&nbsp;" + I18n.t("authenticate.login.send-cta"))
              )
            ),
            <.style()(LoginWithEmailStyles.render[String])
          )
        }
      )
}

object LoginWithEmailStyles extends StyleSheet.Inline {

  import dsl._

  val emailInputWithIconWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), &.before(content := "'\\f003'"))

  val passwordInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      &.before(content := "'\\f023'")
    )

  val submitButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

  val note: StyleA =
    style(
      margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      color(ThemeStyles.TextColor.lighter),
      unsafeChild("a")(color(ThemeStyles.ThemeColor.primary))
    )
}
