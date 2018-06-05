package org.make.services.proposal

import org.make.client.models.{AuthorResponse, OrganisationInfoResponse}
import org.make.front.models._

import scala.scalajs.js

@js.native
trait RegisterProposalResponse extends js.Object {
  val proposalId: String
}

@js.native
trait SearchResultResponse extends js.Object {
  val total: Int
  val results: js.Array[ProposalResponse]
  val seed: js.UndefOr[Int]
}

case class SearchResult(total: Int, results: js.Array[Proposal], seed: Option[Int])

object SearchResult {
  def apply(searchResultResponse: SearchResultResponse): SearchResult = {
    SearchResult(
      total = searchResultResponse.total,
      results = searchResultResponse.results.map(Proposal.apply),
      seed = searchResultResponse.seed.toOption
    )
  }
}

@js.native
trait ProposalResponse extends js.Object {
  val id: String
  val userId: String
  val content: String
  val slug: String
  val status: String
  val createdAt: String
  val updatedAt: js.UndefOr[String]
  val votes: js.Array[VoteResponse]
  val context: ProposalContextResponse
  val trending: js.UndefOr[String]
  val labels: js.Array[String]
  val author: AuthorResponse
  val organisations: js.Array[OrganisationInfoResponse]
  val country: String
  val language: String
  val themeId: js.UndefOr[String]
  val tags: js.Array[TagResponse]
  val myProposal: Boolean
  val operationId: js.UndefOr[String]
}

@js.native
trait VoteResponse extends js.Object {
  val voteKey: String
  val count: Int
  val qualifications: js.Array[QualificationResponse]
  val hasVoted: Boolean
}

@js.native
trait QualificationResponse extends js.Object {
  val qualificationKey: String
  val count: Int
  val hasQualified: Boolean
}

@js.native
trait TagResponse extends js.Object {
  val tagId: String
  val label: String
}
