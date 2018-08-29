/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.models

import org.make.client.models.{AuthorResponse, OrganisationInfoResponse}
import org.make.front.helpers.UndefToOption.undefToOption
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

  def isPending: Boolean = status == "Pending"
  def isPostponed: Boolean = status == "Postponed"
  def isAccepted: Boolean = status == "Accepted"
  def isRefused: Boolean = status == "Refused"
  def isArchived: Boolean = status == "Archived"
}

object Proposal {

  def apply(proposalResponse: ProposalResponse): Proposal = {
    val seqVotes: js.Array[Vote] = proposalResponse.votes.map(Vote.apply)
    val seqLabels: js.Array[String] = proposalResponse.labels
    val seqTags: js.Array[Tag] = proposalResponse.tags.filter(tag => tag.display).map(Tag.apply)
    val seqOrganisationsInfo: js.Array[OrganisationInfo] = proposalResponse.organisations.map(OrganisationInfo(_))

    Proposal(
      id = ProposalId(proposalResponse.id),
      userId = UserId(proposalResponse.userId),
      content = proposalResponse.content,
      slug = proposalResponse.slug,
      status = proposalResponse.status,
      createdAt = new js.Date(proposalResponse.createdAt),
      updatedAt = undefToOption(proposalResponse.updatedAt).map(new js.Date(_)),
      votes = seqVotes,
      context = ProposalContext(proposalResponse.context),
      trending = undefToOption(proposalResponse.trending),
      labels = seqLabels,
      author = Author(proposalResponse.author),
      organisations = seqOrganisationsInfo,
      country = proposalResponse.country,
      language = proposalResponse.language,
      themeId = undefToOption(proposalResponse.themeId).map(ThemeId.apply),
      operationId = undefToOption(proposalResponse.operationId).flatMap {
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

final case class Qualification(key: String, count: Int = 0, hasQualified: Boolean, activateTooltip: Boolean)

object Qualification {
  def apply(qualificationResponse: QualificationResponse): Qualification = {
    Qualification(
      key = qualificationResponse.qualificationKey,
      count = qualificationResponse.count,
      hasQualified = qualificationResponse.hasQualified,
      activateTooltip = false
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
      operation = undefToOption(proposalContextResponse.operation),
      source = undefToOption(proposalContextResponse.source),
      location = undefToOption(proposalContextResponse.location),
      question = undefToOption(proposalContextResponse.question)
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
      firstName = undefToOption(authorResponse.firstName),
      organisationName = undefToOption(authorResponse.organisationName),
      postalCode = undefToOption(authorResponse.postalCode),
      age = undefToOption(authorResponse.age),
      avatarUrl = undefToOption(authorResponse.avatarUrl)
    )
  }
}

final case class OrganisationInfo(organisationId: UserId, organisationName: Option[String])

object OrganisationInfo {
  def apply(organisationInfoResponse: OrganisationInfoResponse): OrganisationInfo = new OrganisationInfo(
    organisationId = UserId(organisationInfoResponse.organisationId),
    organisationName = undefToOption(organisationInfoResponse.organisationName)
  )
}

final case class ProposalId(value: String)

case class ProposalSearchResult(proposals: js.Array[Proposal], hasMore: Boolean)

sealed trait SortAlgorithm { val shortName: String }

object SortAlgorithm {
  val sortAlgorithms: Map[String, SortAlgorithm] = Map(
    RandomAlgorithm.shortName -> RandomAlgorithm,
    ActorVoteAlgorithm.shortName -> ActorVoteAlgorithm,
    ControversyAlgorithm.shortName -> ControversyAlgorithm,
    PopularAlgorithm.shortName -> PopularAlgorithm
  )

  def matchSortAlgorithm(sortAlgorithm: String): Option[SortAlgorithm] = sortAlgorithms.get(sortAlgorithm)
}

case object RandomAlgorithm extends SortAlgorithm { override val shortName: String = "random" }
case object ActorVoteAlgorithm extends SortAlgorithm { override val shortName: String = "actorVote" }
case object ControversyAlgorithm extends SortAlgorithm { override val shortName: String = "controversy" }
case object PopularAlgorithm extends SortAlgorithm { override val shortName: String = "popular" }
