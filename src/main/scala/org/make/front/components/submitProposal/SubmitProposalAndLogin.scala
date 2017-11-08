package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.submitProposal.ConfirmationOfProposalSubmission.ConfirmationOfProposalSubmissionProps
import org.make.front.components.submitProposal.SubmitProposalFormContainer.SubmitProposalFormContainerProps
import org.make.front.components.users.authenticate.RequireAuthenticatedUserContainer.RequireAuthenticatedUserContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  Location        => LocationModel,
  Operation       => OperationModel,
  TranslatedTheme => TranslatedThemeModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object SubmitProposalAndLogin {

  case class SubmitProposalAndLoginState(proposal: String,
                                         displayedComponent: String = "submit-proposal",
                                         errorMessage: Option[String])

  object SubmitProposalAndLoginState {
    val empty: SubmitProposalAndLoginState =
      SubmitProposalAndLoginState(proposal = "", errorMessage = None)
  }

  case class SubmitProposalAndLoginProps(intro: (ReactElement) => ReactElement,
                                         maybeTheme: Option[TranslatedThemeModel],
                                         maybeOperation: Option[OperationModel],
                                         onProposalProposed: ()           => Unit,
                                         propose: (String, LocationModel) => Future[_])

  val reactClass: ReactClass =
    WithRouter(
      React.createClass[SubmitProposalAndLoginProps, SubmitProposalAndLoginState](
        displayName = "SubmitProposalAndLogin",
        getInitialState = { _ =>
          SubmitProposalAndLoginState.empty
        },
        render = { self =>
          val props = self.props.wrapped

          val onConnectionOk: () => Unit = {
            () =>
              val location: LocationModel =
                if (self.props.location.pathname == "/") LocationModel.Homepage else LocationModel.ThemePage
              props.propose(self.state.proposal, location).onComplete {
                case Success(_) => self.setState(_.copy(displayedComponent = "result"))
                case Failure(_) =>
                  self.setState(
                    _.copy(
                      displayedComponent = "submit-proposal",
                      errorMessage = Some(unescape(I18n.t("submit-proposal.error")))
                    )
                  )
              }
          }

          val handleSubmitProposal: String => Unit = { proposal =>
            self.setState(_.copy(displayedComponent = "connect", proposal = proposal))
          }

          if (self.state.displayedComponent == "submit-proposal") {
            <.div(^.className := RowRulesStyles.centeredRow)(
              <.div(^.className := ColRulesStyles.col)(
                self.props.wrapped.intro(
                  <.SubmitProposalFormComponent(
                    ^.wrapped := SubmitProposalFormContainerProps(
                      maybeTheme = props.maybeTheme,
                      errorMessage = None,
                      handleSubmitProposalForm = handleSubmitProposal
                    )
                  )()
                )
              )
            )
          } else if (self.state.displayedComponent == "connect") {

            val intro: ReactElement =
              <.div()(
                <.div(^.className := RowRulesStyles.narrowerCenteredRow)(
                  <.div(^.className := ColRulesStyles.col)(
                    <.p(^.className := SubmitProposalAndLoginStyles.title)(
                      <.span(
                        ^.className := Seq(TextStyles.mediumText, TextStyles.intro, SubmitProposalAndLoginStyles.intro)
                      )("Nous avons besoin de quelques informations"),
                      <.br()(),
                      <.strong(^.className := TextStyles.bigTitle)("Pour valider votre proposition")
                    )
                  )
                ),
                <.div(^.className := RowRulesStyles.evenNarrowerCenteredRow)(
                  <.div(^.className := ColRulesStyles.col)(
                    <.hr(^.className := Seq(SubmitProposalAndLoginStyles.separatorLine))()
                  )
                )
              )

            <.div()(
              <.RequireAuthenticatedUserComponent(
                ^.wrapped := RequireAuthenticatedUserContainerProps(
                  operation = self.props.wrapped.maybeOperation.map(_.operationId),
                  intro = intro,
                  onceConnected = onConnectionOk,
                  defaultView = "register-expanded",
                  registerView = "register-expanded"
                )
              )(),
              <.style()(SubmitProposalAndLoginStyles.render[String])
            )

          } else if (self.state.displayedComponent == "result") {
            <.div(^.className := RowRulesStyles.centeredRow)(
              <.div(^.className := ColRulesStyles.col)(
                <.ConfirmationOfProposalSubmissionComponent(
                  ^.wrapped := ConfirmationOfProposalSubmissionProps(
                    maybeTheme = self.props.wrapped.maybeTheme,
                    onBack = self.props.wrapped.onProposalProposed,
                    onSubmitAnotherProposal = () => {
                      self.setState(_.copy(proposal = "", displayedComponent = "submit-proposal", errorMessage = None))
                    }
                  )
                )()
              )
            )
          } else {
            <.div.empty
          }
        }
      )
    )
}

object SubmitProposalAndLoginStyles extends StyleSheet.Inline {

  import dsl._

  val title: StyleA =
    style(textAlign.center)

  val intro: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(18)))
    )

  val separatorLine: StyleA =
    style(
      height(1.px),
      width(100.%%),
      margin(30.pxToEm(), `0`, 44.pxToEm()),
      border.none,
      backgroundColor(ThemeStyles.BorderColor.veryLight)
    )
}
