package org.make.services.proposal

import org.make.front.models._

import scala.scalajs.js

@js.native
trait RegisterProposalResponse extends js.Object {
  val proposalId: String
}

@js.native
trait SearchResultResponse extends js.Object {
  val total: Int
  val results: Seq[ProposalResponse]
}

case class SearchResult(total: Int, results: Seq[Proposal])

object SearchResult {
  def apply(searchResultResponse: SearchResultResponse): SearchResult = {
    SearchResult(total = searchResultResponse.total, results = searchResultResponse.results.map(Proposal.apply))
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
  val votes: js.Array[Vote]
  val context: ProposalContext
  val trending: js.UndefOr[String]
  val labels: js.Array[String]
  val author: Author
  val country: String
  val language: String
  val themeId: js.UndefOr[String]
  val operationId: js.UndefOr[String]
  val tags: js.Array[Tag]
  val myProposal: Boolean
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


