package org.make.front.models

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder, Json}
import org.make.core.StringValue
import org.make.front.helpers.NumberFormat.formatToKilo
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
                          tags: Seq[Tag]) {
  def votesAgree: Vote = votes.find(_.key == "agree").getOrElse(Vote(key = "agree", qualifications = Seq.empty))
  def votesDisagree: Vote =
    votes.find(_.key == "disagree").getOrElse(Vote(key = "disagree", qualifications = Seq.empty))
  def votesNeutral: Vote = votes.find(_.key == "neutral").getOrElse(Vote(key = "neutral", qualifications = Seq.empty))

}

final case class Qualification(key: String, count: Int = 0, selected: Option[Boolean] = None)
final case class Vote(key: String, count: Int = 0, qualifications: Seq[Qualification], selected: Option[Boolean] = None)

final case class ProposalContext(operation: Option[String],
                                 source: Option[String],
                                 location: Option[String],
                                 question: Option[String])

final case class Author(firstName: Option[String], postalCode: Option[String], age: Option[Int])

final case class ProposalId(value: String) extends StringValue

object ProposalId {
  implicit lazy val proposalIdEncoder: Encoder[ProposalId] = (a: ProposalId) => Json.fromString(a.value)
  implicit lazy val proposalIdDecoder: Decoder[ProposalId] = Decoder.decodeString.map(ProposalId(_))
}

case class ProposalSearchResult(proposals: Seq[Proposal], hasMore: Boolean)
