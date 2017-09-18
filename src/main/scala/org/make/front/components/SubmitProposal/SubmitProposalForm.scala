package org.make.front.components.SubmitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Theme, ThemeId}
import org.make.front.styles._
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.DevDefaults._
import scalacss.internal.{Length, StyleA}
import scalacss.internal.mutable.StyleSheet

object SubmitProposalFormComponent {

  type SubmitProposalFormSelf = React.Self[SubmitProposalFormProps, SubmitProposalFormState]

  case class SubmitProposalFormState(theme: Theme,
                                     proposalContent: String = "",
                                     bait: String = "",
                                     proposalContentMaxLength: Int,
                                     SubmissionError: Boolean = false)

  case class SubmitProposalFormProps(maybeTheme: Option[Theme], handleSubmitProposalForm: (SubmitProposalFormSelf) => _)

  lazy val reactClass: ReactClass =
    React.createClass[SubmitProposalFormProps, SubmitProposalFormState](
      displayName = getClass.toString,
      getInitialState = { self =>
        val theme: Theme = self.props.wrapped.maybeTheme.getOrElse(Theme(ThemeId("asdf"), "-", "", 0, 0, "#FFF"))
        SubmitProposalFormState(
          theme = theme,
          bait = "Il faut ",
          proposalContent = "Il faut ",
          proposalContentMaxLength = 140
        )

      },
      render = (self) => {

        <.form(^.className := SubmitProposalFormStyles.form, ^.onSubmit := handleSubmitFormChange(self))(
          <.label(
            ^.className := Seq(
              InputStyles.wrapper,
              InputStyles.withIcon,
              InputStyles.biggerWithIcon,
              SubmitProposalFormStyles.proposalInputWithIconWrapper
            )
          )(
            <.span(^.className := SubmitProposalFormStyles.innerWapper)(
              <.span(^.className := SubmitProposalFormStyles.textareaWapper)(
                <.textarea(
                  ^.value := self.state.proposalContent, /*textarea autosize*/
                  ^("autoFocus") := true,
                  ^.onChange := handleProposalContentChange(self)
                )()
              ),
              <.span(^.className := SubmitProposalFormStyles.textLimitInfoWapper)(
                <.span(^.className := Seq(TextStyles.smallText, SubmitProposalFormStyles.textLimitInfo))(
                  self.state.proposalContent.length,
                  "/",
                  self.state.proposalContentMaxLength
                )
              )
            )
          ),
          if (self.state.SubmissionError) {
            <.p(^.className := InputStyles.errorMessage)(unescape(I18n.t("form.proposal.errorSubmitFailed")))
          },
          <.div(^.className := SubmitProposalFormStyles.notice)(
            <.p()(unescape(I18n.t("content.proposal.help"))),
            <.p(^.className := TextStyles.smallText)(unescape(I18n.t("content.proposal.subHelp")))
          ),
          <.button(
            ^.className := Seq(SubmitProposalFormStyles.submitButton, CTAStyles.basic, CTAStyles.basicOnButton),
            ^.`type`.submit
          )(
            <.i(^.className := FontAwesomeStyles.pencil)(),
            unescape("&nbsp;"),
            unescape(I18n.t("form.proposal.submit"))
          ),
          <.style()(SubmitProposalFormStyles.render[String])
        )
      }
    )

  private def handleProposalContentChange(self: SubmitProposalFormSelf) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newProposalContent = e.target.value

      if (newProposalContent.length <= self.state.bait.length) {
        self.setState(_.copy(proposalContent = self.state.bait))
      } else if (newProposalContent.length <= self.state.proposalContentMaxLength) {
        self.setState(_.copy(proposalContent = newProposalContent))
      }

    }

  private def handleSubmitFormChange(self: SubmitProposalFormSelf) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      self.props.wrapped.handleSubmitProposalForm(self)
    }
}

object SubmitProposalFormStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val form: StyleA =
    style(textAlign.center)

  val notice: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      unsafeChild("p + p")(marginTop(1.em)),
      unsafeChild("a")(textDecoration := "underline"),
      color(ThemeStyles.TextColor.lighter),
      lineHeight(1.3)
    )

  val proposalInputWithIconWrapper: StyleA =
    style(
      boxShadow := "0 2px 5px 0 rgba(0,0,0,0.50)",
      (&.before)(content := "'\\F0EB'"),
      unsafeChild("textarea")(ThemeStyles.Font.circularStdBold)
    )

  val innerWapper: StyleA = style(display.table, width(100.%%))

  val textareaWapper: StyleA =
    style(display.tableCell, width(100.%%))

  val textLimitInfoWapper: StyleA = style(display.tableCell, verticalAlign.middle)

  val textLimitInfo: StyleA =
    style(padding(1.em), lineHeight.initial, color(ThemeStyles.TextColor.lighter), whiteSpace.nowrap)

  val submitButton: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))
}
