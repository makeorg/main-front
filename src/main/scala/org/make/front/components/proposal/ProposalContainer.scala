package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{Proposal => ProposalModel, Theme => ThemeModel, ThemeId => ThemeIdModel}
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
                val proposal = proposalsResponse.results.head

                val maybeTheme: Option[ThemeModel] =
                  proposal.themeId.flatMap(themeId => state.themes.find(_.id == themeId))

                maybeTheme.map { theme =>
                  val themeName: String = theme.title
                  val themeSlug: String = theme.slug

                  scalajs.js.Dynamic.global.console.log(themeName.toString())

                  ProposalAndThemeInfosModel(
                    proposal = proposal,
                    themeName = Some(themeName),
                    themeSlug = Some(themeSlug)
                  )

                }.getOrElse(ProposalAndThemeInfosModel(proposal = proposal, themeName = Some(""), themeSlug = Some("")))

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
