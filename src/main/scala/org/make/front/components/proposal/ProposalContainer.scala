package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{Proposal => ProposalModel, TranslatedTheme => TranslatedThemeModel}
import org.make.services.proposal.ProposalService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ProposalContainer {

  case class ProposalAndThemeInfosModel(proposal: ProposalModel, themeName: Option[String], themeSlug: Option[String])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Proposal.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Proposal.ProposalProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {

        val futureProposalAndThemeInfos: Future[ProposalAndThemeInfosModel] = {

          val proposalSlug = props.`match`.params("proposalSlug")

          val proposalsResponse =
            ProposalService
              .searchProposals(slug = Some(proposalSlug), limit = Some(1), sort = Seq.empty, skip = None)
              .map { proposalsResponse =>
                if (proposalsResponse.results.nonEmpty) {
                  val proposal = proposalsResponse.results.head

                  val maybeTheme: Option[TranslatedThemeModel] =
                    proposal.themeId.flatMap(themeId => state.themes.find(_.id == themeId))

                  maybeTheme.map { theme =>
                    ProposalAndThemeInfosModel(
                      proposal = proposal,
                      themeName = Some(theme.title),
                      themeSlug = Some(theme.slug)
                    )
                  }.getOrElse(ProposalAndThemeInfosModel(proposal = proposal, themeName = None, themeSlug = None))

                } else {
                  ProposalAndThemeInfosModel(proposal = null, themeName = None, themeSlug = None)
                }
              }

          proposalsResponse.onComplete {
            case Success(_) => // let child handle new results
            case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
          }

          proposalsResponse
        }

        Proposal.ProposalProps(futureProposalAndThemeInfos = futureProposalAndThemeInfos)
      }
    }
}
