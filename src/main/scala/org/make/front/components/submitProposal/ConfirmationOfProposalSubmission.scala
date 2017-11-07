package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{FacebookPixel, I18n, Replacements}
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js.JSConverters._

object ConfirmationOfProposalSubmission {

  case class ConfirmationOfProposalSubmissionProps(maybeTheme: Option[TranslatedThemeModel],
                                                   onBack: ()                  => _,
                                                   onSubmitAnotherProposal: () => _)

  val reactClass: ReactClass =
    React
      .createClass[ConfirmationOfProposalSubmissionProps, Unit](
        displayName = "ConfirmationOfProposalSubmission",
        componentDidMount = { _ =>
          FacebookPixel.fbq("trackCustom", "display-proposal-submit-validation")
        },
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
            <.p(^.className := Seq(TextStyles.bigTitle, ConfirmationOfProposalSubmissionStyles.title))(
              <.i(^.className := FontAwesomeStyles.handPeaceO)(),
              unescape("&nbsp;"),
              <.span(^.dangerouslySetInnerHTML := I18n.t("submit-proposal.confirmation.title"))()
            ),
            <.p(
              ^.className := Seq(TextStyles.mediumText, ConfirmationOfProposalSubmissionStyles.message),
              ^.dangerouslySetInnerHTML := I18n.t("submit-proposal.confirmation.info")
            )(),
            <.button(
              ^.className := Seq(ConfirmationOfProposalSubmissionStyles.cta, CTAStyles.basic, CTAStyles.basicOnButton),
              ^.onClick := (() => {
                self.props.wrapped.onBack()
              })
            )(<.i(^.className := FontAwesomeStyles.handOLeft)(), unescape("&nbsp;"), self.props.wrapped.maybeTheme.map {
              theme =>
                <.span(
                  ^.dangerouslySetInnerHTML := I18n
                    .t("submit-proposal.confirmation.back-to-theme-cta", Replacements(("theme", theme.title)))
                )()
            }.getOrElse {
              <.span(^.dangerouslySetInnerHTML := I18n.t("submit-proposal.confirmation.back-cta"))()
            }),
            <.br()(),
            <.button(
              ^.className := Seq(ConfirmationOfProposalSubmissionStyles.cta, CTAStyles.basic, CTAStyles.basicOnButton),
              ^.onClick := handleClickOnButton
            )(
              <.i(^.className := FontAwesomeStyles.lightbulbTransparent)(),
              unescape("&nbsp;"),
              <.Translate(^.value := "submit-proposal.confirmation.new-proposal-cta", ^.dangerousHtml := true)()
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
      marginTop :=! s"${ThemeStyles.SpacingValue.small.pxToEm(13).value}",
      ThemeStyles.MediaQueries.beyondSmall(marginTop :=! s"${ThemeStyles.SpacingValue.medium.pxToEm(16).value}"),
      (&.firstChild)(marginTop(`0`))
    )
}
