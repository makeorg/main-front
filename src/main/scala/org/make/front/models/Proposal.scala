package org.make.front.models

//todo Add connected user info about proposal
final case class Proposal(content: String,
                          authorFirstname: String,
                          authorPostalCode: Option[String],
                          authorAge: Option[Int],
                          nbVoteAgree: Int = 0,
                          nbVoteDisagree: Int = 0,
                          nbVoteNeutral: Int = 0,
                          nbQualifLikeIt: Int = 0,
                          nbQualifDoable: Int = 0,
                          nbQualifPlatitudeAgree: Int = 0,
                          nbQualifNoWay: Int = 0,
                          nbQualifImpossible: Int = 0,
                          nbQualifPlatitudeDisagree: Int = 0,
                          nbQualifDoNotUnderstand: Int = 0,
                          nbQualifNoOpinion: Int = 0,
                          nbQualifDoNotCare: Int = 0,
                          tags: Seq[Tag])

final case class VoteStats(nbQualifTop: Int, nbQualifMiddle: Int, nbQualifBottom: Int, totalVote: Int)

sealed trait VoteType {
  val translationKey: String
}

case object VoteAgree extends VoteType { override val translationKey = "qualifAgree" }
case object VoteDisagree extends VoteType { override val translationKey = "qualifDisagree" }
case object VoteNeutral extends VoteType { override val translationKey = "qualifNeutral" }

object Proposal {

  def getVoteStats(proposal: Proposal, qualificationType: VoteType): VoteStats = {
    qualificationType match {
      case VoteAgree =>
        VoteStats(
          proposal.nbQualifLikeIt,
          proposal.nbQualifDoable,
          proposal.nbQualifPlatitudeAgree,
          proposal.nbVoteAgree
        )
      case VoteDisagree =>
        VoteStats(
          proposal.nbQualifNoWay,
          proposal.nbQualifImpossible,
          proposal.nbQualifPlatitudeDisagree,
          proposal.nbVoteDisagree
        )
      case VoteNeutral =>
        VoteStats(
          proposal.nbQualifDoNotUnderstand,
          proposal.nbQualifNoOpinion,
          proposal.nbQualifDoNotCare,
          proposal.nbVoteNeutral
        )
    }
  }

}
