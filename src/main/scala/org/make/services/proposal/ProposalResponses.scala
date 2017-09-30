package org.make.services.proposal

import io.circe.Decoder

object ProposalResponses {
  final case class RegisterProposalResponse(proposalId: String)
  final case class VoteResponse(voteKey: String,
                                count: Int = 0,
                                qualifications: Seq[QualificationResponse],
                                hasVoted: Boolean)

  object VoteResponse {
    implicit val decoder: Decoder[VoteResponse] =
      Decoder.forProduct4("voteKey", "count", "qualifications", "hasVoted")(VoteResponse.apply)
  }

  final case class QualificationResponse(qualificationKey: String, count: Int = 0, hasQualified: Boolean)

  object QualificationResponse {
    implicit val decoder: Decoder[QualificationResponse] =
      Decoder.forProduct3("qualificationKey", "count", "hasQualified")(QualificationResponse.apply)
  }

}
