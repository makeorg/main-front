/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.ReactTextareaAutosize.ReactTooltipVirtualDOMElements
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.{CTAStyles, InputStyles, TooltipStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

object SubmitProposalForm {

  type SubmitProposalFormSelf = React.Self[SubmitProposalFormProps, SubmitProposalFormState]

  case class SubmitProposalFormState(proposalContent: String = "", errorMessage: Option[String] = None)

  case class SubmitProposalFormProps(trackingContext: TrackingContext,
                                     trackingParameters: Map[String, String],
                                     trackingInternalOnlyParameters: Map[String, String],
                                     bait: String,
                                     proposalContentMaxLength: Int,
                                     proposalContentMinLength: Int,
                                     maybeTheme: Option[TranslatedThemeModel],
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
          val newProposalContent = e.target.value

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
                        "submit-proposal.form.error-message.limit-of-chars-exceeded",
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
                        "submit-proposal.form.error-message.not-enough-chars",
                        Replacements("min" -> self.props.wrapped.proposalContentMinLength.toString)
                      )
                    )
                  )
                )
              )
            } else {
              self.props.wrapped.handleSubmitProposalForm(content)
              TrackingService.track(
                eventName = "click-proposal-submit",
                trackingContext = TrackingContext(TrackingLocation.submitProposalPage, self.props.wrapped.trackingContext.operationSlug),
                parameters = self.props.wrapped.trackingParameters,
                internalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters
              )
            }
            false
        }

        <.form(^.className := SubmitProposalFormStyles.form, ^.onSubmit := handleSubmit)(
          <.span(^.className := SubmitProposalFormStyles.proposalInputWithSubWrapper)(
            if (self.state.proposalContent.length > self.props.wrapped.proposalContentMaxLength) {
              <.span(^.className := SubmitProposalFormStyles.textLimitReachedAlert)(
                <.span(
                  ^.className := TextStyles.smallerText,
                  ^.dangerouslySetInnerHTML := I18n.t("submit-proposal.form.limit-of-chars-reached-alert")
                )()
              )
            },
            <.label(
              ^.className := js.Array(
                InputStyles.wrapper,
                InputStyles.withIcon,
                InputStyles.biggerWithIcon,
                SubmitProposalFormStyles.proposalInputWithIconWrapper
              )
            )(
              <.span(^.className := TableLayoutStyles.wrapper)(
                <.span(^.className := js.Array(TableLayoutStyles.cell, SubmitProposalFormStyles.textareaWapper))(
                  <.TextareaAutosize(
                    ^.className := SubmitProposalFormStyles.textarea,
                    ^.value := self.state.proposalContent,
                    ^.placeholder := I18n.t("submit-proposal.form.proposal-input-placeholder"),
                    ^.onFocus := handleProposalInputFocused,
                    ^.onChange := handleProposalInputValueChanged
                  )()
                ),
                <.span(^.className := TableLayoutStyles.cellVerticalAlignBottom)(
                  <.span(^.className := js.Array(TextStyles.smallText, SubmitProposalFormStyles.textLimitInfo))(
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
            <.p(^.className := TextStyles.mediumText)(unescape(I18n.t("submit-proposal.form.info"))),
            <.p(
              ^.className := TextStyles.smallText,
              ^.dangerouslySetInnerHTML := I18n.t("submit-proposal.form.moderation-charter")
            )()
          ),
          <.button(
            ^.className := js.Array(SubmitProposalFormStyles.submitButton, CTAStyles.basic, CTAStyles.basicOnButton),
            ^.`type`.submit
          )(
            <.i(^.className := FontAwesomeStyles.pencil)(),
            unescape("&nbsp;" + I18n.t("submit-proposal.form.validate-cta"))
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
      &.before(content := "'\\F0EB'"),
      unsafeChild("textarea")(ThemeStyles.Font.circularStdBold)
    )

  val textareaWapper: StyleA =
    style(width(100.%%))

  val textarea: StyleA =
    style(overflow.hidden)

  val textLimitInfo: StyleA =
    style(
      display.inlineBlock,
      lineHeight(28.pxToEm(13)),
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(13)),
      ThemeStyles.MediaQueries
        .beyondSmall(lineHeight(38.pxToEm(16)), paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(16))),
      ThemeStyles.MediaQueries.beyondMedium(lineHeight(48.pxToEm(16))),
      color(ThemeStyles.TextColor.lighter),
      whiteSpace.nowrap
    )

  val proposalInputWithSubWrapper: StyleA = style(position.relative, display.block)

  val textLimitReachedAlert: StyleA = style(
    TooltipStyles.base,
    top(100.%%),
    right(`0`),
    transform := "none",
    marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
    ThemeStyles.MediaQueries
      .beyondSmall(
        top.auto,
        bottom(100.%%),
        width.auto,
        marginTop.auto,
        marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm())
      ),
    &.after(
      right(50.pxToEm()),
      transform := "none",
      bottom(100.%%),
      borderBottom(5.pxToEm(), solid, ThemeStyles.BackgroundColor.black),
      ThemeStyles.MediaQueries.beyondSmall(
        bottom.auto,
        top(100.%%),
        borderBottom.none,
        borderTop(5.pxToEm(), solid, ThemeStyles.BackgroundColor.black)
      )
    )
  )

  val submitButton: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))
}
