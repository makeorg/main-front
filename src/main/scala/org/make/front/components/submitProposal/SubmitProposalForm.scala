package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.front.components.Components._
import org.make.front.facades.ReactTextareaAutosize.ReactTooltipVirtualDOMElements
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements, _}
import org.make.front.models.{Theme => ThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.{HTMLInputElement}

import scalajs.js.Dynamic.{global => g}

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object SubmitProposalForm {

  type SubmitProposalFormSelf = React.Self[SubmitProposalFormProps, SubmitProposalFormState]

  case class SubmitProposalFormState(proposalContent: String = "", errorMessage: Option[String] = None)

  case class SubmitProposalFormProps(bait: String,
                                     proposalContentMaxLength: Int,
                                     proposalContentMinLength: Int,
                                     maybeTheme: Option[ThemeModel],
                                     errorMessage: Option[String] = None,
                                     handleSubmitProposalForm: (String) => Unit)

  lazy val reactClass: ReactClass =
    React.createClass[SubmitProposalFormProps, SubmitProposalFormState](
      displayName = "SubmitProposalForm",
      getInitialState = { _ =>
        SubmitProposalFormState()
      },
      componentWillReceiveProps = { (self, props) =>
        self.setState(_.copy(errorMessage = props.wrapped.errorMessage, proposalContent = self.state.proposalContent))
      },
      render = { self =>
        val props = self.props.wrapped

        val handleProposalInputValueChanged: (FormSyntheticEvent[HTMLInputElement]) => Unit = { e =>
          var newProposalContent = e.target.value

          if (!newProposalContent.startsWith(self.props.wrapped.bait)) {
            self.setState(
              _.copy(
                proposalContent = self.props.wrapped.bait + newProposalContent
                  .substring(self.props.wrapped.bait.length + 1, newProposalContent.length)
              )
            )
          } else {
            self.setState(_.copy(proposalContent = newProposalContent))
          }
        }

        def handleProposalInputFocused() = () => {
          if (self.state.proposalContent.isEmpty) {
            self.setState(_.copy(proposalContent = self.props.wrapped.bait))
          }
        }

        val handleSubmit: (FormSyntheticEvent[HTMLInputElement]) => Boolean = {
          event =>
            event.preventDefault()
            val content = self.state.proposalContent
            if (content.length > props.proposalContentMaxLength) {
              self.setState(
                _.copy(
                  errorMessage = Some(
                    unescape(
                      I18n.t(
                        "form.proposal.errorProposalTooLong",
                        Replacements("max" -> self.props.wrapped.proposalContentMaxLength.toString)
                      )
                    )
                  )
                )
              )
            } else if (content.length < props.proposalContentMinLength) {
              self.setState(
                _.copy(
                  errorMessage = Some(
                    unescape(
                      I18n.t(
                        "form.proposal.errorProposalTooShort",
                        Replacements("min" -> self.props.wrapped.proposalContentMinLength.toString)
                      )
                    )
                  )
                )
              )
            } else {
              self.props.wrapped.handleSubmitProposalForm(content)
            }
            false
        }

        <.form(^.className := SubmitProposalFormStyles.form, ^.onSubmit := handleSubmit)(
          <.span(^.className := SubmitProposalFormStyles.proposalInputWithSubWrapper)(
            if (self.state.proposalContent.length >= self.props.wrapped.proposalContentMaxLength) {
              <.span(^.className := SubmitProposalFormStyles.textLimitReachedAlert)(
                <.span(
                  ^.className := TextStyles.smallerText,
                  ^.dangerouslySetInnerHTML := "Oups&nbsp;! Vous avez dépassé la limite de 140 caractères 😅 Essayez d’être plus concis.e.&nbsp;🙏"
                )()
              )
            },
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
                  <.TextareaAutosize(
                    ^.value := self.state.proposalContent,
                    ^.placeholder := "Il faut une proposition réaliste et respectueuse de tous",
                    ^.onFocus := handleProposalInputFocused,
                    ^.onChange := handleProposalInputValueChanged
                  )()
                ),
                <.span(^.className := SubmitProposalFormStyles.textLimitInfoWapper)(
                  <.span(^.className := Seq(TextStyles.smallText, SubmitProposalFormStyles.textLimitInfo))(
                    self.state.proposalContent.length,
                    "/",
                    props.proposalContentMaxLength
                  )
                )
              )
            )
          ),
          if (self.state.errorMessage.isDefined) {
            <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errorMessage.getOrElse("")))
          },
          <.div(^.className := SubmitProposalFormStyles.note)(
            <.p(^.className := TextStyles.mediumText)(unescape(I18n.t("content.proposal.help"))),
            <.p(^.className := TextStyles.smallText, ^.dangerouslySetInnerHTML := I18n.t("content.proposal.subHelp"))()
          ),
          <.button(
            ^.className := Seq(SubmitProposalFormStyles.submitButton, CTAStyles.basic, CTAStyles.basicOnButton),
            ^.`type`.submit
          )(
            <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.pencil))(),
            unescape("&nbsp;"),
            unescape(I18n.t("form.proposal.submit"))
          ),
          <.style()(SubmitProposalFormStyles.render[String])
        )
      }
    )
}

object SubmitProposalFormStyles extends StyleSheet.Inline {

  import dsl._

  val form: StyleA =
    style(textAlign.center)

  val note: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      unsafeChild("p + p")(marginTop(1.em)),
      unsafeChild("a")(textDecoration := "underline", color(ThemeStyles.TextColor.lighter)),
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

  val textLimitInfoWapper: StyleA = style(display.tableCell, verticalAlign.bottom)

  val textLimitInfo: StyleA =
    style(
      display.inlineBlock,
      lineHeight :=! s"${28.pxToEm(13).value}",
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(13)),
      ThemeStyles.MediaQueries
        .beyondSmall(lineHeight :=! s"${38.pxToEm(16).value}", paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(16))),
      ThemeStyles.MediaQueries.beyondMedium(lineHeight :=! s"${48.pxToEm(16).value}"),
      color(ThemeStyles.TextColor.lighter),
      whiteSpace.nowrap
    )

  val proposalInputWithSubWrapper: StyleA = style(position.relative, display.block)

  val textLimitReachedAlert: StyleA = style(
    position.absolute,
    top(100.%%),
    right(`0`),
    width(100.%%),
    padding := s"${3.pxToEm().value} ${10.pxToEm().value}",
    marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.BackgroundColor.black),
    ThemeStyles.MediaQueries
      .beyondSmall(
        top.auto,
        bottom(100.%%),
        width.auto,
        marginTop.auto,
        marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm())
      ),
    textAlign.center,
    (&.after)(
      content := "''",
      position.absolute,
      right(50.pxToEm()),
      bottom(100.%%),
      borderBottom :=! s"${5.pxToEm().value} solid ${ThemeStyles.BackgroundColor.black.value}",
      borderRight :=! s"${5.pxToEm().value} solid transparent",
      borderLeft :=! s"${5.pxToEm().value} solid transparent",
      ThemeStyles.MediaQueries.beyondSmall(
        bottom.auto,
        top(100.%%),
        borderBottom.none,
        borderTop :=! s"${5.pxToEm().value} solid ${ThemeStyles.BackgroundColor.black.value}"
      )
    )
  )

  val submitButton: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))
}
