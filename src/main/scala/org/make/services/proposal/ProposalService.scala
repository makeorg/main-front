package org.make.services.proposal

import org.make.client.MakeApiClient
import org.make.core.URI._
import org.make.front.facades.I18n
import org.make.front.models._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSON

object ProposalService extends ApiService {

  override val resourceName: String = "proposals"

  val defaultResultsCount = 20

  def getProposalById(proposalId: ProposalId): Future[Proposal] = {
    MakeApiClient.get[ProposalResponse](resourceName / proposalId.value).map(Proposal.apply)
  }

  def createProposal(content: String,
                     location: Location,
                     themeId: Option[String] = None,
                     source: String = Source.Core.name,
                     operation: Option[Operation] = None,
                     question: Option[String] = None): Future[RegisterProposal] = {
    var headers =
      Map[String, String](MakeApiClient.sourceHeader -> source, MakeApiClient.locationHeader -> location.name)
    themeId.foreach(theme => headers += MakeApiClient.themeIdHeader -> theme)
    operation.foreach(op  => headers += MakeApiClient.operationHeader -> op.label)
    question.foreach(q    => headers += MakeApiClient.questionHeader -> q)
    MakeApiClient
      .post[RegisterProposalResponse](
        resourceName,
        data = JSON.stringify(JsRegisterProposalRequest(RegisterProposalRequest(content))),
        headers = headers
      )
      .map(RegisterProposal.apply)
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
                      skip: Option[Int] = None,
                      isRandom: Option[Boolean] = Some(true),
                      seed: Option[Int] = None): Future[SearchResult] = {
    MakeApiClient
      .post[SearchResultResponse](
        resourceName / "search",
        data = JSON.stringify(
          JsSearchRequest(
            SearchRequest(
              content = content,
              slug = slug,
              themesIds = if (themesIds.nonEmpty) Some(themesIds.map(_.value)) else None,
              operationsIds = if (operationsIds.nonEmpty) Some(operationsIds.map(_.value)) else None,
              tagsIds = if (tagsIds.nonEmpty) Some(tagsIds.map(_.value)) else None,
              labelsIds = labelsIds,
              context = context,
              limit = limit,
              skip = skip,
              sort = sort,
              isRandom = isRandom,
              seed = seed
            )
          )
        )
      )
      .map(SearchResult.apply)
  }

  def vote(proposalId: ProposalId,
           voteValue: String,
           location: Location,
           source: String = Source.Core.name,
           operation: Option[Operation] = None,
           question: Option[String] = None): Future[Vote] = {
    var headers =
      Map[String, String](MakeApiClient.sourceHeader -> source, MakeApiClient.locationHeader -> location.name)
    operation.foreach(op => headers += MakeApiClient.operationHeader -> op.label)
    question.foreach(q   => headers += MakeApiClient.questionHeader -> q)

    MakeApiClient
      .post[VoteResponse](
        apiEndpoint = resourceName / proposalId.value / "vote",
        data = JSON.stringify(JsVoteRequest(VoteRequest(voteValue))),
        headers = headers
      )
      .map(Vote.apply)
  }

  def unvote(proposalId: ProposalId,
             oldVoteValue: String,
             location: Location,
             source: String = Source.Core.name,
             operation: Option[Operation] = None,
             question: Option[String] = None): Future[Vote] = {
    var headers =
      Map[String, String](MakeApiClient.sourceHeader -> source, MakeApiClient.locationHeader -> location.name)
    operation.foreach(op => headers += MakeApiClient.operationHeader -> op.label)
    question.foreach(q   => headers += MakeApiClient.questionHeader -> q)

    MakeApiClient
      .post[VoteResponse](
        apiEndpoint = resourceName / proposalId.value / "unvote",
        data = JSON.stringify(JsVoteRequest(VoteRequest(oldVoteValue))),
        headers = headers
      )
      .map(Vote.apply)
  }

  def qualifyVote(proposalId: ProposalId,
                  vote: String,
                  qualification: String,
                  location: Location,
                  source: String = Source.Core.name,
                  operation: Option[Operation] = None,
                  question: Option[String] = None): Future[Qualification] = {
    var headers =
      Map[String, String](MakeApiClient.sourceHeader -> source, MakeApiClient.locationHeader -> location.name)
    operation.foreach(op => headers += MakeApiClient.operationHeader -> op.label)
    question.foreach(q   => headers += MakeApiClient.questionHeader -> q)

    MakeApiClient
      .post[QualificationResponse](
        apiEndpoint = resourceName / proposalId.value / "qualification",
        data = JSON
          .stringify(JsQualificationRequest(QualificationRequest(voteKey = vote, qualificationKey = qualification))),
        headers = headers
      )
      .map(Qualification.apply)
  }

  def removeVoteQualification(proposalId: ProposalId,
                              vote: String,
                              qualification: String,
                              location: Location,
                              source: String = Source.Core.name,
                              operation: Option[Operation] = None,
                              question: Option[String] = None): Future[Qualification] = {
    var headers =
      Map[String, String](MakeApiClient.sourceHeader -> source, MakeApiClient.locationHeader -> location.name)
    operation.foreach(op => headers += MakeApiClient.operationHeader -> op.label)
    question.foreach(q   => headers += MakeApiClient.questionHeader -> q)

    MakeApiClient
      .post[QualificationResponse](
        apiEndpoint = resourceName / proposalId.value / "unqualification",
        data = JSON
          .stringify(JsQualificationRequest(QualificationRequest(voteKey = vote, qualificationKey = qualification))),
        headers = headers
      )
      .map(Qualification.apply)
  }

  final case class UnexpectedException(message: String = I18n.t("errors.unexpected")) extends Exception(message)

}
