package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalFormContainer.SubmitProposalFormContainerProps
import org.make.front.components.submitProposal.SubmitProposalResult.SubmitProposalResultProps
import org.make.front.components.users.authenticate.RequireAuthenticatedUserContainer.RequireAuthenticatedUserContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Operation => OperationModel, Theme => ThemeModel}

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
                                         maybeTheme: Option[ThemeModel],
                                         maybeOperation: Option[OperationModel],
                                         onProposalProposed: () => Unit,
                                         propose: (String)      => Future[_])

  val reactClass: ReactClass =
    React.createClass[SubmitProposalAndLoginProps, SubmitProposalAndLoginState](
      displayName = "SubmitProposalAndLogin",
      getInitialState = { _ =>
        SubmitProposalAndLoginState.empty
      },
      render = { self =>
        val props = self.props.wrapped

        val onConnectionOk: () => Unit = { () =>
          props.propose(self.state.proposal).onComplete {
            case Success(_) => self.setState(_.copy(displayedComponent = "result"))
            case Failure(_) =>
              self.setState(
                _.copy(
                  displayedComponent = "submit-proposal",
                  errorMessage = Some(unescape(I18n.t("form.proposal.errorSubmitFailed")))
                )
              )
          }
        }

        val handleSubmitProposal: String => Unit = { proposal =>
          self.setState(_.copy(displayedComponent = "connect", proposal = proposal))
        }

        <.div()(if (self.state.displayedComponent == "submit-proposal") {
          self.props.wrapped.intro(
            <.SubmitProposalFormComponent(
              ^.wrapped := SubmitProposalFormContainerProps(
                maybeTheme = props.maybeTheme,
                errorMessage = None,
                handleSubmitProposalForm = handleSubmitProposal
              )
            )()
          )
        } else if (self.state.displayedComponent == "connect") {
          <.RequireAuthenticatedUserComponent(
            ^.wrapped := RequireAuthenticatedUserContainerProps(
              onceConnected = onConnectionOk,
              registerView = "register-expanded"
            )
          )()
        } else if (self.state.displayedComponent == "result") {
          <.SubmitProposalResultComponent(
            ^.wrapped := SubmitProposalResultProps(
              maybeTheme = self.props.wrapped.maybeTheme,
              onBack = self.props.wrapped.onProposalProposed,
              onSubmitAnotherProposal = () => {
                self.setState(_.copy(proposal = "", displayedComponent = "submit-proposal", errorMessage = None))
              }
            )
          )()
        })
      }
    )

}
