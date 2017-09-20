package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.client.BadRequestHttpException
import org.make.front.components.Components._
import org.make.front.components.ViewablePassword.ViewablePasswordProps
import org.make.front.facades.I18n
import org.make.front.styles.{CTAStyles, InputStyles}
import org.scalajs.dom.raw.HTMLInputElement

import scala.util.{Failure, Success}
import scalacss.DevDefaults.StyleA
import scalacss.internal.mutable.StyleSheet.Inline

import scala.concurrent.ExecutionContext.Implicits.global
import org.make.front.Main.CssSettings._

object RegisterWithEmailExpanded {

  val reactClass: ReactClass = React.createClass[RegisterProps, RegisterState](getInitialState = { _ =>
    RegisterState(Map(), Map())
  }, render = { self =>
    def updateField(name: String): (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
      val inputValue = event.target.value
      self.setState(
        state => state.copy(fields = state.fields + (name -> inputValue), errors = state.errors + (name -> ""))
      )
    }

    def onSubmit: () => Boolean = {
      () =>
        self.props.wrapped.register(self.state).onComplete {
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

    val agesChoices: Seq[Int] = Range(13, 100)

    <.form(^.onSubmit := onSubmit)(
      <.p()(self.props.wrapped.intro),
      <.label(
        ^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, RegisterWithEmailExpandedStyles.emailPicto)
      )(
        <.input(
          ^.`type`.email,
          ^.required := true,
          ^.placeholder := s"${I18n.t("form.fieldLabelEmail")} ${I18n.t("form.required")}",
          ^.onChange := updateField("email"),
          ^.value := self.state.fields.getOrElse("email", "")
        )()
      ),
      <.span()(self.state.errors.getOrElse("email", "")),
      <.label(
        ^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, RegisterWithEmailExpandedStyles.passwordPicto)
      )(
        <.ViewablePasswordComponent(
          ^.wrapped := ViewablePasswordProps(
            value = self.state.fields.getOrElse("password", ""),
            required = true,
            placeHolder = s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
            onChange = updateField("password")
          )
        )()
      ),
      <.span()(self.state.errors.getOrElse("password", "")),
      <.label(
        ^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, RegisterWithEmailExpandedStyles.firstNamePicto)
      )(
        <.input(
          ^.`type`.text,
          ^.required := true,
          ^.placeholder := s"${I18n.t("form.fieldLabelFirstName")} ${I18n.t("form.required")}",
          ^.onChange := updateField("firstName"),
          ^.value := self.state.fields.getOrElse("firstName", "")
        )()
      ),
      <.span()(self.state.errors.getOrElse("firstName", "")),
      <.label(^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, RegisterWithEmailExpandedStyles.agePicto))(
        <.select(
          ^.`type`.text,
          ^.required := false,
          ^.placeholder := s"${I18n.t("form.fieldLabelAge")}",
          ^.onChange := updateField("age"),
          ^.value := self.state.fields.getOrElse("age", "")
        )(
          <.option(^.value := "")(s"${I18n.t("form.fieldLabelAge")}"),
          agesChoices.map(age => <.option(^.value := age)(age))
        )
      ),
      <.span()(self.state.errors.getOrElse("age", "")),
      <.label(
        ^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, RegisterWithEmailExpandedStyles.postalCodePicto)
      )(
        <.input(
          ^.`type`.text,
          ^.required := false,
          ^.placeholder := s"${I18n.t("form.fieldPostalCode")}",
          ^.onChange := updateField("postalCode"),
          ^.value := self.state.fields.getOrElse("postalCode", "")
        )()
      ),
      <.span()(self.state.errors.getOrElse("postalCode", "")),
      <.label(
        ^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, RegisterWithEmailExpandedStyles.professionPicto)
      )(
        <.input(
          ^.`type`.text,
          ^.required := false,
          ^.placeholder := s"${I18n.t("form.fieldProfession")}",
          ^.onChange := updateField("profession"),
          ^.value := self.state.fields.getOrElse("profession", "")
        )()
      ),
      <.span()(self.state.errors.getOrElse("profession", "")),
      <.p()(self.props.wrapped.note),
      <.button(^.`type` := "submit", ^.className := Seq(CTAStyles.basic))(I18n.t("form.register.subscribe")),
      <.style()(RegisterWithEmailExpandedStyles.render[String])
    )

  })

  object RegisterWithEmailExpandedStyles extends Inline {
    import dsl._

    val emailPicto: StyleA = style((&.before)(content := "'\\f003'"))
    val passwordPicto: StyleA = style((&.before)(content := "'\\f023'"))
    val firstNamePicto: StyleA = style((&.before)(content := "'\\f007'"))
    val agePicto: StyleA = style((&.before)(content := "'\\f1ae'"))
    val postalCodePicto: StyleA = style((&.before)(content := "'\\f041'"))
    val professionPicto: StyleA = style((&.before)(content := "'\\f0f2'"))
  }

}
