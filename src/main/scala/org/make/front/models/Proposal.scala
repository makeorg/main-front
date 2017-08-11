package org.make.front.models

final case class Proposal(content: String,
                          authorFirstname: String,
                          authorPostalCode: Option[String],
                          authorAge: Option[Int],
                          nbVoteAgree: Int = 0,
                          nbVoteDisagree: Int = 0,
                          nbVoteNeutral: Int = 0,
                          tags: Seq[Tag])
