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
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults.StyleA

object LoginWithEmail {

  case class LoginWithEmailProps(note: String, connectUser: (String, String) => Future[_])
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
              .validate(Some(self.state.email), Map("notBlank" -> unescape(I18n.t("form.register.errorBlankEmail"))))
              .map(_.message)
              .toList match {
              case head :: _ => Some(head)
              case Nil       => None
            }
            val errorPasswordMessages: Option[String] = NotBlankConstraint
              .validate(
                Some(self.state.password),
                Map("notBlank" -> unescape(I18n.t("form.register.errorBlankPassword")))
              )
              .map(_.message)
              .toList match {
              case head :: _ => Some(head)
              case Nil       => None
            }

            self.setState(_.copy(emailErrorMessage = errorEmailMessages, passwordErrorMessage = errorPasswordMessages))

            errorEmailMessages.isEmpty && errorPasswordMessages.isEmpty
          }

          val handleSubmit: () => Boolean = {
            () =>
              if (validate()) {
                props.connectUser(self.state.email, self.state.password).onComplete {
                  case Success(_) => self.setState(LoginWithEmailState.empty)
                  case Failure(e) =>
                    e match {
                      case UnauthorizedHttpException =>
                        self.setState(
                          _.copy(emailErrorMessage = Some(unescape(I18n.t("form.login.errorAuthenticationFailed"))))
                        )
                      case _ =>
                        self
                          .setState(_.copy(emailErrorMessage = Some(unescape(I18n.t("form.login.errorSignInFailed")))))
                    }

                }
              }
              false
          }
          val loginWithEmailInputWrapperClasses = Seq(
            InputStyles.wrapper.htmlClass,
            InputStyles.withIcon.htmlClass,
            LoginWithEmailStyles.emailInputWithIconWrapper.htmlClass,
            if (self.state.emailErrorMessage.isDefined) {
              InputStyles.withError.htmlClass
            }
          ).mkString(" ")

          val updatePasswordInputWrapperClasses = Seq(
            InputStyles.wrapper.htmlClass,
            InputStyles.withIcon.htmlClass,
            LoginWithEmailStyles.passwordInputWithIconWrapper.htmlClass,
            if (self.state.passwordErrorMessage.isDefined) {
              InputStyles.withError.htmlClass
            }
          ).mkString(" ")

          <.form(^.onSubmit := handleSubmit)(
            <.label(^.className := loginWithEmailInputWrapperClasses)(
              <.input(
                ^.`type`.email,
                ^.required := true,
                ^.placeholder := I18n.t("form.fieldLabelEmail"),
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
                ^.placeholder := I18n.t("form.fieldLabelPassword"),
                ^.onChange := updatePassword
              )()
            ),
            if (self.state.passwordErrorMessage.isDefined) {
              <.p(^.className := InputStyles.errorMessage)(self.state.passwordErrorMessage.get)
            },
            if (props.note != "") {
              <.p(^.className := Seq(LoginWithEmailStyles.note, TextStyles.smallerText))(props.note)
            },
            <.div(^.className := LoginWithEmailStyles.submitButtonWrapper)(
              <.button(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton), ^.`type`.submit)(
                unescape(I18n.t("form.login.submitButton"))
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
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), (&.before)(content := "'\\f003'"))

  val passwordInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      (&.before)(content := "'\\f023'")
    )

  val submitButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

  val note: StyleA =
    style(
      margin :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      color(ThemeStyles.TextColor.lighter),
      unsafeChild("a")(color(ThemeStyles.ThemeColor.primary))
    )
}
