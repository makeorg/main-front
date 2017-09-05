package org.make.services.proposal

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.core.URI._
import org.make.client.DefaultMakeApiClientComponent
import org.make.core.{CirceClassFormatters, CirceFormatters}
import org.make.front.facades.I18n
import org.make.front.models.{Proposal, TagId, ThemeId}
import org.make.services.ApiService
import org.make.services.proposal.ProposalResponses.RegisterProposalResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class UnexpectedException(message: String = I18n.t("errors.unexpected")) extends Exception(message)

trait ProposalServiceComponent {
  def apiBaseUrl: String
  def proposalService: ProposalService = new ProposalService(apiBaseUrl)

  class ProposalService(override val apiBaseUrl: String)
      extends ApiService
      with CirceClassFormatters
      with CirceFormatters
      with DefaultMakeApiClientComponent {

    override val resourceName: String = "proposal"

    def createProposal(content: String): Future[RegisterProposalResponse] =
      client
        .post[RegisterProposalResponse](
          resourceName,
          data = RegisterProposalRequest(content).asJson.pretty(ApiService.printer)
        )
        .map(_.get)

    def searchProposals(content: Option[String] = None,
                        themesIds: Seq[ThemeId] = Seq.empty,
                        tagsIds: Seq[TagId] = Seq.empty,
                        options: Option[SearchOptionsRequest] = None): Future[Seq[Proposal]] =
      client
        .post[Seq[Proposal]](
          resourceName / "search",
          data = SearchRequest(
            content = content,
            themesIds = if (themesIds.nonEmpty) Some(themesIds.map(_.value)) else None,
            tagsIds = if (tagsIds.nonEmpty) Some(tagsIds.map(_.value)) else None,
            options = options
          ).asJson.pretty(ApiService.printer)
        )
        .map(_.get)

  }
}
