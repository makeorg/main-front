package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.core.validation.PasswordConstraint
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.{I18n, Replacements}
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object PasswordResetComponent {

  case class PasswordResetProps(handleSubmit: (Self[PasswordResetProps, PasswordResetState])    => Unit,
                                checkResetToken: (Self[PasswordResetProps, PasswordResetState]) => Unit)
  case class PasswordResetState(password: String,
                                showPassword: Boolean,
                                errorMessage: String,
                                isValidResetToken: Boolean,
                                success: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[PasswordResetProps, PasswordResetState](
      displayName = getClass.getSimpleName,
      getInitialState = { _ =>
        PasswordResetState(
          password = "",
          showPassword = false,
          errorMessage = "",
          isValidResetToken = false,
          success = false
        )
      },
      componentWillMount = { self =>
        self.props.wrapped.checkResetToken(self)
      },
      render = self => {
        <.div(^.className := PasswordResetStyles.container)(
          <.div(^.className := Seq(PasswordResetStyles.content, BulmaStyles.Grid.Columns.columns))(
            if (self.state.success) {
              resetPasswordSuccessElement(self)
            } else if (self.state.isValidResetToken) {
              resetFormElement(self)
            } else {
              invalidResetTokenElement(self)
            }
          ),
          <.style()(PasswordResetStyles.render[String])
        )
      }
    )

  def resetFormElement(self: Self[PasswordResetProps, PasswordResetState]): ReactElement = {
    <.div(
      ^.className := Seq(
        BulmaStyles.Grid.Columns.column,
        BulmaStyles.Grid.Columns.is8,
        BulmaStyles.Grid.Offset.isOffset2
      )
    )(
      <.Translate(^.className := MakeStyles.Modal.title, ^.value := "form.passwordReset.title")(),
      <.div(^.className := PasswordResetStyles.terms)(I18n.t("form.passwordReset.description")),
      <.form(^.onSubmit := handleSubmit(self), ^.novalidate := true)(
        <.div(^.className := MakeStyles.Form.field)(
          <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.lock))(),
          <.i(
            ^.className := Seq(PasswordResetStyles.eye(self.state.showPassword), MakeStyles.Form.inputIconLeft),
            ^.onClick := toggleHidePassword(self)
          )(),
          <.input(
            ^.`type` := (if (self.state.showPassword) "text" else "password"),
            ^.required := true,
            ^.className := Seq(MakeStyles.Form.inputText, PasswordResetStyles.input),
            ^.placeholder := s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
            ^.onChange := handlePasswordChange(self),
            ^.value := self.state.password
          )()
        ),
        <.div()(<.span(^.className := PasswordRecoveryComponentStyles.errorMessage)(self.state.errorMessage)),
        <.button(
          ^.className := Seq(MakeStyles.Button.default, PasswordRecoveryComponentStyles.submitButton),
          ^.onClick := handleSubmit(self)
        )(
          <.i(^.className := Seq(FontAwesomeStyles.thumbsUp, PasswordRecoveryComponentStyles.buttonIcon))(),
          <.Translate(^.value := "form.passwordReset.validation")()
        )
      )
    )
  }

  def invalidResetTokenElement(self: Self[PasswordResetProps, PasswordResetState]): ReactElement = {
    <.div(
      ^.className := Seq(
        BulmaStyles.Grid.Columns.column,
        BulmaStyles.Grid.Columns.is8,
        BulmaStyles.Grid.Offset.isOffset2
      )
    )(<.Translate(^.className := MakeStyles.Modal.title, ^.value := "form.passwordReset.failed.title")())
  }

  def resetPasswordSuccessElement(self: Self[PasswordResetProps, PasswordResetState]): ReactElement = {
    <.div(
      ^.className := Seq(
        BulmaStyles.Grid.Columns.column,
        BulmaStyles.Grid.Columns.is8,
        BulmaStyles.Grid.Offset.isOffset2
      )
    )(
      <.Translate(^.className := MakeStyles.Modal.title, ^.value := "form.passwordReset.success.title")(),
      <.div(^.className := PasswordResetStyles.terms)(I18n.t("form.passwordReset.success.description"))
    )
  }

  private def handlePasswordChange(self: Self[PasswordResetProps, PasswordResetState]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newPassword = e.target.value
      self.setState(_.copy(password = newPassword))
    }

  private def toggleHidePassword(self: Self[PasswordResetProps, PasswordResetState]) = () => {
    val showPassword = !self.state.showPassword
    self.setState(self.state.copy(showPassword = showPassword))
  }

  private def handleSubmit(self: Self[PasswordResetProps, PasswordResetState]) = (e: SyntheticEvent) => {
    self.setState(self.state.copy(errorMessage = ""))
    e.preventDefault()

    val errors: Seq[String] = PasswordConstraint
      .validate(Some(self.state.password), Map("minMessage" -> "form.register.errorMinPassword"))
      .map(_.message)

    if (errors.isEmpty) {
      self.props.wrapped.handleSubmit(self)
    } else {
      self.setState(
        self.state.copy(errorMessage = I18n.t(errors.head, Replacements(("min", PasswordConstraint.min.toString))))
      )
    }
  }
}

object PasswordResetStyles extends StyleSheet.Inline {
  import dsl._

  val container: StyleA = style(paddingTop(7.rem), paddingBottom(7.rem))

  val terms: StyleA = style(marginBottom(0.8F.rem), fontSize(1.4.rem), color(rgba(0, 0, 0, 0.3)))

  val content: StyleA = style(
    width(100 %%),
    backgroundColor(MakeStyles.Color.white),
    maxWidth(114.rem),
    marginRight.auto,
    marginLeft.auto,
    textAlign.center,
    width(100.%%),
    height(100.%%),
    padding(2.rem, 10.rem),
    boxShadow := "0 1px 1px 0 rgba(0, 0, 0, 0.5)"
  )

  val input: StyleA = style(height(4.rem), width(100.%%), (media.all.maxWidth(800.px))(height(3.rem)))

  val eye: (Boolean) => StyleA = styleF.bool(
    typePassword =>
      if (typePassword) {
        styleS(
          (&.hover)(cursor.pointer),
          addClassName(FontAwesomeStyles.eyeSlash.htmlClass),
          color(MakeStyles.Color.grey)
        )
      } else {
        styleS((&.hover)(cursor.pointer), addClassName(FontAwesomeStyles.eye.htmlClass))
    }
  )
}
