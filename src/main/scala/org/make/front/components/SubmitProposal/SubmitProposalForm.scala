package org.make.front.components.presentationals

import java.util.regex.Pattern

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{
  FocusSyntheticEvent,
  FormSyntheticEvent,
  MouseSyntheticEvent,
  SyntheticEvent
}
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.core.validation.{ConstraintError, LengthConstraint, RegexConstraint}
import org.make.front.components.presentationals.SubmitProposalFormComponent._
import org.make.front.facades.Localize.LocalizeVirtualDOMAttributes
import org.make.front.facades.ReactModal.{ReactModalVirtualDOMAttributes, ReactModalVirtualDOMElements}
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{GradientColor, Theme}
import org.make.front.styles.FontAwesomeStyles
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}

object SubmitProposalFormComponent {

  type Self = React.Self[SubmitProposalFormProps, SubmitProposalFormState]

  case class SubmitProposalFormState(proposalContent: String = "",
                                     proposalPrefix: String,
                                     proposalPlaceHolder: String,
                                     maxLength: Int,
                                     minLength: Int,
                                     errorMessage: String = "",
                                     modalIsOpen: Boolean = false,
                                     theme: Theme,
                                     confirmationIsOpen: Boolean = false)

  case class SubmitProposalFormProps(proposalPrefix: String,
                                     proposalPlaceHolder: String,
                                     maxLength: Int = 140,
                                     minLength: Int = 5,
                                     theme: Theme,
                                     handleSubmitProposalForm: (Self) => Unit)

  private var proposalElement: Option[HTMLElement] = None

  val setElement: HTMLElement => Unit = input => {
    proposalElement = Some(input)
  }

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[SubmitProposalFormProps, SubmitProposalFormState](getInitialState = { self =>
          SubmitProposalFormState(
            proposalPrefix = self.props.wrapped.proposalPrefix,
            proposalPlaceHolder = self.props.wrapped.proposalPlaceHolder,
            maxLength = self.props.wrapped.maxLength,
            theme = self.props.wrapped.theme,
            minLength = self.props.wrapped.minLength
          )
        }, render = (self) => {

          val history = self.props.history

          <.div()(
            <.form(^.onSubmit := handleSubmitProposalForm(self))(
              <.label()(
                <.i(^.className := FontAwesomeStyles.lightbulbTransparent)(),
                <.textarea(
                  ^.onChange := handleProposalChange(self),
                  ^.onFocus := handleProposalFocus(self),
                  ^.onBlur := handleProposalBlur(self),
                  ^.value := self.state.proposalContent,
                  ^.placeholder := self.state.proposalPlaceHolder
                )(),
                <.span()(self.state.errorMessage),
                SubmitProposalFormCounterElement(
                  SubmitProposalFormCounterElement
                    .SubmitProposalFormCounterProps(max = self.state.maxLength, length = proposalLength(self))
                )
              )
            ),
            <.ReactModal(
              ^.contentLabel := "Submit proposal",
              ^.isOpen := self.state.modalIsOpen,
              ^.onRequestClose := closeModal(self),
              ^.shouldCloseOnOverlayClick := false
            )(if (self.state.confirmationIsOpen) {
              ProposalConfirmElement(self)
            } else {
              ProposalModalElement(self)
            })
          )
        })
    )

  private def closeModal(self: Self): () => Unit = () => {
    self.setState(_.copy(confirmationIsOpen = false, modalIsOpen = false))
  }

  private def toggleModal(self: Self): () => Unit = () => {
    self.setState(_.copy(modalIsOpen = !self.state.modalIsOpen))
  }

  def clearAll(self: Self): () => Unit = () => {
    self.setState(_.copy(modalIsOpen = false, proposalContent = ""))
  }

  def getPrefixValidator(prefix: String): RegexConstraint = {
    new RegexConstraint(pattern = s"^${Pattern.quote(prefix)}.*".r)
  }

  def handleProposalUpdate(self: Self, content: String): Unit = {
    val proposalContent = if (content.isEmpty) self.state.proposalPrefix else content

    var resultState = self.state.copy(errorMessage = "")

    if (getPrefixValidator(self.state.proposalPrefix).validate(Some(proposalContent)).isEmpty) {
      resultState = resultState.copy(proposalContent = proposalContent)
    }

    val constraintLengthErrors: Seq[ConstraintError] = new LengthConstraint(max = Some(self.state.maxLength))
      .validate(Some(proposalContent), Map("maxMessage" -> "content.proposal.isTooLong"))

    if (constraintLengthErrors.nonEmpty) {
      resultState = resultState.copy(
        errorMessage = I18n.t(constraintLengthErrors.head.message, Replacements(("max", self.state.maxLength.toString)))
      )
    }

    self.setState(resultState)
  }

  def handleSubmitProposalForm(self: Self): (SyntheticEvent) => Any = (e: SyntheticEvent) => {
    e.preventDefault()

    val lenghtValidator = new LengthConstraint(
      max = Some(self.state.maxLength),
      min = Some(self.state.minLength + self.state.proposalPrefix.length)
    )
    val proposalValidator = getPrefixValidator(self.state.proposalPrefix) & lenghtValidator

    val constraintErrors = proposalValidator.validate(
      Some(self.state.proposalContent),
      Map("minMessage" -> "content.proposal.isTooShort", "maxMessage" -> "content.proposal.isTooLong")
    )

    if (constraintErrors.isEmpty) {
      self.props.wrapped.handleSubmitProposalForm(self)
    } else {

      self.setState(
        _.copy(
          errorMessage = I18n.t(
            constraintErrors.head.message,
            Replacements(("min", self.state.minLength.toString), ("max", self.state.maxLength.toString))
          )
        )
      )
    }
  }

  def handleProposalChange(self: Self): (FormSyntheticEvent[HTMLInputElement]) => Unit =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      e.preventDefault()
      handleProposalUpdate(self, e.target.value)
    }

  def handleProposalClick(self: Self): (MouseSyntheticEvent) => Unit = (e: MouseSyntheticEvent) => {
    e.preventDefault()
    if (!self.state.modalIsOpen) {
      self.setState(_.copy(modalIsOpen = true))
    }
  }

  def handleProposalFocus(self: Self): (FocusSyntheticEvent) => Any = (e: FocusSyntheticEvent) => {
    if (self.state.proposalContent.isEmpty) {
      self.setState(_.copy(proposalContent = self.state.proposalPrefix))
    }
    if (!self.state.modalIsOpen) {
      toggleModal(self)
    }
  }

  def handleProposalBlur(self: Self): (FocusSyntheticEvent) => Unit = (e: FocusSyntheticEvent) => {
    if (self.state.proposalContent == self.state.proposalPrefix) {
      self.setState(_.copy(proposalContent = ""))
    }
  }

  def proposalLength(self: Self, content: Option[String] = None): Int = {
    val proposalContent = content.getOrElse(self.state.proposalContent)
    val length = proposalContent.length
    if (length < 0) 0 else length
  }

  object SubmitProposalFormCounterElement {

    case class SubmitProposalFormCounterProps(max: Int, length: Int = 0)

    def apply(props: SubmitProposalFormCounterProps): ReactElement = {
      <.span()(props.length, "/", props.max)
    }

  }

}

