package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.facades.{FacebookPixel, I18n, Replacements}
import org.make.front.models.{Theme => ThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js.JSConverters._
import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object ConfirmationOfProposalSubmission {

  case class ConfirmationOfProposalSubmissionProps(maybeTheme: Option[ThemeModel],
                                                   onBack: ()                  => _,
                                                   onSubmitAnotherProposal: () => _)

  val reactClass: ReactClass =
    React
      .createClass[ConfirmationOfProposalSubmissionProps, Unit](
        displayName = "ConfirmationOfProposalSubmission",
        render = { self =>
          def handleClickOnButton() = () => {
            FacebookPixel.fbq(
              "trackCustom",
              "click-proposal-submit-form-open",
              Map("location" -> "end-proposal-form").toJSDictionary
            )
            self.props.wrapped.onSubmitAnotherProposal()
          }

          <.article(^.className := ConfirmationOfProposalSubmissionStyles.wrapper)(
            <.h1(^.className := Seq(TextStyles.bigTitle, ConfirmationOfProposalSubmissionStyles.title))(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.handPeaceO))(),
              " ",
              <.span(^.dangerouslySetInnerHTML := I18n.t("content.proposal.confirmationThanks"))()
            ),
            <.p(
              ^.className := Seq(TextStyles.mediumText, ConfirmationOfProposalSubmissionStyles.message),
              ^.dangerouslySetInnerHTML := I18n.t("content.proposal.confirmationContent")
            )(),
            <.button(
              ^.className := Seq(ConfirmationOfProposalSubmissionStyles.cta, CTAStyles.basic, CTAStyles.basicOnButton),
              ^.onClick := (() => {
                self.props.wrapped.onBack()
              })
            )(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.handOLeft))(),
              " ",
              self.props.wrapped.maybeTheme.map { theme =>
                <.span(
                  ^.dangerouslySetInnerHTML := I18n
                    .t("content.proposal.confirmationButtonBackTheme", Replacements(("theme", theme.title)))
                )()
              }.getOrElse {
                <.span(^.dangerouslySetInnerHTML := I18n.t("content.proposal.confirmationButtonBack"))()
              }
            ),
            <.br()(),
            <.button(
              ^.className := Seq(ConfirmationOfProposalSubmissionStyles.cta, CTAStyles.basic, CTAStyles.basicOnButton),
              ^.onClick := handleClickOnButton
            )(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.lightbulbTransparent))(),
              " ",
              <.Translate(^.value := "content.proposal.confirmationButtonAnotherProposal", ^.dangerousHtml := true)()
            ),
            <.style()(ConfirmationOfProposalSubmissionStyles.render[String])
          )
        }
      )
}

object ConfirmationOfProposalSubmissionStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(textAlign.center)

  val title: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(20)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(34))),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(46)))
    )

  val message: StyleA =
    style(
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm(18)))
    )

  val cta: StyleA =
    style(
      marginTop := s"${ThemeStyles.SpacingValue.small.pxToEm(13).value}",
      ThemeStyles.MediaQueries.beyondSmall(marginTop := s"${ThemeStyles.SpacingValue.medium.pxToEm(16).value}"),
      (&.firstChild)(marginTop(`0`))
    )
}
