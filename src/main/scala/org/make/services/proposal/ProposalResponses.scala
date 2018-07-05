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

package org.make.services.proposal

import org.make.front.helpers.UndefToOption.undefToOption
import org.make.client.models.{AuthorResponse, OrganisationInfoResponse}
import org.make.front.models._

import scala.scalajs.js

@js.native
trait RegisterProposalResponse extends js.Object {
  val proposalId: String
}

@js.native
trait SearchResultResponse extends js.Object {
  val total: Int
  val results: js.Array[ProposalResponse]
  val seed: js.UndefOr[Int]
}

case class SearchResult(total: Int, results: js.Array[Proposal], seed: Option[Int])

object SearchResult {
  def apply(searchResultResponse: SearchResultResponse): SearchResult = {
    SearchResult(
      total = searchResultResponse.total,
      results = searchResultResponse.results.map(Proposal.apply),
      seed = undefToOption(searchResultResponse.seed)
    )
  }
}

@js.native
trait ProposalResponse extends js.Object {
  val id: String
  val userId: String
  val content: String
  val slug: String
  val status: String
  val createdAt: String
  val updatedAt: js.UndefOr[String]
  val votes: js.Array[VoteResponse]
  val context: ProposalContextResponse
  val trending: js.UndefOr[String]
  val labels: js.Array[String]
  val author: AuthorResponse
  val organisations: js.Array[OrganisationInfoResponse]
  val country: String
  val language: String
  val themeId: js.UndefOr[String]
  val tags: js.Array[TagResponse]
  val myProposal: Boolean
  val operationId: js.UndefOr[String]
}

@js.native
trait VoteResponse extends js.Object {
  val voteKey: String
  val count: Int
  val qualifications: js.Array[QualificationResponse]
  val hasVoted: Boolean
}

@js.native
trait QualificationResponse extends js.Object {
  val qualificationKey: String
  val count: Int
  val hasQualified: Boolean
  val activateTooltip: Boolean
}

@js.native
trait TagResponse extends js.Object {
  val tagId: String
  val label: String
}
