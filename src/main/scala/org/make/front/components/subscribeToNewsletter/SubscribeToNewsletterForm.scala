package org.make.front.components.subscribeToNewsletter

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.core.validation.{ConstraintError, EmailConstraint}
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object SubscribeToNewsletterForm {

  case class SubscribeToNewsletterFormProps(handleSubmit: (String) => Future[_])

  case class SubscribeToNewsletterFormState(email: String, errorMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[SubscribeToNewsletterFormProps, SubscribeToNewsletterFormState](
      displayName = "SubscribeToNewsletterForm",
      getInitialState = { _ =>
        SubscribeToNewsletterFormState(email = "", errorMessage = "")
      },
      render = { self =>
        val updateEmail =
          (e: FormSyntheticEvent[HTMLInputElement]) => {
            val newEmail = e.target.value
            self.setState(_.copy(email = newEmail))
          }

        val handleSubmit = (e: SyntheticEvent) => {
          e.preventDefault()
          val errors: Seq[ConstraintError] =
            EmailConstraint
              .validate(Some(self.state.email), Map("invalid" -> "subscribe-to-newsletter.invalid-email"))

          if (errors.isEmpty) {
            self.setState(self.state.copy(errorMessage = ""))
            self.props.wrapped.handleSubmit(self.state.email).onComplete {
              case Success(_) =>
              case Failure(_) =>
                self
                  .setState(
                    self.state.copy(errorMessage = unescape(I18n.t("subscribe-to-newsletter.error")))
                    /*TODO : specify error message from API*/
                  )
            }
          } else {
            self.setState(self.state.copy(errorMessage = unescape(I18n.t(errors.head.message))))
          }
        }

        val emailInputWrapperClasses = Seq(
          InputStyles.wrapper.htmlClass,
          InputStyles.withIcon.htmlClass,
          SubscribeToNewsletterFormStyles.emailInputWithIconWrapper.htmlClass,
          if (self.state.errorMessage != "") {
            InputStyles.withError.htmlClass
          }
        ).mkString(" ")

        <.div(^.className := Seq(RowRulesStyles.evenNarrowerCenteredRow))(
          <.div(^.className := ColRulesStyles.col)(
            <.form(^.onSubmit := handleSubmit, ^.novalidate := true)(
              <.label(^.className := emailInputWrapperClasses)(
                <.input(
                  ^.`type`.email,
                  ^.required := true,
                  ^.placeholder := I18n.t("subscribe-to-newsletter.email-input-placeholder"),
                  ^.onChange := updateEmail,
                  ^.value := self.state.email
                )()
              ),
              if (self.state.errorMessage != "") {
                <.p(
                  ^.className := InputStyles.errorMessage,
                  ^.dangerouslySetInnerHTML := unescape(self.state.errorMessage)
                )()
              },
              <.div(^.className := SubscribeToNewsletterFormStyles.submitButtonWrapper)(
                <.button(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton), ^.`type`.submit)(
                  <.i(^.className := FontAwesomeStyles.paperPlaneTransparent)(),
                  unescape("&nbsp;" + I18n.t("subscribe-to-newsletter.send-cta"))
                )
              )
            )
          ),
          <.style()(SubscribeToNewsletterFormStyles.render[String])
        )
      }
    )
}

object SubscribeToNewsletterFormStyles extends StyleSheet.Inline {
  import dsl._

  val emailInputWithIconWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), (&.before)(content := "'\\f003'"))

  val submitButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)
}
