package org.make.front.components.authenticate.login

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.core.validation.NotBlankConstraint
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.styles.{CTAStyles, InputStyles, MakeStyles}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults.StyleA
import scalacss.internal.mutable.StyleSheet.Inline

object LoginWithEmail {

  case class LoginWithEmailProps(intro: String, connectUser: (String, String) => Future[_])
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
              .validate(Some(self.state.email), Map("notBlank" -> I18n.t("form.register.errorBlankEmail")))
              .map(_.message)
              .toList match {
              case head :: _ => Some(head)
              case Nil       => None
            }
            val errorPasswordMessages: Option[String] = NotBlankConstraint
              .validate(Some(self.state.password), Map("notBlank" -> I18n.t("form.register.errorBlankPassword")))
              .map(_.message)
              .toList match {
              case head :: _ => Some(head)
              case Nil       => None
            }

            self.setState(_.copy(emailErrorMessage = errorEmailMessages, passwordErrorMessage = errorPasswordMessages))

            errorEmailMessages.isEmpty && errorPasswordMessages.isEmpty
          }

          val onSubmit: () => Boolean = { () =>
            if (validate()) {
              props.connectUser(self.state.email, self.state.password).onComplete {
                case Success(_) => self.setState(LoginWithEmailState.empty)
                case Failure(e) =>
              }
            }
            false
          }

          <.form(^.onSubmit := onSubmit)(
            <.p()(props.intro),
            <.label(^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, LoginWithEmailStyles.emailPicto))(
              <.input(
                ^.`type`.text,
                ^.required := true,
                ^.placeholder := I18n.t("form.fieldLabelEmail"),
                ^.onChange := updateEmail
              )()
            ),
            <.span(^.className := LoginWithEmailStyles.error)(self.state.emailErrorMessage),
            <.label(^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, LoginWithEmailStyles.emailPicto))(
              <.input(
                ^.`type`.password,
                ^.required := true,
                ^.placeholder := I18n.t("form.fieldLabelPassword"),
                ^.onChange := updatePassword
              )()
            ),
            <.span(^.className := LoginWithEmailStyles.error)(self.state.passwordErrorMessage),
            <.button(^.className := Seq(CTAStyles.basicOnButton, CTAStyles.basic), ^.`type`.submit)(
              I18n.t("form.login.submitButton")
            ),
            <.style()(LoginWithEmailStyles.render[String])
          )
        }
      )

  object LoginWithEmailStyles extends Inline {
    import dsl._

    val emailPicto: StyleA = style((&.before)(content := "'\\f003'"))
    val passwordPicto: StyleA = style((&.before)(content := "'\\f023'"))

    val error: StyleA = style(MakeStyles.Form.errorMessage, unsafeChild("a") {
      color(MakeStyles.Color.error)
    })
  }
}
