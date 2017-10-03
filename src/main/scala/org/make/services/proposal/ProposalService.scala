package org.make.services.proposal

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.client.MakeApiClient
import org.make.core.URI._
import org.make.core.{CirceClassFormatters, CirceFormatters}
import org.make.front.facades.I18n
import org.make.front.models._
import org.make.services.ApiService
import org.make.services.proposal.ProposalResponses.{
  QualificationResponse,
  RegisterProposalResponse,
  SearchResponse,
  VoteResponse
}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ProposalService extends ApiService with CirceClassFormatters with CirceFormatters {

  override val resourceName: String = "proposals"

  val defaultResultsCount = 20

  def createProposal(content: String): Future[RegisterProposalResponse] =
    MakeApiClient
      .post[RegisterProposalResponse](
        resourceName,
        data = RegisterProposalRequest(content).asJson.pretty(ApiService.printer)
      )
      .map(_.get)

  def searchProposals(content: Option[String] = None,
                      themesIds: Seq[ThemeId] = Seq.empty,
                      operationsIds: Seq[OperationId] = Seq.empty,
                      tagsIds: Seq[TagId] = Seq.empty,
                      trending: Option[String] = None,
                      sort: Seq[SortOptionRequest],
                      limit: Option[Int],
                      skip: Option[Int]): Future[SearchResponse] =
    MakeApiClient
      .post[SearchResponse](
        resourceName / "search",
        data = SearchRequest(
          content = content,
          themesIds = if (themesIds.nonEmpty) Some(themesIds.map(_.value)) else None,
          operationsIds = if (operationsIds.nonEmpty) Some(operationsIds.map(_.value)) else None,
          tagsIds = if (tagsIds.nonEmpty) Some(tagsIds.map(_.value)) else None,
          limit = limit,
          skip = skip,
          sort = sort
        ).asJson.pretty(ApiService.printer)
      )
      .map(_.get)

  def vote(proposalId: ProposalId, voteValue: String): Future[VoteResponse] = {
    MakeApiClient
      .post[VoteResponse](
        apiEndpoint = resourceName / proposalId.value / "vote",
        data = VoteRequest(voteValue).asJson.pretty(ApiService.printer)
      )
      .map(_.get)
  }

  def unvote(proposalId: ProposalId, oldVoteValue: String): Future[VoteResponse] = {
    MakeApiClient
      .post[VoteResponse](
        apiEndpoint = resourceName / proposalId.value / "unvote",
        data = VoteRequest(oldVoteValue).asJson.pretty(ApiService.printer)
      )
      .map(_.get)
  }

  def qualifyVote(proposalId: ProposalId, vote: String, qualification: String): Future[QualificationResponse] = {
    MakeApiClient
      .post[QualificationResponse](
        apiEndpoint = resourceName / proposalId.value / "qualification",
        data = QualificationRequest(voteKey = vote, qualificationKey = qualification).asJson.pretty(ApiService.printer)
      )
      .map(_.get)
  }

  def removeVoteQualification(proposalId: ProposalId,
                              vote: String,
                              qualification: String): Future[QualificationResponse] = {
    MakeApiClient
      .post[QualificationResponse](
        apiEndpoint = resourceName / proposalId.value / "unqualification",
        data = QualificationRequest(voteKey = vote, qualificationKey = qualification).asJson.pretty(ApiService.printer)
      )
      .map(_.get)
  }

  final case class UnexpectedException(message: String = I18n.t("errors.unexpected")) extends Exception(message)

}
