package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{OperationExpanded => OperationModel, TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object ConfirmationOfProposalSubmission {

  case class ConfirmationOfProposalSubmissionProps(trackingParameters: Map[String, String],
                                                   maybeTheme: Option[TranslatedThemeModel],
                                                   maybeOperation: Option[OperationModel],
                                                   onBack: ()                  => _,
                                                   onSubmitAnotherProposal: () => _)

  val reactClass: ReactClass =
    React
      .createClass[ConfirmationOfProposalSubmissionProps, Unit](
        displayName = "ConfirmationOfProposalSubmission",
        componentDidMount = { self =>
          TrackingService
            .track(
              "display-proposal-submit-validation",
              TrackingContext(TrackingLocation.submitProposalPage, self.props.wrapped.maybeOperation.map(_.slug)),
              self.props.wrapped.trackingParameters
            )
        },
        render = { self =>
          def handleClickOnButton() = () => {
            TrackingService.track(
              "click-proposal-submit-form-open",
              TrackingContext(TrackingLocation.endProposalPage, self.props.wrapped.maybeOperation.map(_.slug)),
              self.props.wrapped.trackingParameters
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
      marginTop(ThemeStyles.SpacingValue.small.pxToEm(13)),
      ThemeStyles.MediaQueries.beyondSmall(marginTop(ThemeStyles.SpacingValue.medium.pxToEm(16))),
      (&.firstChild)(marginTop(`0`))
    )
}
