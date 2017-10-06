package org.make.front.models

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder, Json}
import org.make.core.StringValue
import io.circe.java8.time._

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
  implicit val decoder: Decoder[Proposal] =
    Decoder.forProduct18(
      "id",
      "userId",
      "content",
      "slug",
      "status",
      "createdAt",
      "updatedAt",
      "votes",
      "context",
      "trending",
      "labels",
      "author",
      "country",
      "language",
      "themeId",
      "operationId",
      "tags",
      "myProposal"
    )(Proposal.apply)
}

final case class Qualification(key: String, count: Int = 0, hasQualified: Boolean)

object Qualification {
  implicit val decoder: Decoder[Qualification] =
    Decoder.forProduct3("qualificationKey", "count", "hasQualified")(Qualification.apply)
}

final case class Vote(key: String, count: Int = 0, qualifications: Seq[Qualification], hasVoted: Boolean)

object Vote {
  implicit val decoder: Decoder[Vote] =
    Decoder.forProduct4("voteKey", "count", "qualifications", "hasVoted")(Vote.apply)
}

final case class ProposalContext(operation: Option[String],
                                 source: Option[String],
                                 location: Option[String],
                                 question: Option[String])

object ProposalContext {
  implicit val decoder: Decoder[ProposalContext] =
    Decoder.forProduct4("operation", "source", "location", "question")(ProposalContext.apply)
}

final case class Author(firstName: Option[String], postalCode: Option[String], age: Option[Int])

object Author {
  implicit val decoder: Decoder[Author] = Decoder.forProduct3("firstName", "postalCode", "age")(Author.apply)
}

final case class ProposalId(value: String) extends StringValue

object ProposalId {
  implicit lazy val proposalIdEncoder: Encoder[ProposalId] = (a: ProposalId) => Json.fromString(a.value)
  implicit lazy val proposalIdDecoder: Decoder[ProposalId] = Decoder.decodeString.map(ProposalId(_))
}

case class ProposalSearchResult(proposals: Seq[Proposal], hasMore: Boolean)
