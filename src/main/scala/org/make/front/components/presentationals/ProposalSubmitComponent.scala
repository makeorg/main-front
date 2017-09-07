package org.make.front.components.presentationals

import java.util.regex.Pattern

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
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
import org.make.front.components.presentationals.ProposalSubmitComponent._
import org.make.front.facades.Localize.LocalizeVirtualDOMAttributes
import org.make.front.facades.ReactModal.{ReactModalVirtualDOMAttributes, ReactModalVirtualDOMElements}
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{GradientColor, Theme}
import org.make.front.styles.MakeStyles.Font
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}

import scala.util.matching.Regex
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object ProposalSubmitComponent {

  type Self = React.Self[ProposalSubmitProps, ProposalSubmitState]

  case class ProposalSubmitState(proposalContent: String = "",
                                 proposalPrefix: String,
                                 proposalPlaceHolder: String,
                                 maxLength: Int,
                                 minLength: Int,
                                 errorMessage: String = "",
                                 modalIsOpen: Boolean = false,
                                 theme: Theme,
                                 confirmationIsOpen: Boolean = false)
  case class ProposalSubmitProps(proposalPrefix: String,
                                 proposalPlaceHolder: String,
                                 maxLength: Int = 140,
                                 minLength: Int = 5,
                                 theme: Theme,
                                 handleProposalSubmit: (Self) => Unit)

  private var proposalElement: Option[HTMLElement] = None

  val setElement: HTMLElement => Unit = input => {
    proposalElement = Some(input)
  }

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ProposalSubmitProps, ProposalSubmitState](getInitialState = { self =>
          ProposalSubmitState(
            proposalPrefix = self.props.wrapped.proposalPrefix,
            proposalPlaceHolder = self.props.wrapped.proposalPlaceHolder,
            maxLength = self.props.wrapped.maxLength,
            theme = self.props.wrapped.theme,
            minLength = self.props.wrapped.minLength
          )
        }, render = (self) => {

          val history = self.props.history

          <.div()(
            ProposalFormElement(self),
            <.ReactModal(
              ^.contentLabel := "Submit proposal",
              ^.isOpen := self.state.modalIsOpen,
              ^.overlayClassName := ProposalSubmitComponentStyles.overlayModalStyle,
              ^.className := Seq(MakeStyles.Modal.modal, ProposalSubmitComponentStyles.modalStyle),
              ^.onRequestClose := closeModal(self),
              ^.shouldCloseOnOverlayClick := false
            )(
              <.a(
                ^.onClick := closeModal(self),
                ^.className := Seq(MakeStyles.Modal.close, ProposalSubmitComponentStyles.close)
              )(I18n.t("form.login.close")),
              if (self.state.confirmationIsOpen) {
                ProposalConfirmElement(self)
              } else {
                ProposalModalElement(self)
              }
            ),
            <.style()(ProposalSubmitComponentStyles.render[String])
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

  def isMobile: Boolean = {
    import org.scalajs.dom
    dom.window.hasOwnProperty("matchMedia") && dom.window.matchMedia("(max-width: 800px)").matches
  }

  def handleProposalClick(self: Self): (MouseSyntheticEvent) => Unit = (e: MouseSyntheticEvent) => {
    e.preventDefault()
    if (!self.state.modalIsOpen) {
      self.setState(_.copy(modalIsOpen = true))
    }
  }

  def handleProposalChange(self: Self): (FormSyntheticEvent[HTMLInputElement]) => Unit =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      e.preventDefault()
      handleProposalUpdate(self, e.target.value)
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

  def handleProposalSubmit(self: Self): (SyntheticEvent) => Any = (e: SyntheticEvent) => {
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
      self.props.wrapped.handleProposalSubmit(self)
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

  def proposalLength(self: Self, content: Option[String] = None): Int = {

    val proposalContent = content.getOrElse(self.state.proposalContent)
    val length = proposalContent.length
    if (length < 0) 0 else length
  }
}

object ProposalModalElement {

  def apply(self: Self[ProposalSubmitProps, ProposalSubmitState]): ReactElement = {
    val gradientColor: GradientColor = self.state.theme.gradient.getOrElse(GradientColor("#FFF", "#FFF"))
    val themeTitleStyle =
      ProposalSubmitComponentStyles.themeTitleStyle(gradientColor.from, gradientColor.to).htmlClass
    <.div(^.className := Seq(MakeStyles.Modal.content, ProposalSubmitComponentStyles.modalContent))(
      <.h2(^.className := ProposalSubmitComponentStyles.titleWrapper)(
        <.span(^.className := ProposalSubmitComponentStyles.titleIntro)(I18n.t("content.proposal.titleIntro")),
        <.span(^.className := themeTitleStyle)(self.state.theme.title)
      ),
      ProposalFormElement(self),
      <.p(^.className := ProposalSubmitComponentStyles.help)(I18n.t("content.proposal.help")),
      <.p(^.className := ProposalSubmitComponentStyles.subHelp)(I18n.t("content.proposal.subHelp")),
      <.button(
        ^.className := Seq(MakeStyles.Button.default, ProposalSubmitComponentStyles.submitButton),
        ^.onClick := handleProposalSubmit(self)
      )(
        <.i(^.className := Seq(ProposalSubmitComponentStyles.submitButtonIcon, FontAwesomeStyles.pencil))(),
        <.Translate(^.value := "form.proposal.submit")()
      )
    )
  }
}

object ProposalConfirmElement {
  def apply(self: Self[ProposalSubmitProps, ProposalSubmitState]): ReactElement = {

    def handleAnotherProposal(): (MouseSyntheticEvent) => Unit = (e: MouseSyntheticEvent) => {
      self.setState(_.copy(confirmationIsOpen = false))
    }

    <.div(^.className := Seq(MakeStyles.Modal.content, ProposalSubmitComponentStyles.modalContent))(
      <.div(^.className := ProposalSubmitComponentStyles.confirmationContainer)(
        <.h2(^.className := ProposalSubmitComponentStyles.confirmationTitle)(
          <.i(^.className := Seq(ProposalSubmitComponentStyles.confirmationIconTitle, FontAwesomeStyles.handPeaceO))(),
          <.Translate(^.value := "content.proposal.confirmationThanks")()
        ),
        <.p(^.className := ProposalSubmitComponentStyles.confirmationContent)(
          <.Translate(^.value := "content.proposal.confirmationContent", ^.dangerousHtml := true)()
        ),
        <.button(
          ^.className := Seq(MakeStyles.Button.default, ProposalSubmitComponentStyles.confirmationButtonBackTheme),
          ^.onClick := (() => { self.props.history.push(s"/theme/${self.props.wrapped.theme.slug}") })
        )(
          <.i(^.className := Seq(ProposalSubmitComponentStyles.submitButtonIcon, FontAwesomeStyles.handOLeft))(),
          <.Translate(
            ^.value := "content.proposal.confirmationButtonBackTheme",
            ^("theme") := (if (isMobile) "<br>" + self.props.wrapped.theme.title else self.props.wrapped.theme.title),
            ^.dangerousHtml := true
          )()
        ),
        <.button(
          ^.className := Seq(
            MakeStyles.Button.default,
            ProposalSubmitComponentStyles.confirmationButtonAnotherProposal
          ),
          ^.onClick := handleAnotherProposal
        )(
          <.i(
            ^.className := Seq(ProposalSubmitComponentStyles.submitButtonIcon, FontAwesomeStyles.lightbulbTransparent)
          )(),
          <.Translate(^.value := "content.proposal.confirmationButtonAnotherProposal")()
        )
      )
    )
  }
}

object ProposalSubmitCounterElement {
  case class ProposalSubmitCounterProps(max: Int, length: Int = 0, isMobileModal: Boolean)

  def apply(props: ProposalSubmitCounterProps): ReactElement = {

    val className =
      if (isMobile && props.isMobileModal) ProposalSubmitComponentStyles.counterMainMobileModal
      else ProposalSubmitComponentStyles.counterMain
    <.div(^.className := className)(
      <.span(^.className := getCounterClassName(props.length, props.max))(props.length),
      <.span()("/"),
      <.span()(props.max)
    )
  }

  private def getCounterClassName(length: Int, max: Int) = {
    if (length > max) ProposalSubmitComponentStyles.counterMaxError.htmlClass else ""
  }
}

object ProposalFormElement {
  def apply(self: Self[ProposalSubmitProps, ProposalSubmitState]): ReactElement = {
    <.div(^.className := ProposalSubmitComponentStyles.main)(
      <.i(
        ^.className := Seq(
          FontAwesomeStyles.lightbulbTransparent,
          MakeStyles.Form.inputIcon,
          if (isMobile && self.state.modalIsOpen) ProposalSubmitComponentStyles.iconFieldMobileModal
          else ProposalSubmitComponentStyles.iconField
        )
      )(),
      <.form(
        ^.className := ProposalSubmitComponentStyles.form(self.state.modalIsOpen),
        ^.onSubmit := handleProposalSubmit(self)
      )(
        if (isMobile && self.state.modalIsOpen) {
          <.textarea(
            ^.style := Map(
              "height" -> "8.2rem",
              "borderRadius" -> "1.5rem",
              "boxShadow" -> "0 2px 5px 0 rgba(0, 0, 0, 0.5)",
              "fontSize" -> "1.5rem",
              "paddingTop" -> "1rem",
              "paddingLeft" -> "3.4rem",
              "paddingRight" -> "6.5rem"
            ),
            ^.className := Seq(MakeStyles.Form.inputText, ProposalSubmitComponentStyles.input),
            ^.onChange := handleProposalChange(self),
            ^.onFocus := handleProposalFocus(self),
            ^.onBlur := handleProposalBlur(self),
            ^.value := self.state.proposalContent,
            ^.placeholder := self.state.proposalPlaceHolder
          )()
        } else {
          <.input(
            ^.style := (if (isMobile) Map("height" -> "3.5rem") else Map("height" -> "5rem")),
            ^.onClick := handleProposalClick(self),
            ^.className := Seq(MakeStyles.Form.inputText, ProposalSubmitComponentStyles.input),
            ^.onChange := handleProposalChange(self),
            ^.onFocus := handleProposalFocus(self),
            ^.onBlur := handleProposalBlur(self),
            ^.value := self.state.proposalContent,
            ^.placeholder := self.state.proposalPlaceHolder
          )()
        },
        <.div(
          ^.className := ProposalSubmitComponentStyles.toolTip,
          ^.style := (if (self.state.errorMessage.isEmpty) Map("visibility" -> "hidden")
                      else Map("visibility" -> "visible"))
        )(self.state.errorMessage),
        ProposalSubmitCounterElement(
          ProposalSubmitCounterElement
            .ProposalSubmitCounterProps(
              max = self.state.maxLength,
              length = proposalLength(self),
              isMobileModal = isMobile && self.state.modalIsOpen
            )
        )
      )
    )
  }
}

object ProposalSubmitComponentStyles extends StyleSheet.Inline {
  import dsl._

  def themeTitleStyle(from: String, to: String): StyleA =
    style(
      MakeStyles.gradientBackgroundImage("94deg", from, to),
      addClassName(MakeStyles.Modal.overlay.htmlClass),
      color.transparent,
      backgroundClip :=! "text",
      textShadow := "none",
      fontSize(4.8F.rem),
      lineHeight(4.8F.rem),
      Font.tradeGothicLTStd,
      BulmaStyles.ResponsiveHelpers.block,
      textAlign.center,
      textTransform.uppercase,
      position.relative,
      (media.all.maxWidth(800.px))(fontSize(3.rem), lineHeight(3.rem))
    )

  val titleWrapper: StyleA = style(paddingBottom(2.95F.rem), (media.all.maxWidth(800.px))(paddingBottom(2.rem)))

  val titleIntro: StyleA =
    style(
      MakeStyles.Font.playfairDisplayItalic,
      fontSize(1.8.rem),
      color(MakeStyles.Color.black),
      marginBottom(1.54F.rem),
      display.block,
      (media.all.maxWidth(800.px))(fontSize(1.5F.rem))
    )

  val modalContent: StyleA =
    style(border.`0`, margin.auto, width(100.%%), padding(0.rem, 0.5F.rem), (&.focus)(border.`0`))

  val modalContentSignin: StyleA =
    style(width(60.rem), (media.all.maxWidth(800.px))(width(31.rem)))

  val overlayModalStyle: StyleA =
    style(background := s"linear-gradient(121deg, #ffffff, #ececec)", addClassName(MakeStyles.Modal.overlay.htmlClass))

  val modalStyle: StyleA =
    style(
      flexBasis := 100.%%,
      margin.auto,
      padding(9.2F.rem, 10.rem),
      backgroundColor.transparent,
      outline.none,
      overflowY.auto,
      (media.all.maxWidth(800.px))(padding(8.8F.rem, 3.rem))
    )

  val main: StyleA = style(
    position.relative,
    display.flex,
    MakeStyles.Font.circularStdBook,
    fontSize(1.6F.rem),
    lineHeight(1.6F.rem),
    color(MakeStyles.Color.grey)
  )

  val counterMaxError: StyleA = style(color(MakeStyles.Color.pink))

  val inputShadow = "1px 1px 25px 0 rgba(0, 0, 0, .5), 1px 1px 1px 0 rgba(0, 0, 0, .5)"

  val form: (Boolean) => StyleA = styleF.bool(
    modalIsOpen =>
      if (modalIsOpen) {
        styleS(
          width(100.%%),
          display.flex,
          marginBottom(3.8F.rem),
          (media.all.maxWidth(800.px))(marginBottom(2.9F.rem))
        )
      } else {
        styleS(width(100.%%), display.flex, marginBottom.`0`, (media.all.maxWidth(800.px))(marginBottom(2.9F.rem)))
    }
  )

  val input: StyleA =
    style(
      MakeStyles.Font.circularStdBold,
      paddingLeft(6.3F.rem),
      paddingRight(10.rem),
      fontSize(1.8F.rem),
      width(100.%%),
      media.all.maxWidth(800.px)(height(3.rem)),
      borderWidth(0.1F.rem),
      borderStyle.solid,
      borderColor(MakeStyles.Color.softGrey),
      boxShadow := "0 1px 10px 0 rgba(0, 0, 0, 0.3), 0 1px 2px 0 #000000",
      (&.hover)(boxShadow := inputShadow),
      (&.focus)(boxShadow := inputShadow),
      (media.all.maxWidth(800.px))(fontSize(1.5F.rem), lineHeight(1.5F.rem))
    )

  val iconField: StyleA =
    style(
      unsafeRoot("i.fa.ProposalSubmitComponentStyles-iconField")(
        fontSize(3.rem),
        transform := "translate(2.8rem, 1rem)",
        (media.all.maxWidth(800.px))(fontSize(2.rem), lineHeight(2.rem), transform := "translate(2.8rem, 0.7rem)")
      )
    )
  val iconFieldMobileModal: StyleA =
    style(
      unsafeRoot("i.fa.ProposalSubmitComponentStyles-iconFieldMobileModal")(
        fontSize(1.7F.rem),
        transform := "translate(1.5rem, 1rem)"
      )
    )

  val counterMain: StyleA =
    style(
      alignSelf.center,
      position.absolute,
      right(3.rem),
      (media.all.maxWidth(800.px))(fontSize(1.3F.rem), lineHeight(1.3F.rem))
    )

  val counterMainMobileModal: StyleA =
    style(fontSize(1.3F.rem), alignSelf.flexEnd, right(1.6F.rem), marginBottom(0.8F.rem), marginLeft(-4.8F.rem))

  val close: StyleA =
    style(
      position.fixed,
      right(3.7F.rem),
      height(4.rem),
      width(4.rem),
      zIndex(10),
      (&.before)(width(4.rem)),
      (&.after)(width(4.rem)),
      (media.all.maxWidth(800.px))(
        right(0.rem),
        height(5.6F.rem),
        width(5.6F.rem),
        (&.after)(height(0.5F.rem), width(2.9F.rem)),
        (&.before)(height(0.5F.rem), width(2.9F.rem))
      )
    )

  val help: StyleA =
    style(
      MakeStyles.Font.circularStdBook,
      fontSize(1.8F.rem),
      color(MakeStyles.Color.black),
      letterSpacing(0.05F.rem),
      (media.all.maxWidth(800.px))(
        fontSize(1.5F.rem),
        lineHeight(1.5F.rem),
        color(MakeStyles.Color.grey),
        letterSpacing(0.02F.rem),
        marginBottom(1.6F.rem)
      )
    )

  val subHelp: StyleA =
    style(
      MakeStyles.Font.circularStdBook,
      fontSize(1.4F.rem),
      color(MakeStyles.Color.black),
      letterSpacing(0.05F.rem),
      (media.all.maxWidth(800.px))(
        fontSize(1.3F.rem),
        lineHeight(1.3F.rem),
        color(MakeStyles.Color.grey),
        letterSpacing(0.02F.rem)
      )
    )

  val submitButton: StyleA =
    style(
      width(17.5F.rem),
      marginTop(4.rem),
      marginBottom(1.5F.rem),
      (media.all.maxWidth(800.px))(width.auto, fontSize(1.3F.rem))
    )

  val submitButtonIcon: StyleA =
    style(paddingBottom(0.5F.rem), paddingRight(0.9.rem))

  val confirmationTitle: StyleA =
    style(
      MakeStyles.Font.tradeGothicLTStd,
      fontSize(4.6F.rem),
      lineHeight(4.6F.rem),
      color(MakeStyles.Color.black),
      textTransform.uppercase,
      marginBottom(2.7F.rem),
      (media.all.maxWidth(800.px))(fontSize(3.rem), marginBottom(1.7F.rem))
    )

  val confirmationContent: StyleA =
    style(
      MakeStyles.Font.circularStdBook,
      fontSize(1.8F.rem),
      lineHeight(2.3F.rem),
      color(MakeStyles.Color.black),
      marginBottom(3.7F.rem),
      (media.all.maxWidth(800.px))(fontSize(1.5F.rem), lineHeight(1.9F.rem), marginBottom(2.7F.rem))
    )

  val confirmationButtonBackTheme: StyleA = style(
    alignSelf.center,
    marginBottom(2.rem),
    textTransform.uppercase,
    unsafeRoot("button.make-button-default.ProposalSubmitComponentStyles-confirmationButtonBackTheme")(
      (media.all.maxWidth(800.px))(fontSize(1.5F.rem), marginBottom(2F.rem), height.auto)
    )
  )
  val confirmationButtonAnotherProposal: StyleA =
    style(alignSelf.center, textTransform.uppercase, (media.all.maxWidth(800.px))(fontSize(1.5F.rem)))
  val confirmationContainer: StyleA = style(display.flex, flexDirection.column)
  val confirmationIconTitle: StyleA = style(
    fontSize(4.6F.rem),
    transform := "translate(-0.9rem, -0.6rem)",
    (media.all.maxWidth(800.px))(fontSize(3.2F.rem), transform := "translate(-0.4rem, -0.2rem)")
  )
  val toolTip: StyleA = style(
    visibility.hidden,
    whiteSpace.nowrap,
    backgroundColor(MakeStyles.Color.black),
    color(MakeStyles.Color.white),
    MakeStyles.Font.circularStdBook,
    fontSize(1.4F.rem),
    lineHeight(1.4F.rem),
    position.absolute,
    top(-3.5F.rem),
    right.`0`,
    padding(0.6F.rem, 1.rem),
    (&.after)(
      content := "''",
      position.absolute,
      bottom(-2.2F.rem),
      right(4.rem),
      marginLeft((-1.4).rem),
      borderWidth(1.4.rem),
      borderStyle.solid,
      borderColor :=! "black transparent transparent transparent"
    )
  )
}
