package org.make.front.models

import org.make.client.models.AuthorResponse
import org.make.services.proposal.{ProposalResponse, QualificationResponse, RegisterProposalResponse, VoteResponse}

import scala.scalajs.js

//todo Add connected user info about proposal
final case class Proposal(id: ProposalId,
                          userId: UserId,
                          content: String,
                          slug: String,
                          status: String,
                          createdAt: js.Date,
                          updatedAt: Option[js.Date],
                          votes: Seq[Vote],
                          context: ProposalContext,
                          trending: Option[String],
                          labels: Seq[String],
                          author: Author,
                          country: String,
                          language: String,
                          themeId: Option[ThemeId],
                          operationId: Option[OperationId],
                          tags: Seq[Tag],
                          myProposal: Boolean) {
  def votesAgree: Vote =
    votes.find(_.key == "agree").getOrElse(Vote(key = "agree", qualifications = Seq.empty, hasVoted = false))
  def votesDisagree: Vote =
    votes.find(_.key == "disagree").getOrElse(Vote(key = "disagree", qualifications = Seq.empty, hasVoted = false))
  def votesNeutral: Vote =
    votes.find(_.key == "neutral").getOrElse(Vote(key = "neutral", qualifications = Seq.empty, hasVoted = false))
}

object Proposal {
  def apply(proposalResponse: ProposalResponse): Proposal = {
    val seqVotes: Seq[Vote] = proposalResponse.votes.map(Vote.apply)
    val seqLabels: Seq[String] = proposalResponse.labels
    val seqTags: Seq[Tag] = proposalResponse.tags.map(Tag.apply)

    Proposal(
      id = ProposalId(proposalResponse.id),
      userId = UserId(proposalResponse.userId),
      content = proposalResponse.content,
      slug = proposalResponse.slug,
      status = proposalResponse.status,
      createdAt = new js.Date(proposalResponse.createdAt),
      updatedAt = proposalResponse.updatedAt.toOption.map(new js.Date(_)),
      votes = seqVotes,
      context = ProposalContext(proposalResponse.context),
      trending = proposalResponse.trending.toOption,
      labels = seqLabels,
      author = Author(proposalResponse.author),
      country = proposalResponse.country,
      language = proposalResponse.language,
      themeId = proposalResponse.themeId.toOption.map(ThemeId.apply),
      operationId = proposalResponse.operationId.toOption.map(OperationId.apply),
      tags = seqTags,
      myProposal = proposalResponse.myProposal
    )
  }
}

case class RegisterProposal(proposalId: ProposalId)

object RegisterProposal {
  def apply(registerProposalResponse: RegisterProposalResponse): RegisterProposal = {
    RegisterProposal(proposalId = ProposalId(registerProposalResponse.proposalId))
  }
}

final case class Vote(key: String, count: Int = 0, qualifications: Seq[Qualification], hasVoted: Boolean)

object Vote {
  def apply(voteResponse: VoteResponse): Vote = {
    Vote(
      key = voteResponse.voteKey,
      count = voteResponse.count,
      qualifications = voteResponse.qualifications.map(Qualification.apply),
      hasVoted = voteResponse.hasVoted
    )
  }
}

final case class Qualification(key: String, count: Int = 0, hasQualified: Boolean)

object Qualification {
  def apply(qualificationResponse: QualificationResponse): Qualification = {
    Qualification(
      key = qualificationResponse.qualificationKey,
      count = qualificationResponse.count,
      hasQualified = qualificationResponse.hasQualified
    )
  }
}

final case class ProposalContext(operation: Option[String],
                                 source: Option[String],
                                 location: Option[String],
                                 question: Option[String])

object ProposalContext {
  def apply(proposalContextResponse: ProposalContextResponse): ProposalContext = {
    ProposalContext(
      operation = proposalContextResponse.operation.toOption,
      source = proposalContextResponse.source.toOption,
      location = proposalContextResponse.location.toOption,
      question = proposalContextResponse.question.toOption
    )
  }
}

@js.native
trait ProposalContextResponse extends js.Object {
  val operation: js.UndefOr[String]
  val source: js.UndefOr[String]
  val location: js.UndefOr[String]
  val question: js.UndefOr[String]
}

final case class Author(firstName: Option[String], postalCode: Option[String], age: Option[Int])

object Author {
  def apply(authorResponse: AuthorResponse): Author = {
    Author(
      firstName = authorResponse.firstName.toOption,
      postalCode = authorResponse.postalCode.toOption,
      age = authorResponse.age.toOption
    )
  }
}

final case class ProposalId(value: String)

case class ProposalSearchResult(proposals: Seq[Proposal], hasMore: Boolean)
