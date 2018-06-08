package org.make.front.models

import org.make.client.models.{AuthorResponse, OrganisationInfoResponse}
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
                          votes: js.Array[Vote],
                          context: ProposalContext,
                          trending: Option[String],
                          labels: js.Array[String],
                          author: Author,
                          organisations: js.Array[OrganisationInfo],
                          country: String,
                          language: String,
                          themeId: Option[ThemeId],
                          operationId: Option[OperationId],
                          tags: js.Array[Tag],
                          myProposal: Boolean) {
  def votesAgree: Vote =
    votes.find(_.key == "agree").getOrElse(Vote(key = "agree", qualifications = js.Array(), hasVoted = false))
  def votesDisagree: Vote =
    votes.find(_.key == "disagree").getOrElse(Vote(key = "disagree", qualifications = js.Array(), hasVoted = false))
  def votesNeutral: Vote =
    votes.find(_.key == "neutral").getOrElse(Vote(key = "neutral", qualifications = js.Array(), hasVoted = false))
}

object Proposal {
  def apply(proposalResponse: ProposalResponse): Proposal = {
    val seqVotes: js.Array[Vote] = proposalResponse.votes.map(Vote.apply)
    val seqLabels: js.Array[String] = proposalResponse.labels
    val seqTags: js.Array[Tag] = proposalResponse.tags.filter(tag => !tag.label.contains(":")).map(Tag.apply)
    val seqOrganisationsInfo: js.Array[OrganisationInfo] = proposalResponse.organisations.map(OrganisationInfo(_))

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
      organisations = seqOrganisationsInfo,
      country = proposalResponse.country,
      language = proposalResponse.language,
      themeId = Option(proposalResponse.themeId).flatMap(_.toOption).map(ThemeId.apply),
      operationId = Option(proposalResponse.operationId).flatMap(_.toOption).flatMap {
        case ""    => None
        case other => Some(OperationId(other))
      },
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

final case class Vote(key: String, count: Int = 0, qualifications: js.Array[Qualification], hasVoted: Boolean)

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

final case class Author(firstName: Option[String],
                        organisationName: Option[String],
                        postalCode: Option[String],
                        age: Option[Int],
                        avatarUrl: Option[String])

object Author {
  def apply(authorResponse: AuthorResponse): Author = {
    Author(
      firstName = Option(authorResponse.firstName).flatMap(_.toOption),
      organisationName = Option(authorResponse.organisationName).flatMap(_.toOption),
      postalCode = authorResponse.postalCode.toOption,
      age = authorResponse.age.toOption,
      avatarUrl = Option(authorResponse.avatarUrl).flatMap(_.toOption)
    )
  }
}

final case class OrganisationInfo(organisationId: UserId, organisationName: Option[String])

object OrganisationInfo {
  def apply(organisationInfoResponse: OrganisationInfoResponse): OrganisationInfo = new OrganisationInfo(
    organisationId = UserId(organisationInfoResponse.organisationId),
    organisationName = organisationInfoResponse.organisationName.toOption
  )
}

final case class ProposalId(value: String)

case class ProposalSearchResult(proposals: js.Array[Proposal], hasMore: Boolean)
