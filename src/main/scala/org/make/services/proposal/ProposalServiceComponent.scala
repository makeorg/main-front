package org.make.services.proposal

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.client.DefaultMakeApiClientComponent
import org.make.core.{CirceClassFormatters, CirceFormatters}
import org.make.front.facades.I18n
import org.make.services.ApiService
import org.make.services.proposal.ProposalRequests.RegisterProposalRequest
import org.make.services.proposal.ProposalResponses.RegisterProposalResponse

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

    def postProposal(content: String): Future[RegisterProposalResponse] =
      client.post[RegisterProposalResponse](
        resourceName,
        data = RegisterProposalRequest(content).asJson.pretty(ApiService.printer)
      )

  }
}
