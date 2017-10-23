package org.make.front.models

import java.time.ZonedDateTime

import org.make.client.models.AuthorResponse
import org.make.services.proposal.{ProposalResponse, QualificationResponse, RegisterProposalResponse, VoteResponse}

import scala.collection.mutable
import scala.scalajs.js

//todo Add connected user info about proposal
final case class Proposal(id: ProposalId,
                          userId: UserId,
                          content: String,
                          slug: String,
                          status: String,
                          createdAt: ZonedDateTime,
                          updatedAt: Option[ZonedDateTime],
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
    val seqVotes: mutable.Seq[Vote] = proposalResponse.votes
    val seqLabels: mutable.Seq[String] = proposalResponse.labels
    val seqTags: mutable.Seq[Tag] = proposalResponse.tags

    Proposal(id = ProposalId(proposalResponse.id),
            userId = UserId(proposalResponse.userId),
            content = proposalResponse.content,
            slug = proposalResponse.slug,
            status = proposalResponse.status,
            createdAt = ZonedDateTime.parse(proposalResponse.createdAt),
            updatedAt = Some(ZonedDateTime.parse(proposalResponse.updatedAt.get)),
            votes = seqVotes,
            context = proposalResponse.context,
            trending = proposalResponse.trending.toOption,
            labels = seqLabels,
            author = proposalResponse.author,
            country = proposalResponse.country,
            language = proposalResponse.language,
            themeId = Some(ThemeId(proposalResponse.themeId.get)),
            operationId = Some(OperationId(proposalResponse.operationId.get)),
            tags = seqTags,
            myProposal = proposalResponse.myProposal)
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
      hasVoted = voteResponse.hasVoted)
  }
}

final case class Qualification(key: String, count: Int = 0, hasQualified: Boolean)

object Qualification {
  def apply(qualificationResponse: QualificationResponse): Qualification = {
    Qualification(
      key =  qualificationResponse.qualificationKey,
      count = qualificationResponse.count,
      hasQualified = qualificationResponse.hasQualified)
  }
}

@js.native
trait ProposalContext extends js.Object {
  val operation: js.UndefOr[String]
  val source: js.UndefOr[String]
  val location: js.UndefOr[String]
  val question: js.UndefOr[String]
}

object ProposalContext {
  def apply(operation: js.UndefOr[String],
            source: js.UndefOr[String],
            location: js.UndefOr[String],
            question: js.UndefOr[String]): ProposalContext = {
    js.Dynamic.literal(
      operation = operation,
      source = source,
      location = location,
      question = question
    ).asInstanceOf[ProposalContext]
  }
}

final case class Author(firstName: Option[String], postalCode: Option[String], age: Option[Int])

object Author {
  def apply(authorResponse: AuthorResponse): Author = {
    Author(
      firstName = authorResponse.firstName.toOption,
      postalCode = authorResponse.postalCode.toOption,
      age = authorResponse.age.toOption)
  }
}

trait ProposalId extends js.Object {
  val value: String
}

object ProposalId {
  def apply(value: String): ProposalId = {
    js.Dynamic.literal(value = value).asInstanceOf[ProposalId]
  }
}

case class ProposalSearchResult(proposals: Seq[Proposal], hasMore: Boolean)
