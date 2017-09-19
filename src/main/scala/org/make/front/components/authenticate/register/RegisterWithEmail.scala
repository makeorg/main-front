package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.client.BadRequestHttpException
import org.make.front.facades.I18n
import org.make.front.models.{User => UserModel}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.Future
import scala.util.{Failure, Success}

object RegisterWithEmail {

  case class RegisterState(fields: Map[String, String], errors: Map[String, String]) {
    def hasError(field: String): Boolean = {
      val maybeErrorMessage = errors.get(field)
      maybeErrorMessage.isEmpty || maybeErrorMessage.contains("")
    }
  }

  object RegisterState {
    val empty = RegisterState(Map(), Map())
  }

  case class RegisterProps(onSubmit: (RegisterState) => Future[UserModel])

  val reactClass: ReactClass =
    React.createClass[RegisterProps, RegisterState](getInitialState = { _ =>
      RegisterState(Map(), Map())
    }, render = { self =>
      def updateField(name: String): (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
        self.setState(
          state =>
            state.copy(fields = state.fields + (name -> event.target.value), errors = state.errors + (name -> ""))
        )
      }

      def onSubmit(): () => Boolean = {
        () =>
          self.props.wrapped.onSubmit(self.state).onComplete {
            case Success(_) => self.setState(RegisterState.empty)
            case Failure(e) =>
              e match {
                case exception: BadRequestHttpException if exception.getMessage.contains("already exist") =>
                  self.setState(
                    state => state.copy(errors = state.errors + ("email" -> I18n.t("form.register.errorAlreadyExist")))
                  )
                case _ =>
                  self.setState(
                    state =>
                      state.copy(errors = state.errors + ("email" -> I18n.t("form.register.errorRegistrationFailed")))
                  )
              }
          }
          false
      }

      <.form(^.onSubmit := onSubmit())(
        <.input(
          ^.`type`.email,
          ^.required := true,
          ^.placeholder := s"${I18n.t("form.fieldLabelEmail")} ${I18n.t("form.required")}",
          ^.onChange := updateField("email"),
          ^.value := self.state.fields.getOrElse("email", "")
        )("\\f003"),
        <.span()(self.state.errors.getOrElse("email", "")),
        <.input(
          ^.`type`.password,
          ^.required := true,
          ^.placeholder := s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
          ^.onChange := updateField("password"),
          ^.value := self.state.fields.getOrElse("password", "")
        )("\\f023"),
        <.span()(self.state.errors.getOrElse("password", "")),
        <.input(
          ^.`type`.text,
          ^.required := true,
          ^.placeholder := s"${I18n.t("form.fieldLabelFirstName")} ${I18n.t("form.required")}",
          ^.onChange := updateField("firstName"),
          ^.value := self.state.fields.getOrElse("firstName", "")
        )("\\f007"),
        <.span()(self.state.errors.getOrElse("firstName", ""))
      )
    })

}