object ProposalModalElement {

  def apply(self: Self[SubmitProposalFormProps, SubmitProposalFormState]): ReactElement = {
    val gradientColor: GradientColor = self.state.theme.gradient.getOrElse(GradientColor("#FFF", "#FFF"))

    <.div()(
      <.h2()(<.span()(I18n.t("content.proposal.titleIntro")), <.span()(self.state.theme.title)),
      <.form(^.onSubmit := handleSubmitProposalForm(self))(
        <.label()(
          <.i(^.className := FontAwesomeStyles.lightbulbTransparent)(),
          <.textarea(
            ^.onChange := handleProposalChange(self),
            ^.onFocus := handleProposalFocus(self),
            ^.onBlur := handleProposalBlur(self),
            ^.value := self.state.proposalContent,
            ^.placeholder := self.state.proposalPlaceHolder
          )(),
          <.span()(self.state.errorMessage),
          SubmitProposalFormCounterElement(
            SubmitProposalFormCounterElement
              .SubmitProposalFormCounterProps(max = self.state.maxLength, length = proposalLength(self))
          )
        )
      ),
      <.p()(I18n.t("content.proposal.help")),
      <.p()(I18n.t("content.proposal.subHelp")),
      <.button(^.onClick := handleSubmitProposalForm(self))(
        <.i(^.className := FontAwesomeStyles.pencil)(),
        unescape(I18n.t("form.proposal.submit"))
      )
    )
  }
}

object ProposalConfirmElement {
  def apply(self: Self[SubmitProposalFormProps, SubmitProposalFormState]): ReactElement = {

    def handleAnotherProposal(): (MouseSyntheticEvent) => Unit = (e: MouseSyntheticEvent) => {
      self.setState(_.copy(confirmationIsOpen = false))
    }

    <.div()(
      <.h2()(
        <.i(^.className := FontAwesomeStyles.handPeaceO)(),
        unescape(I18n.t("content.proposal.confirmationThanks"))
      ),
      <.p()(<.Translate(^.value := "content.proposal.confirmationContent", ^.dangerousHtml := true)()),
      <.button(^.onClick := (() => {
        self.props.history.push(s"/theme/${self.props.wrapped.theme.slug}")
      }))(
        <.i(^.className := FontAwesomeStyles.handOLeft)(),
        unescape(
          I18n.t(
            "content.proposal.confirmationButtonAnotherProposal",
            Replacements(("theme", self.props.wrapped.theme.title))
          )
        )
      ),
      <.button(^.onClick := handleAnotherProposal)(
        <.i(^.className := FontAwesomeStyles.lightbulbTransparent)(),
        unescape(I18n.t("content.proposal.confirmationButtonAnotherProposal"))
      )
    )
  }
}
