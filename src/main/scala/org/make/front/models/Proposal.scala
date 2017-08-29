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
                          voteAgree: Vote,
                          voteDisagree: Vote,
                          voteNeutral: Vote,
                          proposalContext: ProposalContext,
                          trending: Option[String],
                          labels: Seq[String],
                          author: Author,
                          country: String,
                          language: String,
                          themeId: Option[ThemeId],
                          tags: Seq[Tag])

final case class Qualification(key: String, count: Int = 0, selected: Boolean = false)
final case class Vote(key: String, selected: Boolean = false, count: Int = 0, qualifications: Seq[Qualification])

final case class ProposalContext(operation: Option[String],
                                 source: Option[String],
                                 location: Option[String],
                                 question: Option[String])

final case class Author(firstname: Option[String], postalCode: Option[String], age: Option[Int])

final case class ProposalId(value: String) extends StringValue

object ProposalId {
  implicit lazy val proposalIdEncoder: Encoder[ProposalId] = (a: ProposalId) => Json.fromString(a.value)
  implicit lazy val proposalIdDecoder: Decoder[ProposalId] = Decoder.decodeString.map(ProposalId(_))
}

object Proposal {
  def getPercentageVote(count: Int, totalVote: Int): String = {
    formatToKilo(count * 100 / totalVote)
  }
}
