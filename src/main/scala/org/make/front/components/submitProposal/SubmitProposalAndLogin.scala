package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalForm.SubmitProposalFormProps
import org.make.front.components.users.authenticate.RequireAuthenticatedUserContainer.RequireAuthenticatedUserContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Theme => ThemeModel}

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

  case class SubmitProposalAndLoginProps(bait: String,
                                         proposalContentMaxLength: Int,
                                         maybeTheme: Option[ThemeModel],
                                         onProposalProposed: () => Unit,
                                         propose: (String)      => Future[_])

  val reactClass: ReactClass =
    React.createClass[SubmitProposalAndLoginProps, SubmitProposalAndLoginState](
      getInitialState = { _ =>
        SubmitProposalAndLoginState.empty
      },
      componentWillReceiveProps = { (self, props) =>
        if (self.state.proposal == "") {
          self.setState(_.copy(proposal = props.wrapped.bait))
        }
      },
      render = { self =>
        val props = self.props.wrapped

        def goTo(destination: String): Unit = self.setState(_.copy(displayedComponent = destination))

        val onConnectionOk: () => Unit = { () =>
          goTo("submit-proposal")
          props.propose(self.state.proposal).onComplete {
            case Success(_) => props.onProposalProposed()
            case Failure(_) =>
              self.setState(_.copy(errorMessage = Some(unescape(I18n.t("form.proposal.errorSubmitFailed")))))
          }

        }

        val handleSubmitProposal: String => Unit = { proposal =>
          self.setState(_.copy(proposal = proposal))
          goTo("connect")
        }

        <.div()(if (self.state.displayedComponent == "submit-proposal") {
          <.SubmitProposalFormComponent(
            ^.wrapped := SubmitProposalFormProps(
              bait = props.bait,
              proposalContentMaxLength = props.proposalContentMaxLength,
              maybeTheme = props.maybeTheme,
              errorMessage = None,
              handleSubmitProposalForm = handleSubmitProposal
            )
          )()
        } else if (self.state.displayedComponent == "connect") {
          <.RequireAuthenticatedUserComponent(
            ^.wrapped := RequireAuthenticatedUserContainerProps(onceConnected = onConnectionOk)
          )()
        })
      }
    )

}
