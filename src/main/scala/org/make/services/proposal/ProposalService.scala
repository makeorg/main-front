package org.make.services.proposal

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.client.MakeApiClient
import org.make.core.URI._
import org.make.core.{CirceClassFormatters, CirceFormatters}
import org.make.front.facades.I18n
import org.make.front.models._
import org.make.services.ApiService
import org.make.services.proposal.{QualificationResponse, VoteResponse, RegisterProposalResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ProposalService extends ApiService with CirceClassFormatters with CirceFormatters {

  override val resourceName: String = "proposals"

  val defaultResultsCount = 20

  def getProposalById(proposalId: ProposalId): Future[Proposal] = {
    MakeApiClient.get[ProposalResponse](resourceName / proposalId.value).map(Proposal.apply)
  }

  def createProposal(content: String,
                     location: Location,
                     themeId: Option[String] = None,
                     source: String = Source.Core.name,
                     operation: Option[String] = None,
                     question: Option[String] = None): Future[RegisterProposal] = {
    //TODO: set headers everywhere appropriate. For now headers are reset to their previous values with a backup.
    val backupHeaders = MakeApiClient.customHeaders
    MakeApiClient.customHeaders ++= Map[String, String](
      MakeApiClient.sourceHeader -> source,
      MakeApiClient.locationHeader -> location.name
    )
    themeId.foreach(theme => MakeApiClient.customHeaders += MakeApiClient.themeIdHeader -> theme)
    operation.foreach(op  => MakeApiClient.customHeaders += MakeApiClient.operationHeader -> op)
    question.foreach(q    => MakeApiClient.customHeaders += MakeApiClient.questionHeader -> q)
    val registerProposalResponse = MakeApiClient
      .post[RegisterProposalResponse](
        resourceName,
        data = RegisterProposalRequest(content).asJson.pretty(ApiService.printer)
      )
      .map(RegisterProposal.apply)
    MakeApiClient.customHeaders = backupHeaders
    registerProposalResponse
  }

  def searchProposals(content: Option[String] = None,
                      slug: Option[String] = None,
                      themesIds: Seq[ThemeId] = Seq.empty,
                      operationsIds: Seq[OperationId] = Seq.empty,
                      tagsIds: Seq[TagId] = Seq.empty,
                      trending: Option[String] = None,
                      labelsIds: Option[Seq[String]] = None,
                      context: Option[ContextRequest] = Some(ContextRequest()),
                      sort: Seq[SortOptionRequest] = Seq.empty,
                      limit: Option[Int] = None,
                      skip: Option[Int] = None): Future[SearchResult] = {
    MakeApiClient
      .post[SearchResultResponse](
        resourceName / "search",
        data = SearchRequest(
          content = content,
          slug = slug,
          themesIds = if (themesIds.nonEmpty) Some(themesIds.map(_.value)) else None,
          operationsIds = if (operationsIds.nonEmpty) Some(operationsIds.map(_.value)) else None,
          tagsIds = if (tagsIds.nonEmpty) Some(tagsIds.map(_.value)) else None,
          labelsIds = labelsIds,
          context = context,
          limit = limit,
          skip = skip,
          sort = sort
        ).asJson.pretty(ApiService.printer)
      )
      .map(SearchResult.apply)
  }

  def vote(proposalId: ProposalId, voteValue: String): Future[Vote] = {
    MakeApiClient
      .post[VoteResponse](
        apiEndpoint = resourceName / proposalId.value / "vote",
        data = VoteRequest(voteValue).asJson.pretty(ApiService.printer)
      )
      .map(Vote.apply)
  }

  def unvote(proposalId: ProposalId, oldVoteValue: String): Future[Vote] = {
    MakeApiClient
      .post[VoteResponse](
        apiEndpoint = resourceName / proposalId.value / "unvote",
        data = VoteRequest(oldVoteValue).asJson.pretty(ApiService.printer)
      )
      .map(Vote.apply)
  }

  def qualifyVote(proposalId: ProposalId, vote: String, qualification: String): Future[Qualification] = {
    MakeApiClient
      .post[QualificationResponse](
        apiEndpoint = resourceName / proposalId.value / "qualification",
        data = QualificationRequest(voteKey = vote, qualificationKey = qualification).asJson.pretty(ApiService.printer)
      )
      .map(Qualification.apply)
  }

  def removeVoteQualification(proposalId: ProposalId,
                              vote: String,
                              qualification: String): Future[Qualification] = {
    MakeApiClient
      .post[QualificationResponse](
        apiEndpoint = resourceName / proposalId.value / "unqualification",
        data = QualificationRequest(voteKey = vote, qualificationKey = qualification).asJson.pretty(ApiService.printer)
      )
      .map(Qualification.apply)
  }

  final case class UnexpectedException(message: String = I18n.t("errors.unexpected")) extends Exception(message)

}
