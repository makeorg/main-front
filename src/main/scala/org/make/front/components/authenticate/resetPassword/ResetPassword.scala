package org.make.front.components.authenticate.resetPassword

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.core.validation.PasswordConstraint
import org.make.front.components.Components._
import org.make.front.components.authenticate.NewPasswordInput.NewPasswordInputProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object PasswordReset {

  case class PasswordResetProps(handleSubmit: (Self[PasswordResetProps, PasswordResetState])    => Unit,
                                checkResetToken: (Self[PasswordResetProps, PasswordResetState]) => Unit)

  case class PasswordResetState(password: String,
                                showPassword: Boolean,
                                errorMessage: String,
                                isValidResetToken: Boolean,
                                success: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[PasswordResetProps, PasswordResetState](
      displayName = "PasswordReset",
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
        <.div(^.className := ResetPasswordStyles.wrapper)(
          <.div(^.className := Seq(RowRulesStyles.centeredRow))(
            <.div(^.className := ColRulesStyles.col)(
              <.div(^.className := Seq(ResetPasswordStyles.contentWrapper))(if (self.state.success) {
                successMessage
              } else if (self.state.isValidResetToken) {
                resetPasswordForm(self)
              } else {
                invalidToken
              })
            )
          ),
          <.style()(ResetPasswordStyles.render[String])
        )
      }
    )

  def resetPasswordForm(self: Self[PasswordResetProps, PasswordResetState]): Seq[ReactElement] = {
    Seq(
      <.div(^.className := Seq(RowRulesStyles.centeredRow))(
        <.div(^.className := ColRulesStyles.col)(
          <.h1(^.className := Seq(ResetPasswordStyles.title, TextStyles.mediumTitle))(
            unescape(I18n.t("form.passwordReset.title"))
          )
        )
      ),
      <.div(^.className := ResetPasswordStyles.content)(
        <.div(^.className := Seq(RowRulesStyles.evenNarrowerCenteredRow))(
          <.div(^.className := ColRulesStyles.col)(
            <.p(^.className := Seq(ResetPasswordStyles.message, TextStyles.smallText))(
              unescape(I18n.t("form.passwordReset.description")),
              <.form(^.onSubmit := handleSubmit(self), ^.novalidate := true)(
                <.div(^.className := ResetPasswordStyles.newPasswordInputComponentWrapper)(
                  <.NewPasswordInputComponent(
                    ^.wrapped := NewPasswordInputProps(
                      value = self.state.password,
                      required = true,
                      placeHolder = s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
                      onChange = handlePasswordChange(self)
                    )
                  )()
                ),
                if (self.state.errorMessage.nonEmpty) {
                  <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errorMessage))
                },
                <.div(^.className := ResetPasswordStyles.submitButtonWrapper)(
                  <.button(
                    ^.className := Seq(CTAStyles.basicOnButton, CTAStyles.basic, CTAStyles.moreDiscreet),
                    ^.`type` := "submit"
                  )(
                    <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.thumbsUp))(),
                    unescape("&nbsp;"),
                    I18n.t("form.passwordReset.validation")
                  )
                )
              )
            )
          )
        )
      )
    )
  }

  val invalidToken: Seq[ReactElement] = {
    Seq(
      <.div(^.className := Seq(RowRulesStyles.evenNarrowerCenteredRow))(
        <.div(^.className := ColRulesStyles.col)(
          <.p(^.className := Seq(ResetPasswordStyles.message, TextStyles.smallText))(
            unescape(I18n.t("form.passwordReset.failed.title"))
          )
        )
      )
    )
  }

  val successMessage =
    Seq(
      <.div(^.className := Seq(RowRulesStyles.centeredRow))(
        <.div(^.className := ColRulesStyles.col)(
          <.h1(^.className := Seq(ResetPasswordStyles.title, TextStyles.mediumTitle))(
            unescape(I18n.t("form.passwordReset.success.title"))
          )
        )
      ),
      <.div(^.className := ResetPasswordStyles.content)(
        <.div(^.className := Seq(RowRulesStyles.evenNarrowerCenteredRow))(
          <.div(^.className := ColRulesStyles.col)(
            <.p(^.className := Seq(ResetPasswordStyles.message, TextStyles.smallText))(
              unescape(I18n.t("form.passwordReset.success.description"))
            )
          )
        )
      )
    )

  private def handlePasswordChange(self: Self[PasswordResetProps, PasswordResetState]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newPassword = e.target.value
      self.setState(_.copy(password = newPassword))
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
        self.state
          .copy(errorMessage = I18n.t(errors.head, Replacements(("min", PasswordConstraint.min.toString))))
      )
    }
  }
}

object ResetPasswordStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      paddingTop((ThemeStyles.SpacingValue.larger + 50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((ThemeStyles.SpacingValue.larger + 80).pxToEm())),
      paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
      minHeight(100.%%),
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )

  val contentWrapper: StyleA =
    style(
      minHeight(300.pxToEm()),
      paddingTop((ThemeStyles.SpacingValue.larger).pxToEm()),
      paddingBottom((ThemeStyles.SpacingValue.larger).pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val content: StyleA =
    style(marginTop((ThemeStyles.SpacingValue.medium).pxToEm()))

  val title: StyleA =
    style(textAlign.center)

  val message: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.light))

  val newPasswordInputComponentWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val submitButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

}
